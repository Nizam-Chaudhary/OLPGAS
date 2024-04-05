package com.example.olpgas.manage_room.presentation.post_room

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.olpgas.browse_rooms.data.remote.model.RoomBookingStatus
import com.example.olpgas.browse_rooms.domain.use_case.RefreshLocalCacheUseCase
import com.example.olpgas.core.util.ConnectivityObserver
import com.example.olpgas.core.util.Resource
import com.example.olpgas.core.util.domain.states.TextFieldState
import com.example.olpgas.manage_room.domain.use_case.PostRoomUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.olpgas.core.util.Error as Error

@HiltViewModel
class PostRoomViewModel @Inject constructor(
    private val postRoomUseCase: PostRoomUseCase,
    private val refreshLocalCacheUseCase: RefreshLocalCacheUseCase,
    connectivityObserver: ConnectivityObserver
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

    private val _suitableForState: MutableLiveData<MutableList<ChipField>> = MutableLiveData<MutableList<ChipField>>(mutableListOf())
    val suitableForState: LiveData<MutableList<ChipField>> = _suitableForState

    private val _featuresState:MutableLiveData<MutableList<ChipField>> = MutableLiveData<MutableList<ChipField>>(mutableListOf())
    val featuresState: LiveData<MutableList<ChipField>> = _featuresState

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

    private val _imagesState = MutableLiveData<MutableList<ByteArray>>(mutableListOf())
    val imagesState: LiveData<MutableList<ByteArray>> = _imagesState

    private val _postRoomState = MutableLiveData<PostRoomState>()
    val postRoomState: LiveData<PostRoomState> = _postRoomState

    private val _stateErrorState = MutableLiveData<Error?>()
    val stateErrorState: LiveData<Error?> = _stateErrorState

    private val _cityErrorState = MutableLiveData<Error?>()
    val cityErrorState: LiveData<Error?> = _cityErrorState

    private val _roomTypeErrorState = MutableLiveData<Error?>()
    val roomTypeErrorState: LiveData<Error?> = _roomTypeErrorState

    private val _featuresErrorState = MutableLiveData<Error?>()
    val featuresErrorState: LiveData<Error?> = _featuresErrorState

    private val _suitableForErrorState = MutableLiveData<Error?>()
    val suitableForErrorState: LiveData<Error?> = _suitableForErrorState

    private val _imagesErrorState = MutableLiveData<Error?>()
    val imagesErrorState: LiveData<Error?> = _imagesErrorState

    private var isInitialized = false

    val connectionStatus = connectivityObserver.observe().asLiveData()

    fun onEvent(event: PostRoomEvent) {
        when(event) {
            PostRoomEvent.OnCreate -> {
                if(!isInitialized) {
                    _featuresState.value?.add(ChipField("AC", false))
                    _featuresState.value?.add(ChipField("Washing Machine", false))
                    _featuresState.value?.add(ChipField("Wi-Fi", false))
                    _featuresState.value?.add(ChipField("Fans", false))
                    _featuresState.value?.add(ChipField("Geyser", false))
                    _featuresState.value?.add(ChipField("Furnished", false))

                    _suitableForState.value?.add(ChipField("Students", false))
                    _suitableForState.value?.add(ChipField("Professionals", false))
                    _suitableForState.value?.add(ChipField("Solo", false))
                    _suitableForState.value?.add(ChipField("Couples", false))
                    isInitialized = true
                }
            }
            is PostRoomEvent.FeatureCheckChange -> {
                _featuresState.value?.removeAt(event.index)
                _featuresState.value?.add(event.index, event.chipField)
                Log.d("Post Room", featuresState.value.toString())
            }
            is PostRoomEvent.SuitableForCheckChange -> {
                _suitableForState.value?.removeAt(event.index)
                _suitableForState.value?.add(event.index, event.chipField)
                Log.d("Post Room", suitableForState.value.toString())
            }
            is PostRoomEvent.AddedAmenity -> {
                _featuresState.value?.add(event.amenity)
            }
            is PostRoomEvent.AddedImage -> {
                _imagesState.value?.add(event.image)
            }
            is PostRoomEvent.AddedSuitableFor -> {
                _suitableForState.value?.add(event.suitableFor)
            }
            is PostRoomEvent.EnteredDeposit -> {
                _depositState.value = depositState.value?.copy(
                    text = event.deposit
                )
            }
            is PostRoomEvent.EnteredDescription -> {
                _descriptionState.value = descriptionState.value?.copy(
                    text = event.description
                )
            }
            is PostRoomEvent.EnteredLandMark -> {
                _landMarkState.value = landMarkState.value?.copy(
                    text = event.landMark
                )
            }
            is PostRoomEvent.EnteredRentAmount -> {
                _rentAmountState.value = rentAmountState.value?.copy(
                    text = event.rentAmount
                )
            }
            is PostRoomEvent.EnteredRoomArea -> {
                _roomAreaState.value = roomAreaState.value?.copy(
                    text = event.roomArea
                )
            }
            is PostRoomEvent.EnteredRoomName -> {
                _roomNameState.value = roomNameState.value?.copy(
                    text = event.roomName
                )
            }
            is PostRoomEvent.EnteredRoomNumber -> {
                _roomNumberState.value = roomNumberState.value?.copy(
                    text = event.roomNumber
                )
            }
            is PostRoomEvent.EnteredShareableBy -> {
                _shareableState.value = shareableState.value?.copy(
                    text = event.shareableBy
                )
            }
            is PostRoomEvent.EnteredStreetNumber -> {
                _streetNumberState.value = streetNumberState.value?.copy(
                    text = event.streetNumber
                )
            }
            is PostRoomEvent.SelectedCity -> {
                _cityState.value = event.city
            }
            is PostRoomEvent.SelectedRoomType -> {
                _roomTypeState.value = event.roomType
            }
            is PostRoomEvent.SelectedState -> {
                _stateState.value = event.state
            }
            PostRoomEvent.OnSubmit -> {
                postRoom()
            }
        }
    }

    private fun postRoom() {
        viewModelScope.launch {
            _postRoomState.value = PostRoomState.IsLoading
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
            _cityErrorState.value = null
            _featuresErrorState.value = null
            _imagesErrorState.value = null
            _stateErrorState.value = null
            _roomTypeErrorState.value = null
            _suitableForErrorState.value = null

            val result = postRoomUseCase(
                toInt(roomAreaState.value?.text!!),
                toInt(shareableState.value?.text!!),
                roomTypeState.value!!,
                toInt(rentAmountState.value?.text!!),
                toInt(depositState.value?.text!!),
                descriptionState.value?.text!!,
                getList(suitableForState.value!!),
                getList(featuresState.value!!),
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

            if(result.roomNameError != null) {
                _roomNameState.value = roomNameState.value?.copy(
                    error = result.roomNameError
                )
            }

            if(result.roomNumberError != null) {
                _roomNumberState.value = roomNumberState.value?.copy(
                    error = result.roomNumberError
                )
            }

            if(result.streetNumberError != null) {
                _streetNumberState.value = streetNumberState.value?.copy(
                    error = result.streetNumberError
                )
            }

            if(result.landMarkError != null) {
                _landMarkState.value = landMarkState.value?.copy(
                    error = result.landMarkError
                )
            }

            if(result.stateError != null) {
                _stateErrorState.value = result.stateError
            }

            if(result.cityError != null) {
                _cityErrorState.value = result.cityError
            }

            if(result.roomTypeError != null) {
                _roomTypeErrorState.value = result.roomTypeError
            }

            if(result.shareableError != null) {
                _shareableState.value = shareableState.value?.copy(
                    error = result.shareableError
                )
            }

            if(result.rentAmountError != null) {
                _rentAmountState.value = rentAmountState.value?.copy(
                    error = result.rentAmountError
                )
            }

            if(result.depositError != null) {
                _depositState.value = depositState.value?.copy(
                    error = result.depositError
                )
            }

            if(result.roomAreaError != null) {
                _roomAreaState.value = roomAreaState.value?.copy(
                    error = result.roomAreaError
                )
            }

            if(result.featuresError != null) {
                _featuresErrorState.value = result.featuresError
            }

            if(result.suitableForError != null) {
                _suitableForErrorState.value = result.suitableForError
            }

            if(result.descriptionError != null) {
                _descriptionState.value = descriptionState.value?.copy(
                    error = result.descriptionError
                )
            }

            if(result.imagesError != null) {
                _imagesErrorState.value = result.imagesError
            }

            when(result.result) {
                is Resource.Error -> {
                    _postRoomState.value = PostRoomState.Error(result.result.uiText.toString())

                }
                is Resource.Success -> {
                    refreshLocalCacheUseCase()
                    _postRoomState.value = PostRoomState.Success
                }
                null -> _postRoomState.value = PostRoomState.Nothing
            }
        }
    }

    private fun toInt(value: String) : Int {
        return try {
            if(value.isEmpty()) 0 else value.toInt()
        } catch(e: Exception) {
            0
        }
    }

    private fun getList(list: List<ChipField>) : List<String> {
        val listOfString = mutableListOf<String>()
        for(i in list) {
            if(i.isChecked)
                listOfString.add(i.text)
        }
        return listOfString
    }
}