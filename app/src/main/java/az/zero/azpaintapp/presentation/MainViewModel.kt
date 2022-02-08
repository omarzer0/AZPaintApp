package az.zero.azpaintapp.presentation

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import az.zero.azpaintapp.R
import az.zero.azpaintapp.data.models.ActionItem
import az.zero.azpaintapp.data.models.ActionType
import az.zero.azpaintapp.data.models.ActionType.*
import az.zero.azpaintapp.utils.TypeOfView

class MainViewModel : ViewModel() {

    private val _state = MutableLiveData<MainState>()
    val state: LiveData<MainState> = _state

    fun preformAction(actionItem: ActionItem) {
        when (actionItem.action) {
            PEN -> _state.value = _state.value?.copy(
                typeOfView = TypeOfView.FreeDrawing
            )
            ARROW -> _state.value = _state.value?.copy(
                typeOfView = TypeOfView.Arrow
            )
            RECTANGLE -> _state.value = _state.value?.copy(
                typeOfView = TypeOfView.Rectangle
            )
            OVAL -> _state.value = _state.value?.copy(
                typeOfView = TypeOfView.Oval
            )
            Palette -> {
                val isVisible = _state.value?.paletteIsVisible ?: false
                _state.value = _state.value?.copy(
                    paletteIsVisible = !isVisible
                )
            }
        }

    }

    fun colorChanged(color: Int) {
        _state.value = _state.value?.copy(
            selectedColor = color,
            paletteIsVisible = false
        )
    }

    init {
        _state.value = MainState()
    }
}

data class MainState(
    val actionList: MutableList<ActionItem> = mutableListOf<ActionItem>().apply {
        add(ActionItem(R.drawable.ic_pen, PEN))
        add(ActionItem(R.drawable.ic_arrow, ARROW))
        add(ActionItem(R.drawable.ic_rectangle, RECTANGLE))
        add(ActionItem(R.drawable.ic_eclipes, OVAL))
        add(ActionItem(R.drawable.ic_palette, Palette))
    },

    val selectedAction: ActionType = PEN,
    val selectedColor: Int = Color.BLACK,
    val typeOfView: TypeOfView = TypeOfView.FreeDrawing,
    val paletteIsVisible: Boolean = false
)