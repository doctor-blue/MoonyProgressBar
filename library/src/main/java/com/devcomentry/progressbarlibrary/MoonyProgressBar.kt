package com.devcomentry.progressbarlibrary

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.cardview.widget.CardView

/**
 * Create by Nguyen Van Tan (Doctor-blue) 5/1/2021
 */
class MoonyProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.MoonyProgressBarStyle
) : CardView(context, attrs, defStyleAttr) {

    //Attributes
    private var _currentProgress: Float = 0f
        set(value) {
            field = value
            if (value >= 100f){
                _currentState = ProgressState.COMPLETED
                field = 100f
            }
        }

    private var _currentState = ProgressState.PREPARE

    @ColorInt
    var progressColor: Int = Color.MAGENTA
        set(@ColorInt value) {
            field = value
            paintProgress.color = value
            paintText.color = value
            invalidate()
        }

    @ColorInt
    var defaultColor: Int = Color.GRAY
        set(@ColorInt value) {
            field = value
            ptProgressNormal.color = value
            invalidate()
        }

    @ColorInt
    var completedColor: Int = Color.GREEN


    @ColorInt
    var failedColor: Int = Color.RED

    var inProgressMessage = "Downloading..."
    var completedMessage = "Completed!"
    var failedMessage = "Failed!"
    var pausedMessage = "Paused!"

    @Dimension
    var textSize = 48f
        set(@Dimension value) {
            field = value
            paintText.textSize = value
            invalidate()
        }

    var currentProgress: Float = _currentProgress
        set(value) {
            field = value
            if (value >= 100f)
                field = 100f

            _currentProgress = currentProgress
            invalidate()
        }
        get() = _currentProgress


    var currentState = _currentState
        set(value) {
            field = value
            _currentState = value
            invalidate()
        }
        get() = _currentState


    private val rect = RectF()
    private var textAlpha = 1
    private var alphaDuration = 400L


    //Paint
    private val paintProgress = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    private val ptProgressNormal = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    private val paintText = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        textSize = this@MoonyProgressBar.textSize
    }


    init {
        obtainStyledAttributes(attrs, defStyleAttr)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        radius = h / 2f
    }

    @SuppressLint("RestrictedApi")
    private fun obtainStyledAttributes(attrs: AttributeSet?, defStyleAttr: Int) {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.MoonyProgressBar,
            defStyleAttr,
            0
        )

        try {
            progressColor =
                typedArray.getColor(R.styleable.MoonyProgressBar_progressColor, progressColor)

            completedColor =
                typedArray.getColor(R.styleable.MoonyProgressBar_completedColor, completedColor)

            failedColor =
                typedArray.getColor(R.styleable.MoonyProgressBar_failedColor, failedColor)

            defaultColor =
                typedArray.getColor(R.styleable.MoonyProgressBar_defaultColor, defaultColor)

            textSize = typedArray.getDimension(R.styleable.MoonyProgressBar_textSize, textSize)

            inProgressMessage = typedArray.getString(R.styleable.MoonyProgressBar_inProgressMess)
                ?: inProgressMessage

            failedMessage = typedArray.getString(R.styleable.MoonyProgressBar_failedMess)
                ?: failedMessage

            completedMessage = typedArray.getString(R.styleable.MoonyProgressBar_completedMess)
                ?: completedMessage

            pausedMessage = typedArray.getString(R.styleable.MoonyProgressBar_pausedMess)
                ?: pausedMessage


        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            typedArray.recycle()
        }
    }

    fun setState(state: ProgressState, progress: Float) {
        _currentState = state
        _currentProgress = progress
        if (state == ProgressState.IN_PROGRESS) {
            animateAlpha(175, 255)
        }
        invalidate()
    }

    private fun animateAlpha(from: Int, to: Int) =
        ValueAnimator.ofInt(from, to).apply {
            duration = alphaDuration
            addUpdateListener {
                val value = it.animatedValue as Int
                textAlpha = value
                invalidate()
            }
            start()
        }


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)


        val message = when (_currentState) {
            ProgressState.IN_PROGRESS -> {
                paintText.color = progressColor
                paintProgress.color = progressColor
                inProgressMessage
            }
            ProgressState.PAUSED -> {
                pausedMessage
            }
            ProgressState.FAILED -> {
                paintText.color = failedColor
                paintProgress.color = failedColor
                failedMessage
            }
            ProgressState.COMPLETED -> {
                paintText.color = completedColor
                paintProgress.color = completedColor
                textAlpha = 255
                completedMessage
            }
            else -> {
                ""
            }
        }

        rect.left = radius / 2
        rect.right = width - radius / 2
        rect.bottom = height - height / 2f + height / 25f
        rect.top = height / 2f - height / 25f

        val progressWidth = rect.width()
        val progressHeight = rect.height()

        canvas.drawRoundRect(rect, progressHeight / 2f, progressHeight / 2f, ptProgressNormal)

        rect.right = currentProgress * progressWidth / 100 + rect.left
        canvas.drawRoundRect(rect, progressHeight / 2f, progressHeight / 2f, paintProgress)

        paintText.alpha = textAlpha

        drawTextCentered(
            message,
            width / 2f,
            height - (height - rect.bottom) / 2f,
            paintText,
            canvas
        )

    }

    private fun drawTextCentered(text: String, x: Float, y: Float, paint: Paint, canvas: Canvas) {
        val xPos = x - (paint.measureText(text) / 2).toInt()
        val yPos = (y - (paint.descent() + paint.ascent()) / 2)
        canvas.drawText(text, xPos, yPos, paint)
    }
}