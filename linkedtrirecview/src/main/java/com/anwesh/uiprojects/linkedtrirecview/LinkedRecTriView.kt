package com.anwesh.uiprojects.linkedtrirecview

/**
 * Created by anweshmishra on 11/07/18.
 */

import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Path
import android.view.View
import android.view.MotionEvent
import android.content.Context

val NODES : Int = 5

class LinkedRecTriView(ctx : Context) : View(ctx) {

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    
    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
}