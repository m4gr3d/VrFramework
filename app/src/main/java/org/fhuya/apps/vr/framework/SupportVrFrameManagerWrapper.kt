package org.fhuya.apps.vr.framework

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.View

/**
 * Created by fhuya on 4/7/17.
 */
internal class SupportVrFrameManagerWrapper(val frameManager: VrFrameManager) : FragmentManager.FragmentLifecycleCallbacks() {
    override fun onFragmentViewCreated(fm: FragmentManager?, fragment: Fragment?, v: View?, savedInstanceState: Bundle?) {
        frameManager.onFrameViewCreated(fragment?.view)
    }

    override fun onFragmentStopped(fm: FragmentManager?, f: Fragment?) {
        frameManager.onFrameStopped(f?.view)
    }

    override fun onFragmentResumed(fm: FragmentManager?, f: Fragment?) {
        frameManager.onFrameResumed(f?.view)
    }

    override fun onFragmentStarted(fm: FragmentManager?, f: Fragment?) {
        frameManager.onFrameStarted(f?.view)
    }

    override fun onFragmentViewDestroyed(fm: FragmentManager?, f: Fragment?) {
        frameManager.onFrameViewDestroyed(f?.view)
    }

    override fun onFragmentPaused(fm: FragmentManager?, f: Fragment?) {
        frameManager.onFramePaused(f?.view)
    }
}