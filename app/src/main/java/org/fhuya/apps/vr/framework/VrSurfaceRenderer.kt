package org.fhuya.apps.vr.framework

import android.content.Context
import android.graphics.*
import android.opengl.GLES11Ext.GL_TEXTURE_EXTERNAL_OES
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.util.Log
import android.view.Surface
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * Created by fhuya on 4/7/17.
 */
class VrSurfaceRenderer(context: Context) : GLSurfaceView.Renderer {

    interface SurfaceAvailableListener {
        fun onSurfaceAvailable(surface : Surface)
    }

    private val surfaceAvailabilityListeners = HashSet<SurfaceAvailableListener>()

    // TODO: complete
    private var glSurfaceTexture: SurfaceTexture? = null
    private var glSurface: Surface? = null

    private val photo: Bitmap
    private val photoWidth: Int
    private val photoHeight: Int

    private val textures = IntArray(1)
    private var plane : VrPlane? = null

    init {
        photo = BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher)
        photoWidth = photo.getWidth()
        photoHeight = photo.getHeight()
    }

    fun generateVrPlane() {
        glSurface?.release()

        GLES20.glGenTextures(1, textures, 0)
        GLES20.glBindTexture(GL_TEXTURE_EXTERNAL_OES, textures[0])

        GLES20.glTexParameteri(GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE)

//        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, photo, 0)
        val surfaceTexture = SurfaceTexture(textures[0])
        glSurfaceTexture = surfaceTexture
        glSurfaceTexture?.setOnFrameAvailableListener {
            Log.d("VrSurfaceRenderer", "Surface texture frame available.")
        }
        glSurface = Surface(surfaceTexture)
        plane = VrPlane()

        for(listener in surfaceAvailabilityListeners) {
            listener.onSurfaceAvailable(glSurface!!)
        }
    }

    fun registerSurfaceAvailableListener(listener: SurfaceAvailableListener) {
        if (glSurface != null) {
            listener.onSurfaceAvailable(glSurface!!)
        }
        surfaceAvailabilityListeners.add(listener)
    }

    fun unregisterSurfaceAvailableListener(listener : SurfaceAvailableListener) {
        surfaceAvailabilityListeners.remove(listener)
    }

    override fun onDrawFrame(gl: GL10) {
        val transformMatrix = FloatArray(16)
        glSurfaceTexture?.getTransformMatrix(transformMatrix)
        val matrix = Matrix().apply {
            setValues(transformMatrix)
        }
        // Draw the photo onto the surface.
        val canvas = glSurface?.lockCanvas(null) ?: return
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        val paint = Paint().apply {
            color = Color.WHITE
        }
//        canvas.drawBitmap(photo, Matrix(), paint)
        canvas.drawColor(Color.RED)
        glSurface?.unlockCanvasAndPost(canvas)
        glSurfaceTexture?.updateTexImage()

        plane?.draw(textures[0])
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        // Set the background frame color
        GLES20.glClearColor(0f, 0f, 0f, 1f)
        generateVrPlane()
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {}
}