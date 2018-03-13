package org.mixitconf

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.mixitconf.service.hasIntentPackage

open class MainActivity : AbstractMixitActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        twitterLink.setOnClickListener(OnLinkClickListener(this, Social.Twitter))
        emailLink.setOnClickListener(OnLinkClickListener(this, Social.Email))
    }

    class OnLinkClickListener(val context: Context, val media: Social) : View.OnClickListener {

        override fun onClick(view: View?) {
            when(media){
                Social.Twitter -> sendTwitter()
                Social.Email -> sendEmail()
            }

        }

        private fun sendTwitter() {
            val hasTwitterApp = context.hasIntentPackage("com.twitter.android")
            val intentUri = if (hasTwitterApp) "twitter://user?screen_name=mixitconf" else "https://twitter.com/mixitconf"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(intentUri)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }

        private fun sendEmail() {
            val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:contact@mix-it.fr"))
            context.startActivity(intent)
        }
    }

    enum class Social { Twitter, Email }
}
