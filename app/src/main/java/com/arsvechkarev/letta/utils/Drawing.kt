package com.arsvechkarev.letta.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.view.MotionEvent
import android.view.View
import com.arsvechkarev.letta.BuildConfig
import com.arsvechkarev.letta.core.assertThat
import kotlin.math.pow
import kotlin.math.roundToInt


val Int.f get() = this.toFloat()
val Float.i get() = this.toInt()

/**
 * Deconstructing motion event:
 *    val (x, y) = event
 */
operator fun MotionEvent.component1() = this.x

operator fun MotionEvent.component2() = this.x

fun MotionEvent.toPointF() = PointF(x, y)

fun Drawable.toBitmap(): Bitmap {
  val bitmap = Bitmap.createBitmap(
    intrinsicWidth,
    intrinsicHeight,
    Bitmap.Config.ARGB_8888
  )
  val canvas = Canvas(bitmap)
  setBounds(0, 0, canvas.width, canvas.height)
  draw(canvas)
  return bitmap
}

inline fun Canvas.execute(block: Canvas.() -> Unit) {
  save()
  apply(block)
  restore()
}

fun RectF.toRect(): Rect {
  return Rect(left.i, top.i, right.i, bottom.i)
}

// Only in debug
fun View.drawBounds(canvas: Canvas, color: Int = Color.RED) {
  if (!BuildConfig.DEBUG) {
    throw IllegalStateException("Nope")
  }
  canvas.drawRect(Rect(0, 0, width, height), Paint().apply {
    style = Paint.Style.STROKE
    this.color = color
    strokeWidth = 5f
  })
}

val String.c get() = Color.parseColor(this)

fun Int.isWhiterThan(limitChannel: Int): Boolean {
  val r = this and 0x00FF0000 shr 16
  val g = this and 0x0000FF00 shr 8
  val b = this and 0x000000FF
  return r > limitChannel && g > limitChannel && b > limitChannel
      && r == g && r == b && g == b
}

fun Int.withAlpha(alpha: Int): Int {
  assertThat(alpha in 0..255) { "Incorrect alpha: $alpha" }
  val a = alpha shl 16
  return this and 0xFF000000.toInt() or a
}

fun Int.withAlpha(alpha: Float): Int {
  assertThat(alpha in 0f..1f) { "Incorrect alpha: $alpha" }
  return withAlpha((alpha * 255).toInt())
}

fun lerpColor(startColor: Int, endColor: Int, fraction: Float): Int {
  val startA = (startColor shr 24 and 0xff) / 255.0f
  var startR = (startColor shr 16 and 0xff) / 255.0f
  var startG = (startColor shr 8 and 0xff) / 255.0f
  var startB = (startColor and 0xff) / 255.0f
  
  val endInt = endColor
  val endA = (endInt shr 24 and 0xff) / 255.0f
  var endR = (endInt shr 16 and 0xff) / 255.0f
  var endG = (endInt shr 8 and 0xff) / 255.0f
  var endB = (endInt and 0xff) / 255.0f
  
  // convert from sRGB to linear
  startR = startR.toDouble().pow(2.2).toFloat()
  startG = startG.toDouble().pow(2.2).toFloat()
  startB = startB.toDouble().pow(2.2).toFloat()
  
  endR = endR.toDouble().pow(2.2).toFloat()
  endG = endG.toDouble().pow(2.2).toFloat()
  endB = endB.toDouble().pow(2.2).toFloat()
  
  // compute the interpolated color in linear space
  var a = startA + fraction * (endA - startA)
  var r = startR + fraction * (endR - startR)
  var g = startG + fraction * (endG - startG)
  var b = startB + fraction * (endB - startB)
  
  // convert back to sRGB in the [0..255] range
  a *= 255.0f
  r = r.toDouble().pow(1.0 / 2.2).toFloat() * 255.0f
  g = g.toDouble().pow(1.0 / 2.2).toFloat() * 255.0f
  b = b.toDouble().pow(1.0 / 2.2).toFloat() * 255.0f
  
  return (a.roundToInt() shl 24) or (r.roundToInt() shl 16) or (g.roundToInt() shl 8) or b.roundToInt()
}
