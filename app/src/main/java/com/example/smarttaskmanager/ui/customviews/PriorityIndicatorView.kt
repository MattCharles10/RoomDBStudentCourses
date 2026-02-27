package com.example.smarttaskmanager.ui.customviews

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.example.smarttaskmanager.data.Priority
import kotlin.math.min

class PriorityIndicatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var priority: Priority = Priority.MEDIUM
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 40f
        textAlign = Paint.Align.CENTER
    }

    private val priorityColors = mapOf(
        Priority.LOW to Color.rgb(76, 175, 80),    // Green
        Priority.MEDIUM to Color.rgb(255, 193, 7),  // Amber
        Priority.HIGH to Color.rgb(255, 87, 34),    // Deep Orange
        Priority.URGENT to Color.rgb(244, 67, 54)   // Red
    )

    fun setPriority(priority: Priority) {
        this.priority = priority
        invalidate()
        requestLayout()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val size = min(width, height)
        val radius = size / 2f

        // Draw circle background
        paint.color = priorityColors[priority] ?: Color.GRAY
        canvas.drawCircle(width / 2f, height / 2f, radius, paint)

        // Draw text
        val text = priority.name.first().toString()
        val y = height / 2f - (textPaint.descent() + textPaint.ascent()) / 2
        canvas.drawText(text, width / 2f, y, textPaint)
    }
}