package org.mixitconf

import android.os.Bundle
import org.mixitconf.repository.TalkReader

open class MainActivity : AbstractMixitActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        TalkReader.getInstance(baseContext)
    }

    override fun onResume() {
        super.onResume()
    }
}
