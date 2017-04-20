package org.fhuya.apps.vr.framework

import android.content.Context
import android.opengl.GLSurfaceView

/**
 * Created by fhuya on 4/7/17.
 */
class VrSurfaceView(context: Context, val renderer : VrSurfaceRenderer) : GLSurfaceView(context) {

    init {
        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2)

        // Set the renderer for drawing on the VrSurfaceView
        setRenderer(renderer)
    }
}