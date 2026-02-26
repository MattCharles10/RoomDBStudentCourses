package com.example.smarttaskmanager.ui.customviews

import android.content.Context
import android.graphics.*
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import com.example.smarttaskmanager.data.Priority
import kotlin.math.min


class PriorityIndicatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var priority: Priority = Priority.MEDIUM

    // Handle view state restoration (for rotation/process death)
    private var savedPriority: String? = null

    init {
        // Restore state if available
        if (savedPriority != null) {
            priority = Priority.valueOf(savedPriority!!)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw priority indicator circle
        paint.color = when (priority) {
            Priority.HIGH -> Color.RED
            Priority.MEDIUM -> Color.YELLOW
            Priority.LOW -> Color.GREEN
        }

        val radius = min(width, height) / 2f
        canvas.drawCircle(width / 2f, height / 2f, radius, paint)
    }

    fun setPriority(newPriority: Priority) {
        priority = newPriority
        invalidate() // Request redraw
    }

    // Save view state (handles rotation)
    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val savedState = SavedState(superState)
        savedState.priority = priority.name
        return savedState
    }

    // Restore view state
    override fun onRestoreInstanceState(state: Parcelable?) {
        val savedState = state as SavedState
        super.onRestoreInstanceState(savedState.superState)
        priority = Priority.valueOf(savedState.priority ?: Priority.MEDIUM.name)
        savedPriority = savedState.priority
    }

    // Custom SavedState class
    internal class SavedState : View.BaseSavedState {
        var priority: String? = null

        constructor(superState: Parcelable?) : super(superState)

        constructor(parcel: Parcel) : super(parcel) {
            priority = parcel.readString()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeString(priority)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(parcel: Parcel): SavedState {
                    return SavedState(parcel)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }
}