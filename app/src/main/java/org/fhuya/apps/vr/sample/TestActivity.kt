package org.fhuya.apps.vr.sample

import org.fhuya.apps.vr.framework.VrFragmentActivity
import org.fhuya.apps.vr.framework.VrFrameInfo

/**
 * Created by fhuya on 4/7/17.
 */
class TestActivity : VrFragmentActivity() {

    override fun getVrFrameInfoById(id: Int): VrFrameInfo? {
        return when(id) {
            else -> null
        }
    }
}