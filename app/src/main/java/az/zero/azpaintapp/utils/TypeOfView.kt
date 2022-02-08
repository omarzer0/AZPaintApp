package az.zero.azpaintapp.utils

sealed class TypeOfView {
    object FreeDrawing : TypeOfView()
    object Rectangle : TypeOfView()
    object Oval : TypeOfView()
    object Arrow : TypeOfView()
}