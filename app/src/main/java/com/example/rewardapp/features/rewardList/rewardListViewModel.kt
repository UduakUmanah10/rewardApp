package com.example.rewardapp.features.rewardList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.rewardapp.database.RewardDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class rewardListViewModel @Inject constructor(
    private val rewardDao: RewardDao

) : ViewModel() {

    val rewards = rewardDao.getAllRewards().asLiveData()


}
