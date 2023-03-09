package com.example.rewardapp.features.addeditReward

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rewardapp.database.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val rewardDao: RewardDao
) : ViewModel(), AddEditRewardScreenActions {

    private val rewardNameInputIsErrorLiveData = savedStateHandle.getLiveData<Boolean>("rewardNameInputIsError", false)
    val rewardNameInputIsError = rewardNameInputIsErrorLiveData

    private val _DefaultRewardIconKeyLivedata = savedStateHandle.getLiveData<IconKeysEn>("defaultRewardIconkey", defaulticon)
    val DefaultRewardIconKey = _DefaultRewardIconKeyLivedata

    private val rewardId = savedStateHandle.get<Long>(ARG_REWARD_ID)
    val isEditMode = rewardId != NO_REWARD_ID.toLong()

    private val rewardNameInputLivedata = savedStateHandle.getLiveData<String>("rewardLiveData")
    val rewardNameInput: LiveData<String> = rewardNameInputLivedata

    private val ChancesInPercentLiveData = savedStateHandle.getLiveData<Int>(" ChancesInPercentLiveData", 10)
    val ChancesInPercent: LiveData<Int> = ChancesInPercentLiveData

    private val eventChannel = Channel<AddEditRewardEvent>()
    val events = eventChannel.receiveAsFlow()

    private val _showRewardIconSelectionDialogRewardEvent = savedStateHandle.getLiveData<Boolean>("showRewardIconDialogLiveData", false)
    val showRewardIconSelectionDialogRewardEvent: LiveData<Boolean> = _showRewardIconSelectionDialogRewardEvent

    sealed class AddEditRewardEvent {
        object RewardCreated : AddEditRewardEvent()
    }

    override fun onRewardIconSelected(reward: IconKeysEn) {
        _DefaultRewardIconKeyLivedata.value = reward
    }

    override fun onRewardIconButtonClicked() {
        _showRewardIconSelectionDialogRewardEvent.value = true
    }
    override fun cancelRewardIconDialouge() {
        _showRewardIconSelectionDialogRewardEvent.value = false
    }

    override fun onRewardInputChanged(input: String) {
        rewardNameInputLivedata.value = input
    }

    override fun onRewardInPercentChanged(input: Int) {
        ChancesInPercentLiveData.value = input
    }

    override fun onSavedClicked() {
        val rewardInput = rewardNameInput.value
        val chancesInPercentInput = ChancesInPercent.value
        val rewardIconKey = DefaultRewardIconKey.value
        rewardNameInputIsErrorLiveData.value = false

        viewModelScope.launch {
            if (rewardInput != null &&
                rewardIconKey != null &&
                chancesInPercentInput != null &&
                rewardInput.isNotBlank()
            ) {
                if (rewardId != null) {
                    // updateReward()
                } else {
                    createReward(RewardEntity(rewardIconKey.name, rewardInput, chancesInPercentInput))
                }
            } else {
                if (rewardInput.isNullOrBlank()) {
                    rewardNameInputIsErrorLiveData.value = true
                }
                // show error
            }
        }
    }

    suspend fun insert(reward: RewardEntity) {
        rewardDao.insertReward(reward)
    }

    private suspend fun createReward(reward: RewardEntity) {
        rewardDao.insertReward(reward)
        eventChannel.send(AddEditRewardEvent.RewardCreated)
    }
}

const val ARG_REWARD_ID = "rewardId"
const val NO_REWARD_ID = -1
