package az.zero.azpaintapp.data.models

import android.graphics.Paint
import az.zero.azpaintapp.utils.TypeOfView

data class CustomView(
    val left: Float = -1f,
    val top: Float = -1f,
    val right: Float = -1f,
    val down: Float = -1f,
    val shape: TypeOfView = TypeOfView.FreeDrawing,
    val paint: Paint = Paint()
)
