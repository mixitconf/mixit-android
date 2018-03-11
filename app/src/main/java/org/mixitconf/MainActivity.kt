package org.mixitconf

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import org.mixitconf.repository.TalkReader

open class MainActivity : AbstractMixitActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainNavigation.setOnNavigationItemSelectedListener(getNavigationItemSelectedListener())

        TalkReader.getInstance(baseContext)
    }

}
