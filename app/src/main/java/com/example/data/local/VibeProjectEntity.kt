package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vibe_projects")
data class VibeProjectEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val moodName: String,
    val moodHex: String,
    val behaviorPrompt: String,
    val architecturePattern: String,
    val logicSummary: String,
    val generatedCode: String,
    val language: String = "Kotlin",
    val timestamp: Long = System.currentTimeMillis(),
    val isSynced: Boolean = false,
    val cloudId: String? = null
)
