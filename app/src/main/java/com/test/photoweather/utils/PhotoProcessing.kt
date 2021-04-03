package com.test.photoweather.utils

import android.content.Context
import android.graphics.*
import com.test.photoweather.R

object PhotoProcessing {
    fun drawTextToBitmap(
        gContext: Context,
        bitmap: Bitmap,
        gText: String
    ): Bitmap? {
        var bitmap = bitmap
        val resources = gContext.resources
        val scale: Float = resources.displayMetrics.density
        var bitmapConfig = bitmap.config
        // set default bitmap config if none
        if (bitmapConfig == null) {
            bitmapConfig = Bitmap.Config.ARGB_8888
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bitmap = bitmap.copy(bitmapConfig, true)
        val canvas = Canvas(bitmap)
        // new antialised Paint
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        // text color - #3D3D3D
        paint.color = Color.WHITE
        // text size in pixels
        paint.textSize = 15f
        // text shadow
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE)

        // draw text to the Canvas center
        val bounds = Rect()
        var noOfLines = 0
        for (line in gText.split("\n").toTypedArray()) {
            noOfLines++
        }
        paint.getTextBounds(gText, 0, gText.length, bounds)
        val x = 10
        var y = bitmap.height - bounds.height() * noOfLines
        val mPaint = Paint()
        mPaint.color = gContext.resources.getColor(R.color.transparentBlack)
        val left = 0
        val top = bitmap.height - bounds.height() * (noOfLines + 1)
        val right = bitmap.width
        val bottom = bitmap.height
        canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mPaint)
        for (line in gText.split("\n").toTypedArray()) {
            canvas.drawText(line!!, x.toFloat(), y.toFloat(), paint)
            y += (paint.descent().toInt() - (paint.ascent().toInt()))
        }
        return bitmap
    }
}