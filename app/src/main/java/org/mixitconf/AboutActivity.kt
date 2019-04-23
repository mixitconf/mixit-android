package org.mixitconf

import android.os.Bundle
import android.text.method.LinkMovementMethod
import kotlinx.android.synthetic.main.activity_about.*
import org.mixitconf.service.toHtml

open class AboutActivity : MixitActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        aboutDescription.text = resources.getText(R.string.about_description).toString().toHtml()
        aboutDescription.movementMethod = LinkMovementMethod.getInstance()
    }
}
