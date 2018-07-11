package com.anwesh.uiprojects.linkedtrirecview

/**
 * Created by anweshmishra on 11/07/18.
 */

import android.app.Activity
import android.graphics.Paint
import android.graphics.Canvas
import android.view.View
import android.view.MotionEvent
import android.content.Context
import android.graphics.Color

val NODES : Int = 5

class LinkedRecTriView(ctx : Context) : View(ctx) {

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val renderer : Renderer = Renderer(this)

    override fun onDraw(canvas : Canvas) {
        renderer.render(canvas, paint)
    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }

    data class LRTState(var scale : Float = 0f, var prevScale : Float = 0f, var dir : Float = 0f) {

        fun update(stopcb : (Float) -> Unit) {
            scale += 0.1f * dir
            if (Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                stopcb(prevScale)
            }
        }

        fun startUpdating(startcb : () -> Unit) {
            if (dir == 0f) {
                dir = 1 - 2 * prevScale
                startcb()
            }
        }
    }

    data class LRTAnimator(var view : View, var animated : Boolean = false) {

        fun animate(cb : () -> Unit) {
            if (animated) {
                cb()
                try {
                    Thread.sleep(50)
                    view.invalidate()
                } catch(ex : Exception) {

                }
            }
        }

        fun start() {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }

        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }

    data class LRTNode(var i : Int = 0, val state : LRTState = LRTState()) {

        private var next : LRTNode? = null

        private var prev : LRTNode? = null

        init {
            addNeighbor()
        }

        fun addNeighbor() {
            if (i < NODES - 1) {
                next = LRTNode(i + 1)
                next?.prev = this
            }
        }

        fun draw(canvas : Canvas, paint : Paint) {
            val w : Float = canvas.width.toFloat()
            val h : Float = canvas.height.toFloat()
            val gap : Float = w / NODES
            val sc1 : Float = Math.min(0.5f, state.scale) * 2
            val sc2 : Float = Math.min(0.5f, Math.max(0f, state.scale - 0.5f)) * 2
            canvas.save()
            canvas.translate(i * gap, h/2)
            for (i in 0..1) {
                canvas.drawLine(0f,0f, gap/2 * sc1, -gap/2 * sc2, paint)
                canvas.drawLine(0f,0f, gap/2 * sc1, -gap/2 * sc2, paint)
            }
            canvas.restore()
        }

        fun update(stopcb : (Int, Float) -> Unit) {
            state.update {
                stopcb(i, it)
            }
        }

        fun startUpdating(startcb : () -> Unit) {
            state.startUpdating(startcb)
        }

        fun getNext(dir : Int, cb : () -> Unit) : LRTNode {
            var curr : LRTNode? = prev
            if (dir == 1) {
                curr = next
            }
            if (curr != null) {
                return curr
            }
            cb()
            return this
        }
    }

    data class  LinkedRecTri(var i : Int) {

        private var curr : LRTNode = LRTNode(0)

        private var dir : Int = 1

        fun draw(canvas : Canvas, paint : Paint) {
            paint.color = Color.parseColor("#4CAF50")
            paint.strokeWidth = Math.min(canvas.width, canvas.height).toFloat() / 60
            paint.strokeCap = Paint.Cap.ROUND
            curr.draw(canvas, paint)
        }

        fun update(stopcb : (Int, Float) -> Unit) {
            curr.update {i, scale ->
                curr = curr.getNext(dir) {
                    dir *= -1
                }
                stopcb(i, scale)
            }
        }

        fun startUpdating(startcb : () -> Unit) {
            curr.startUpdating(startcb)
        }
    }

    data class Renderer(var view : LinkedRecTriView) {

        private val animator : LRTAnimator = LRTAnimator(view)

        private val lrt : LinkedRecTri = LinkedRecTri(0)

        fun render(canvas : Canvas, paint : Paint) {
            canvas.drawColor(Color.parseColor("#212121"))
            lrt.draw(canvas, paint)
            animator.animate {
                lrt.update {i,scale ->
                    animator.stop()
                }
            }
        }

        fun handleTap() {
            lrt.startUpdating {
                animator.start()
            }
        }
    }

    companion object {
        fun create(activity : Activity) : LinkedRecTriView {
            val view : LinkedRecTriView = LinkedRecTriView(activity)
            activity.setContentView(view)
            return view
        }
    }
}