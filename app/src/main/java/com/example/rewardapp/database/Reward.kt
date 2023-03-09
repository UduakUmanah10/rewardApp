package com.example.rewardapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rewards")
class RewardEntity(
    val IconKey: String,
    val title: String,
    val ChancesInPercent: Int,
    val isUnLocked: Boolean = false,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
)
