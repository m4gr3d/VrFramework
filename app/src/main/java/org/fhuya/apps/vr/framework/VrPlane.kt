package org.fhuya.apps.vr.framework

import android.opengl.GLES11Ext
import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

/**
 * Created by fhuya on 4/7/17.
 */
internal class VrPlane {
    companion object {
        private const val SIZE_FLOAT_IN_BYTES = 4
    }

    private val vertices = arrayOf(
            -1f, -1f,
            1f, -1f,
            -1f, 1f,
            1f, 1f
    )

    private val textureVertices = arrayOf(
            0f, 1f,
            1f, 1f,
            0f, 0f,
            1f, 0f
    )

    private val verticesBuffer: FloatBuffer by lazy {
        val buff = ByteBuffer.allocateDirect(vertices.size * SIZE_FLOAT_IN_BYTES)
        buff.order(ByteOrder.nativeOrder())
        val result = buff.asFloatBuffer().put(vertices.toFloatArray())
        result.position(0)
        result
    }

    private val textureBuffer: FloatBuffer by lazy {
        val buff = ByteBuffer.allocateDirect(textureVertices.size * SIZE_FLOAT_IN_BYTES)
        buff.order(ByteOrder.nativeOrder())
        val result = buff.asFloatBuffer().put(textureVertices.toFloatArray())
        result.position(0)
        result
    }

    private val vertexShaderCode =  """
attribute vec4 aPosition;
attribute vec2 aTexPosition;
varying vec2 vTexPosition;
void main() {
  gl_Position = aPosition;
  vTexPosition = aTexPosition;
}
"""

    private val fragmentShaderCode = """
precision mediump float;
uniform sampler2D uTexture;
varying vec2 vTexPosition;
void main() {
  gl_FragColor = texture2D(uTexture, vTexPosition);
}
"""

    private val vertexShader: Int
    private val fragmentShader: Int
    private val program: Int

    init {
        vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER)
        GLES20.glShaderSource(vertexShader, vertexShaderCode)
        GLES20.glCompileShader(vertexShader)

        fragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER)
        GLES20.glShaderSource(fragmentShader, fragmentShaderCode)
        GLES20.glCompileShader(fragmentShader)

        program = GLES20.glCreateProgram()
        GLES20.glAttachShader(program, vertexShader)
        GLES20.glAttachShader(program, fragmentShader)

        GLES20.glLinkProgram(program)
    }

    fun draw(texture: Int) {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0)
        GLES20.glUseProgram(program)
        GLES20.glDisable(GLES20.GL_BLEND)

        val positionHandle = GLES20.glGetAttribLocation(program, "aPosition")
        val textureHandle = GLES20.glGetUniformLocation(program, "uTexture")
        val texturePositionHandle = GLES20.glGetAttribLocation(program, "aTexPosition")

        GLES20.glVertexAttribPointer(texturePositionHandle, 2, GLES20.GL_FLOAT, false, 0, textureBuffer)
        GLES20.glEnableVertexAttribArray(texturePositionHandle)

        GLES20.glActiveTexture(GLES20.GL_TEXTURE1)
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture)
        GLES20.glUniform1i(textureHandle, 1)

        GLES20.glVertexAttribPointer(positionHandle, 2, GLES20.GL_FLOAT, false, 0, verticesBuffer)
        GLES20.glEnableVertexAttribArray(positionHandle)

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
    }
}