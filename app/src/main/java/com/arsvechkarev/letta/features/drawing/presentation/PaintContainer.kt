package com.arsvechkarev.letta.features.drawing.presentation

import android.graphics.Color
import com.arsvechkarev.letta.core.brushes.BrushType
import com.arsvechkarev.letta.views.BrushDisplayer
import com.arsvechkarev.letta.views.DrawingView
import com.arsvechkarev.letta.views.Image
import com.arsvechkarev.letta.views.VerticalSeekbar
import com.arsvechkarev.letta.views.gradientpalette.GradientPalette
import kotlin.math.pow

class PaintContainer(
  undoImage: Image,
  drawingView: DrawingView,
  palette: GradientPalette,
  verticalSeekbar: VerticalSeekbar,
  brushDisplayer: BrushDisplayer
) {
  
  init {
    val initialPercentSize = 0.3f
    val initialColor = Color.parseColor("#4000FF")
    drawingView.isClickable = true
    drawingView.brushType = BrushType.OVAL
    drawingView.brushColor = initialColor
    verticalSeekbar.updatePercent(initialPercentSize)
    drawingView.brushSize = initialPercentSize.exponentiate()
    verticalSeekbar.updateColorIfAllowed(initialColor)
    
    verticalSeekbar.onUp = {
      brushDisplayer.clear()
    }
    undoImage.setOnClickListener {
      drawingView.undo()
    }
    palette.onColorChanged = {
      verticalSeekbar.updateColorIfAllowed(it)
      drawingView.brushColor = it
    }
    verticalSeekbar.onPercentChanged = {
      val width = it.exponentiate()
      drawingView.brushSize = width
      brushDisplayer.draw(drawingView.brushColor, width)
    }
  }
  
  private fun Float.exponentiate(): Float {
    return this * 50 + (this * 6).pow(3.5f)
  }
}