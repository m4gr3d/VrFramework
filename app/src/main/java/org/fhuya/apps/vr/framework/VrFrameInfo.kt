package org.fhuya.apps.vr.framework

import android.support.annotation.DimenRes
import android.support.annotation.IdRes

/**
 * Contains information about the VR frame.
 * This will be used by {@link VrFrameView} to initialise and setup the VR frame.
 */
data class VrFrameInfo(
        @IdRes val id: Int,
        val entityName: String,
        @DimenRes val widthRes: Int,
        @DimenRes val heightRes: Int,
        val scalingFactor: Pair<Float, Float> = Pair(1f, 1f))