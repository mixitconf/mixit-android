package org.mixitconf.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.text.method.LinkMovementMethod
import kotlinx.android.synthetic.main.fragment_about.view.*
import org.mixitconf.R
import org.mixitconf.service.toHtml

/**
 * Dialog box used to display general informations
 */
class DialogAboutFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the Builder class for convenient dialog construction
        val builder = AlertDialog.Builder(activity)
        // Get the layout inflater
        val inflater = activity.layoutInflater
        // Get the view
        return builder
                .setView(inflater.inflate(R.layout.fragment_about, null).apply {
                    aboutDescription.text = activity.resources.getText(R.string.about_description).toString().toHtml()
                    aboutDescription.movementMethod = LinkMovementMethod.getInstance()
                })
                .create()
    }


}
