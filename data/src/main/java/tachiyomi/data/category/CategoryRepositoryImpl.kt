package tachiyomi.data.category

import app.cash.sqldelight.async.coroutines.awaitAsList
import app.cash.sqldelight.async.coroutines.awaitAsOneOrNull
import kotlinx.coroutines.flow.Flow
import tachiyomi.data.Database
import tachiyomi.data.category.CategoryMapper.mapCategory
import tachiyomi.data.subscribeToList
import tachiyomi.domain.category.model.Category
import tachiyomi.domain.category.model.CategoryUpdate
import tachiyomi.domain.category.repository.CategoryRepository

class CategoryRepositoryImpl(
    private val database: Database,
) : CategoryRepository {

    override suspend fun get(id: Long): Category? {
        return database.categoriesQueries
            .getCategory(id, ::mapCategory)
            .awaitAsOneOrNull()
    }

    override suspend fun getAll(): List<Category> {
        return database.categoriesQueries
            .getCategories(::mapCategory)
            .awaitAsList()
    }

    override fun getAllAsFlow(): Flow<List<Category>> {
        return database.categoriesQueries
            .getCategories(::mapCategory)
            .subscribeToList()
    }

    override suspend fun getCategoriesByMangaId(mangaId: Long): List<Category> {
        return database.categoriesQueries
            .getCategoriesByMangaId(mangaId, ::mapCategory)
            .awaitAsList()
    }

    override fun getCategoriesByMangaIdAsFlow(mangaId: Long): Flow<List<Category>> {
        return database.categoriesQueries
            .getCategoriesByMangaId(mangaId, ::mapCategory)
            .subscribeToList()
    }

    override suspend fun getFolders(): List<Category> {
        return database.categoriesQueries
            .getFolders(::mapCategory)
            .awaitAsList()
    }

    override fun getFoldersAsFlow(): Flow<List<Category>> {
        return database.categoriesQueries
            .getFolders(::mapCategory)
            .subscribeToList()
    }

    // SY -->
    override suspend fun insert(category: Category): Long? {
        return database.categoriesQueries.insert(
            name = category.name,
            order = category.order,
            flags = category.flags,
            version = category.version,
            uid = category.uid,
            last_modified_at = category.lastModifiedAt,
            isFolder = if (category.isFolder) 1L else 0L,
            cover = category.cover,
            locked = if (category.locked) 1L else 0L,
        ).awaitAsOneOrNull()
    }
    // SY <--

    override suspend fun setFolderCover(categoryId: Long, cover: String?) {
        database.categoriesQueries.setFolderCover(cover = cover, categoryId = categoryId)
    }

    override suspend fun updatePartial(update: CategoryUpdate) {
        database.categoriesQueries.update(
            name = update.name,
            order = update.order,
            flags = update.flags,
            version = update.version,
            uid = update.uid,
            last_modified_at = update.lastModifiedAt,
            isSyncing = null,
            isFolder = update.isFolder?.let { if (it) 1L else 0L },
            cover = update.cover,
            locked = update.locked?.let { if (it) 1L else 0L },
            categoryId = update.id,
        )
    }

    override suspend fun updatePartial(updates: List<CategoryUpdate>) {
        database.transaction {
            updates.forEach { updatePartial(it) }
        }
    }

    override suspend fun updateAllFlags(flags: Long?) {
        database.categoriesQueries.updateAllFlags(flags)
    }

    override suspend fun delete(categoryId: Long) {
        database.categoriesQueries.delete(categoryId = categoryId)
    }
}
