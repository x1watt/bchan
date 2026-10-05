package eu.kanade.tachiyomi.data.backup.restore

import android.content.Context
import com.hippo.unifile.UniFile
import eu.kanade.tachiyomi.data.backup.BackupDecoder
import logcat.LogPriority
import tachiyomi.core.common.util.system.logcat
import tachiyomi.domain.manga.interactor.GetFavorites
import tachiyomi.domain.storage.service.AUTOMATIC_BACKUPS_PATH
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

/**
 * Looks for a backup worth restoring inside the storage folder.
 *
 * Picking that folder is how a reinstall gets its downloads and automatic backups back, so the
 * library they belong to should come back with them, rather than the user having to know that a
 * separate restore step exists.
 */
class StorageBackupFinder(
    private val context: Context,
    private val getFavorites: GetFavorites = Injekt.get(),
) {

    /**
     * The newest backup under [root] that actually holds entries, or null when there is nothing to
     * restore — or when the library already has entries, since restoring must never overwrite a
     * library the user has already built.
     *
     * [root] is the folder as just picked rather than one read back from
     * [tachiyomi.domain.storage.service.StorageManager], whose base directory is updated
     * asynchronously and would still be the previous folder at this point.
     */
    suspend fun findForEmptyLibrary(root: UniFile?): UniFile? {
        if (root == null) return null
        if (getFavorites.await().isNotEmpty()) return null

        return candidates(root).firstOrNull(::hasEntries)
    }

    /**
     * Backups under [root], newest first. Automatic backups live in their own subfolder, but a
     * manual export dropped in the folder itself is just as restorable, so both are considered.
     *
     * Files from every install count, not just this one:
     * [eu.kanade.tachiyomi.data.backup.create.BackupCreator] names its files after the application
     * id, so a debug build would otherwise ignore the release build's backups sitting right beside
     * it — which is exactly the case worth handling.
     */
    private fun candidates(root: UniFile): List<UniFile> {
        val autoBackups = root.findFile(AUTOMATIC_BACKUPS_PATH)?.takeIf { it.isDirectory }
        return listOfNotNull(autoBackups, root)
            .flatMap { dir -> dir.listFiles().orEmpty().asIterable() }
            .filter { it.isFile && it.name?.endsWith(BACKUP_EXTENSION) == true }
            .sortedByDescending { it.lastModified() }
            .take(MAX_CANDIDATES)
    }

    /**
     * Decodes the backup to see whether it carries any entries. An install that has only ever run
     * with an empty library still writes automatic backups, and restoring one of those would look
     * exactly like the feature not working.
     */
    private fun hasEntries(file: UniFile): Boolean {
        return try {
            BackupDecoder(context).decode(file.uri).backupManga.isNotEmpty()
        } catch (e: Exception) {
            logcat(LogPriority.WARN, e) { "Ignoring unreadable backup ${file.name}" }
            false
        }
    }
}

private const val BACKUP_EXTENSION = ".tachibk"

/** Enough to look past a run of empty automatic backups without decoding a whole folder. */
private const val MAX_CANDIDATES = 10
