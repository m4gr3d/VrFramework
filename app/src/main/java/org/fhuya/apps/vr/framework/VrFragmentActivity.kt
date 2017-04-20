package org.fhuya.apps.vr.framework

import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v4.app.FragmentActivity
import android.view.View

/**
 * Framework used to render fragments in VR.
 */
abstract class VrFragmentActivity : FragmentActivity() {

    private val vrRenderer: VrSurfaceRenderer by lazy { VrSurfaceRenderer(this) }
    private val vrFrameManager: VrFrameManager by lazy { VrFrameManager(vrRenderer) }
    private val supportVrFrameManagerWrapper: SupportVrFrameManagerWrapper by lazy {
        SupportVrFrameManagerWrapper(vrFrameManager)
    }

    private val vrView : VrSurfaceView by lazy {
        VrSurfaceView(this, vrRenderer)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.registerFragmentLifecycleCallbacks(supportVrFrameManagerWrapper, false)
        //TODO: Consider registering another fragment lifecycle callbacks to the framework fragment manager when Android O is released.

        //Create a GLSurfaceView instance and set it as the content view for this activity.
        setContentView(vrView)
    }

    override fun onDestroy() {
        super.onDestroy()
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(supportVrFrameManagerWrapper)
    }

    override fun findViewById(@IdRes id : Int): View? {
        val vrFrameInfo = getVrFrameInfoById(id) ?: return super.findViewById(id)
        return VrFrameView(this, vrFrameInfo)
    }

    /**
     * Returns the {@link VrFrameInfo} matching the given id.
     */
    abstract fun getVrFrameInfoById(@IdRes id: Int): VrFrameInfo?
}