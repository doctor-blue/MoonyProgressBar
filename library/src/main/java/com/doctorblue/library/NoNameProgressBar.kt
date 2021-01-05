package com.doctorblue.library

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import com.google.android.material.card.MaterialCardView

class NoNameProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.NoNameProgressBarStyle
) : MaterialCardView(context, attrs, defStyleAttr) {


    //Paint
    private val paintProgress = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.YELLOW
    }
}