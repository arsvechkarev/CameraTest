package com.arsvechkarev.letta.core.brushes

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import com.arsvechkarev.letta.utils.dp
import com.arsvechkarev.letta.utils.f
import com.arsvechkarev.letta.utils.i
import kotlin.random.Random

class SprayBrush(
  color: Int, width: Float
) : Brush(color, width) {
  
  private val random = Random
  private val pointSize = 1.dp
  private val exampleRect = RectF()
  
  private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    this.color = color
  }
  
  private val sprayBitmap = Bitmap.createBitmap(width.i, width.i, Bitmap.Config.ARGB_8888).apply {
    val canvas = Canvas(this)
    drawSpray(canvas, width / 2, width / 2)
  }
  private val points = ArrayList<Float>()
  
  override fun onDown(x: Float, y: Float) {
    points.add(x)
    points.add(y)
  }
  
  override fun onMove(lastX: Float, lastY: Float, x: Float, y: Float) {
    points.add(x)
    points.add(y)
  }
  
  override fun onUp(x: Float, y: Float) {
    points.add(x)
    points.add(y)
  }
  
  override fun draw(canvas: Canvas) {
    var i = 0
    while (i < points.size) {
      val x = points[i]
      val y = points[i + 1]
      i += 2
      canvas.drawBitmap(sprayBitmap, x - width / 2, y - width / 2, paint)
    }
  }
  
  override fun onExampleDraw(canvas: Canvas, x: Float, y: Float, brushWidth: Float) {
    exampleRect.set(
      x - brushWidth / 2f,
      y - brushWidth / 2f,
      x + brushWidth / 2f,
      y + brushWidth / 2f
    )
    canvas.drawBitmap(sprayBitmap, null, exampleRect, paint)
  }
  
  private fun drawSpray(canvas: Canvas, x: Float, y: Float) {
    val totalPoints = random.nextInt((width * 0.4f).i, (width * 0.6f).i)
    val path = Path()
    path.addCircle(x, y, width / 2f, Path.Direction.CW)
    canvas.clipPath(path)
    for (i in 0..totalPoints) {
      val randomX = random.nextInt((x - width / 2).i, (x + width / 2).i).f
      val randomY = random.nextInt((y - width / 2).i, (y + width / 2).i).f
      canvas.drawRect(randomX, randomY, randomX + pointSize, randomY + pointSize, paint)
    }
  }
}