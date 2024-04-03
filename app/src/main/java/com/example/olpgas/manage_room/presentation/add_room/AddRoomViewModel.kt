package com.example.olpgas.manage_room.presentation.add_room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.olpgas.browse_rooms.data.remote.model.RoomBookingStatus
import com.example.olpgas.core.util.Resource
import com.example.olpgas.core.util.domain.states.TextFieldState
import com.example.olpgas.manage_room.domain.use_case.PostRoomUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddRoomViewModel @Inject constructor(
    private val postRoomUseCase: PostRoomUseCase
) : ViewModel() {

    private val _roomNameState: MutableLiveData<TextFieldState> = MutableLiveData(TextFieldState())
    val roomNameState: LiveData<TextFieldState> = _roomNameState

    private val _roomAreaState: MutableLiveData<TextFieldState> = MutableLiveData(TextFieldState())
    val roomAreaState: LiveData<TextFieldState> = _roomAreaState

    private val _shareableState: MutableLiveData<TextFieldState> = MutableLiveData(TextFieldState())
    val shareableState: LiveData<TextFieldState> = _shareableState

    private val _roomTypeState: MutableLiveData<String> = MutableLiveData()
    val roomTypeState: LiveData<String> = _roomTypeState

    private val _rentAmountState: MutableLiveData<TextFieldState> = MutableLiveData(TextFieldState())
    val rentAmountState: LiveData<TextFieldState> = _rentAmountState

    private val _depositState: MutableLiveData<TextFieldState> = MutableLiveData(TextFieldState())
    val depositState: LiveData<TextFieldState> = _depositState

    private val _descriptionState: MutableLiveData<TextFieldState> = MutableLiveData(TextFieldState())
    val descriptionState: LiveData<TextFieldState> = _descriptionState

    private val _suitableForState: MutableLiveData<MutableList<String>> = MutableLiveData<MutableList<String>>()
    val suitableForState: LiveData<MutableList<String>> = _suitableForState

    private val _featuresState:MutableLiveData<MutableList<String>> = MutableLiveData<MutableList<String>>()
    val featuresState: LiveData<MutableList<String>> = _featuresState

    private val _ratingsState: MutableLiveData<TextFieldState> = MutableLiveData(TextFieldState())
    val ratingsState: LiveData<TextFieldState> = _ratingsState

    private val _roomNumberState: MutableLiveData<TextFieldState> = MutableLiveData(TextFieldState())
    val roomNumberState: LiveData<TextFieldState> = _roomNumberState

    private val _streetNumberState: MutableLiveData<TextFieldState> = MutableLiveData(TextFieldState())
    val streetNumberState: LiveData<TextFieldState> = _streetNumberState

    private val _landMarkState: MutableLiveData<TextFieldState> = MutableLiveData(TextFieldState())
    val landMarkState: LiveData<TextFieldState> = _landMarkState

    private val _cityState: MutableLiveData<String> = MutableLiveData()
    val cityState: LiveData<String> = _cityState

    private val _stateState: MutableLiveData<String> = MutableLiveData()
    val stateState: LiveData<String> = _stateState

    private val _imagesState = MutableLiveData<MutableList<ByteArray>>()
    val imagesState: LiveData<MutableList<ByteArray>> = _imagesState

    private val _postRoomState = MutableLiveData<PostRoomState>()
    val postRoomState: LiveData<PostRoomState> = _postRoomState

    fun onEvent(event: AddRoomEvent) {
        when(event) {
            is AddRoomEvent.AddedAmenity -> {
                _featuresState.value?.add(event.amenity)
            }
            is AddRoomEvent.AddedImage -> {
                _imagesState.value?.add(event.image)
            }
            is AddRoomEvent.AddedSuitableFor -> {
                _suitableForState.value?.add(event.suitableFor)
            }
            is AddRoomEvent.EnteredDeposit -> {
                _depositState.value = depositState.value?.copy(
                    text = event.deposit
                )
            }
            is AddRoomEvent.EnteredDescription -> {
                _descriptionState.value = descriptionState.value?.copy(
                    text = event.description
                )
            }
            is AddRoomEvent.EnteredLandMark -> {
                _landMarkState.value = landMarkState.value?.copy(
                    text = event.landMark
                )
            }
            is AddRoomEvent.EnteredRentAmount -> {
                _rentAmountState.value = rentAmountState.value?.copy(
                    text = event.rentAmount
                )
            }
            is AddRoomEvent.EnteredRoomArea -> {
                _roomAreaState.value = roomAreaState.value?.copy(
                    text = event.roomArea
                )
            }
            is AddRoomEvent.EnteredRoomName -> {
                _roomNameState.value = roomNameState.value?.copy(
                    text = event.roomName
                )
            }
            is AddRoomEvent.EnteredRoomNumber -> {
                _roomNumberState.value = roomNumberState.value?.copy(
                    text = event.roomNumber
                )
            }
            is AddRoomEvent.EnteredShareableBy -> {
                _shareableState.value = shareableState.value?.copy(
                    text = event.shareableBy
                )
            }
            is AddRoomEvent.EnteredStreetNumber -> {
                _streetNumberState.value = streetNumberState.value?.copy(
                    text = event.streetNumber
                )
            }
            is AddRoomEvent.SelectedCity -> {
                _cityState.value = event.city
            }
            is AddRoomEvent.SelectedRoomType -> {
                _roomTypeState.value = event.roomType
            }
            is AddRoomEvent.SelectedState -> {
                _stateState.value = event.state
            }
            AddRoomEvent.OnSubmit -> {

            }
        }
    }

    fun postRoom() {
        viewModelScope.launch {
            _roomNameState.value = roomNameState.value?.copy(
                error = null
            )
            _roomAreaState.value = roomAreaState.value?.copy(
                error = null
            )
            _shareableState.value = shareableState.value?.copy(
                error = null
            )
            _rentAmountState.value = rentAmountState.value?.copy(
                error = null
            )
            _depositState.value = depositState.value?.copy(
                error = null
            )
            _descriptionState.value = descriptionState.value?.copy(
                error = null
            )
            _roomNumberState.value = roomNumberState.value?.copy(
                error = null
            )
            _streetNumberState.value = streetNumberState.value?.copy(
                error = null
            )
            _landMarkState.value = landMarkState.value?.copy(
                error = null
            )

            val result = postRoomUseCase(
                roomAreaState.value?.text!!.toInt(),
                shareableState.value?.text!!.toInt(),
                roomTypeState.value!!,
                rentAmountState.value?.text!!.toInt(),
                depositState.value?.text!!.toInt(),
                descriptionState.value?.text!!,
                suitableForState.value!!,
                featuresState.value!!,
                0f,
                roomNameState.value?.text!!,
                roomNumberState.value?.text!!,
                streetNumberState.value?.text!!,
                landMarkState.value?.text!!,
                cityState.value!!,
                stateState.value!!,
                RoomBookingStatus.NotBooked.value,
                imagesState.value!!
            )

            when(result.result) {
                is Resource.Error -> {
                    _postRoomState.value = PostRoomState.Error(result.result.uiText.toString())
                }
                is Resource.Success -> {
                    _postRoomState.value = PostRoomState.Success
                }
                null -> Unit
            }
        }
    }
}