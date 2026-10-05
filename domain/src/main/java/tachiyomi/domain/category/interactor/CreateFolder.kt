package tachiyomi.domain.category.interactor

import logcat.LogPriority
import tachiyomi.core.common.util.lang.withNonCancellableContext
import tachiyomi.core.common.util.system.logcat
import tachiyomi.domain.category.model.Category
import tachiyomi.domain.category.repository.CategoryRepository

/**
 * Creates a virtual folder. A folder is a [Category] row flagged with [Category.isFolder] = true, so
 * it reuses all category membership plumbing but is rendered as a tile (not a tab) and never syncs.
 */
class CreateFolder(
    private val categoryRepository: CategoryRepository,
) {

    suspend fun await(name: String, locked: Boolean = false): Result = withNonCancellableContext {
        val categories = categoryRepository.getAll()
        val nextOrder = categories.maxOfOrNull { it.order }?.plus(1) ?: 0
        val newFolder = Category(
            id = 0,
            name = name,
            order = nextOrder,
            flags = 0,
            isFolder = true,
            locked = locked,
        )

        try {
            val id = categoryRepository.insert(newFolder)
                ?: error("Failed to insert folder")
            Result.Success(newFolder.copy(id = id))
        } catch (e: Exception) {
            logcat(LogPriority.ERROR, e)
            Result.InternalError(e)
        }
    }

    sealed interface Result {
        data class Success(val category: Category) : Result
        data class InternalError(val error: Throwable) : Result
    }
}
