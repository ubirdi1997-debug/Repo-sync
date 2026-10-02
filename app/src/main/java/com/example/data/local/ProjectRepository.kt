package com.example.data.local

import kotlinx.coroutines.flow.Flow

class ProjectRepository(private val dao: VibeProjectDao) {

    val allProjects: Flow<List<VibeProjectEntity>> = dao.getAllProjects()

    fun search(query: String): Flow<List<VibeProjectEntity>> = dao.searchProjects(query)

    fun filterByMood(mood: String): Flow<List<VibeProjectEntity>> = dao.getProjectsByMood(mood)

    suspend fun getProjectById(id: Long): VibeProjectEntity? = dao.getProjectById(id)

    suspend fun insertProject(project: VibeProjectEntity): Long = dao.insertProject(project)

    suspend fun updateProject(project: VibeProjectEntity) = dao.updateProject(project)

    suspend fun updateSyncStatus(id: Long, isSynced: Boolean, cloudId: String?) =
        dao.updateSyncStatus(id, isSynced, cloudId)

    suspend fun deleteProject(id: Long) = dao.deleteProjectById(id)

    suspend fun seedInitialSampleIfEmpty(existingCount: Int) {
        if (existingCount == 0) {
            val sample = VibeProjectEntity(
                title = "Adaptive Fluid Heartbeat",
                moodName = "Zen Calm",
                moodHex = "#38BDF8",
                behaviorPrompt = "When the user rests, smoothly pulse a luminous aura. On tap, ripple out an acoustic wave and save telemetry.",
                architecturePattern = "MVI + StateFlow + Canvas Wave",
                logicSummary = "Defines HeartbeatState (Resting, Pulsing, Dispatched), emits pulse events on 1000ms tick, coordinates with local telemetry DAO.",
                generatedCode = """
                    // Vibe Generated Logic - Kotlin + Compose
                    sealed interface HeartbeatState {
                        object Resting : HeartbeatState
                        data class Pulsing(val intensity: Float) : HeartbeatState
                        data class Dispatched(val eventId: String) : HeartbeatState
                    }
                    
                    @Composable
                    fun HeartbeatPulse(state: HeartbeatState, onPulseTap: () -> Unit) {
                        val infiniteTransition = rememberInfiniteTransition(label = "pulse")
                        val scale by infiniteTransition.animateFloat(
                            initialValue = 0.95f,
                            targetValue = 1.05f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(1200, easing = FastOutSlowInEasing),
                                repeatMode = RepeatMode.Reverse
                            ),
                            label = "scale"
                        )
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .scale(scale)
                                .clickable { onPulseTap() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("✦ Heartbeat", style = MaterialTheme.typography.titleMedium)
                        }
                    }
                """.trimIndent(),
                language = "Kotlin / Compose",
                timestamp = System.currentTimeMillis() - 3600_000,
                isSynced = true,
                cloudId = "vibe_cloud_001"
            )
            dao.insertProject(sample)
        }
    }
}
