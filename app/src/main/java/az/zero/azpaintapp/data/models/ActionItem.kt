package az.zero.azpaintapp.data.models

data class ActionItem(
    val image: Int,
    val action: ActionType
)


enum class ActionType {
    PEN,
    ARROW,
    RECTANGLE,
    OVAL,
    Palette
}