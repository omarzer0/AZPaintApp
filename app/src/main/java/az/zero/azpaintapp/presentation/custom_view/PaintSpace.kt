package az.zero.azpaintapp.presentation.custom_view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import az.zero.azpaintapp.data.models.CustomView
import az.zero.azpaintapp.utils.TypeOfView
import kotlin.math.atan2


class PaintSpace : View {
    constructor(context: Context) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)


    private val paint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 10f
        style = Paint.Style.STROKE
    }

    private var customView: CustomView? = null
    private var customDrawingList = mutableListOf<CustomView>()
    private var typeOfView: TypeOfView = TypeOfView.FreeDrawing
    private var path: Path? = null
    private val pathList = mutableListOf<Pair<Path, Paint>>()

    fun setPaintColor(color: Int) {
        paint.color = color
    }

    fun changeViewType(type: TypeOfView) {
        typeOfView = type
    }


    override fun onDraw(canvas: Canvas?) {

        drawMyShape(customView, canvas)
        customDrawingList.forEach {
            drawMyShape(it, canvas)
        }

        pathList.forEach {
            canvas?.apply {
                drawPath(it.first, it.second)
            }
        }

    }

    private fun drawMyShape(customView: CustomView?, canvas: Canvas?) {
        customView?.let {
            canvas?.apply {
                when (customView.shape) {
                    TypeOfView.Arrow -> {
                        drawArrow(it.left, it.top, it.right, it.down, it.paint, canvas)
                    }
                    TypeOfView.Oval -> {
                        drawOval(it.left, it.top, it.right, it.down, it.paint)
                    }
                    TypeOfView.Rectangle -> {
                        drawRect(it.left, it.top, it.right, it.down, it.paint)
                    }
                    TypeOfView.FreeDrawing -> {
                        drawPath(Path(), it.paint)
                    }
                }
            }
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) return super.onTouchEvent(event)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                createNewView(event.x, event.y)
            }

            MotionEvent.ACTION_MOVE -> {
                updateCoordinatesAndRedraw(event.x, event.y)
            }

            MotionEvent.ACTION_UP -> {
                addViewAndClear()
            }

            else -> {
                super.onTouchEvent(event)
            }
        }
        return true
    }

    /**
     * Handle the creation of a new view to be drawn be canvas
     *
     * @param left is the initial x axis coordinate
     * @param top is the initial y axis coordinate
     * @author Omar Adel
     * */
    private fun createNewView(left: Float, top: Float) {
        when (typeOfView) {
            TypeOfView.FreeDrawing -> {
                path = Path()
                path?.moveTo(left, top)
            }
            else -> {
                customView = CustomView(left = left, top = top)
            }
        }
    }

    /**
     * Handle user's drag to specific coordinates and call
     * invalidate() to redraw the PaintSpace
     *
     * @param right is the last current x axis coordinate
     * @param down is the last current y axis coordinate
     * @author Omar Adel
     * */
    private fun updateCoordinatesAndRedraw(right: Float, down: Float) {
        when (typeOfView) {
            TypeOfView.FreeDrawing -> {
                path?.apply {
                    lineTo(right, down)
                    pathList.add(Pair(this, Paint(paint)))
                }
            }
            else -> {
                customView = customView?.copy(
                    right = right,
                    down = down,
                    shape = typeOfView,
                    paint = Paint(paint)
                )
            }
        }
        invalidate()
    }

    /**
     * Add the current view to customDrawingList and
     * set the custom view to null to free it for another
     * shape to be drawn
     *
     * @author Omar Adel
     * */
    private fun addViewAndClear() {
        customView?.let { customDrawingList.add(it) }
        path = null
        customView = null
    }

    /**
     * Handle the drawing of an arrow by four coordinates
     * 1: Draw a line from the initial x and y to
     * the current x and y coordinates
     *
     * 2: Save the current state of the board to retrieve it
     * after any (rotation) operation
     *
     * 3: Calculate deltaX which is the difference between
     * current x and initial x coordinate and deltaY for
     * current y and initial y as well
     *
     * 4: Calculate the theta which is the inverse tangent of the
     * sloop by arc tangent of deltaY and deltaX
     * it is better to use atan2 rather than atan
     * then convert the result to degree
     *
     * 5: Rotate the drawing canvas by this degree and current x and y
     *
     * 6: Draw two lines with the same start x coordinate and angle
     * but add a fraction (ex: 40) to one and subtract it from the other
     *
     * 7: Restore the state of the canvas for future drawings
     *
     * @param left is the initial x axis coordinate
     * @param top is the initial y axis coordinate
     * @param right is the last current x axis coordinate
     * @param down is the last current y axis coordinate
     * @param paint is the current paint configuration
     * @param canvas is the current drawing canvas
     *
     * @author Omar Adel
     * */
    private fun drawArrow(
        left: Float,
        top: Float,
        right: Float,
        down: Float,
        paint: Paint,
        canvas: Canvas?
    ) {
        canvas?.apply {
            drawLine(left, top, right, down, paint)
            save()
            val deltaX = (right - left).toDouble()
            val deltaY = (down - top).toDouble()
            val theta = atan2(deltaY, deltaX)
            val degree = Math.toDegrees(theta).toFloat()
            rotate(degree, right, down)
            drawLine(right - 40, down + 40, right, down, paint)
            drawLine(right - 40, down - 40, right, down, paint)
            restore()
        }
    }
}