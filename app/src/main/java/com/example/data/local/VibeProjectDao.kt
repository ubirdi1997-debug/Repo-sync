package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface VibeProjectDao {

    @Query("SELECT * FROM vibe_projects ORDER BY timestamp DESC")
    fun getAllProjects(): Flow<List<VibeProjectEntity>>

    @Query("SELECT * FROM vibe_projects WHERE id = :id LIMIT 1")
    suspend fun getProjectById(id: Long): VibeProjectEntity?

    @Query("SELECT * FROM vibe_projects WHERE moodName = :mood ORDER BY timestamp DESC")
    fun getProjectsByMood(mood: String): Flow<List<VibeProjectEntity>>

    @Query("SELECT * FROM vibe_projects WHERE title LIKE '%' || :query || '%' OR behaviorPrompt LIKE '%' || :query || '%' ORDER BY timestamp DESC")
    fun searchProjects(query: String): Flow<List<VibeProjectEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: VibeProjectEntity): Long

    @Update
    suspend fun updateProject(project: VibeProjectEntity)

    @Query("UPDATE vibe_projects SET isSynced = :isSynced, cloudId = :cloudId WHERE id = :id")
    suspend fun updateSyncStatus(id: Long, isSynced: Boolean, cloudId: String?)

    @Query("DELETE FROM vibe_projects WHERE id = :id")
    suspend fun deleteProjectById(id: Long)

    @Query("DELETE FROM vibe_projects")
    suspend fun clearAll()
}
