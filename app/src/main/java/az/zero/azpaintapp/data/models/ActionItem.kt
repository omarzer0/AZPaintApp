package az.zero.azpaintapp.data.models

data class ActionItem(
    val image: Int,
    val action: ActionType,
    val selected: Boolean = false
)


enum class ActionType {
    PEN,
    ARROW,
    RECTANGLE,
    OVAL,
    Palette
}