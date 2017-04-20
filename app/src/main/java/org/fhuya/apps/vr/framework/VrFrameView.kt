package org.fhuya.apps.vr.framework

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.Surface
import android.widget.FrameLayout

/**
 * Created by fhuya on 4/7/17.
 */
internal class VrFrameView(context: Context, val frameInfo: VrFrameInfo) :
        FrameLayout(context), VrSurfaceRenderer.SurfaceAvailableListener {

    override fun onSurfaceAvailable(surface: Surface) {
        setSurface(surface)
    }

    private var surface: Surface? = null

    private fun setSurface(surface : Surface) {
        this.surface = surface
        invalidate()
    }

    override fun getId() = frameInfo.id

    override fun dispatchDraw(canvas : Canvas) {
        val surfaceCanvas = lockSurfaceCanvas() ?: return
        surfaceCanvas.scale(1 / frameInfo.scalingFactor.first, 1/ frameInfo.scalingFactor.second)
        super.dispatchDraw(surfaceCanvas)
        unlockSurfaceCanvas(surfaceCanvas)
    }

    private fun lockSurfaceCanvas(): Canvas? {
        if (surface?.isValid ?: false) {
            val canvas = surface?.lockCanvas(null)
            canvas?.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
            return canvas
        }
        return null
    }

    private fun unlockSurfaceCanvas(canvas : Canvas) {
        surface?.unlockCanvasAndPost(canvas)
        //TODO: Update the texture on the gl thread.
    }
}