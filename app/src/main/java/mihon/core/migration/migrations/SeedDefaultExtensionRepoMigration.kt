package mihon.core.migration.migrations

import logcat.LogPriority
import mihon.core.migration.Migration
import mihon.core.migration.MigrationContext
import mihon.domain.extension.interactor.AddExtensionStore
import mihon.domain.extension.interactor.GetExtensionStores
import tachiyomi.core.common.util.lang.withIOContext
import tachiyomi.core.common.util.system.logcat

/**
 * Seeds the default extension store (Keiyoushi, the community-maintained store for
 * Mihon/Tachiyomi forks) so the app ships with a working list of installable extensions.
 *
 * Runs always (so it also applies to fresh installs, which only execute [Migration.isAlways]
 * migrations) but only when no store is configured yet. This seeds first launch without
 * ever re-adding a store the user has intentionally removed or replaced. Adding a store needs
 * the network, so an offline first launch simply retries on the next start.
 */
class SeedDefaultExtensionRepoMigration : Migration {
    override val version: Float = Migration.ALWAYS

    override suspend fun invoke(migrationContext: MigrationContext): Boolean = withIOContext {
        val getExtensionStores = migrationContext.get<GetExtensionStores>() ?: return@withIOContext false
        val addExtensionStore = migrationContext.get<AddExtensionStore>() ?: return@withIOContext false

        // Only seed when the user has no stores configured.
        if (getExtensionStores.get().isNotEmpty()) return@withIOContext false

        addExtensionStore(KEIYOUSHI_INDEX_URL).onFailure {
            logcat(LogPriority.ERROR, it) { "Error seeding default extension store" }
        }
        return@withIOContext true
    }

    private companion object {
        const val KEIYOUSHI_INDEX_URL = "https://github.com/keiyoushi/extensions/raw/repo/index.pb"
    }
}
