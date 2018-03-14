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

        twitterLink.setOnClickListener(OnLinkClickListener(this, Action.Twitter))
        emailLink.setOnClickListener(OnLinkClickListener(this, Action.Email))
        mapCpeFloor0Button.setOnClickListener(OnLinkClickListener(this, Action.Floor0Map))
        mapCpeSubFloorButton.setOnClickListener(OnLinkClickListener(this, Action.SubFloorMap))
        comeToPartyButton.setOnClickListener(OnLinkClickListener(this, Action.ComeToParty))
        comeToMiXiTButton.setOnClickListener(OnLinkClickListener(this, Action.ComeToMixit))
    }

    class OnLinkClickListener(val context: Context, val action: Action) : View.OnClickListener {

        override fun onClick(view: View?) {
            when(action){
                Action.Twitter -> sendTwitter()
                Action.Email -> sendEmail()
                Action.ComeToMixit -> comeToMixit()
                Action.ComeToParty -> comeToParty()
                Action.SubFloorMap -> displaySubFloorMap()
                Action.Floor0Map -> displayFloor0Map()
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

        private fun comeToMixit(){
            val hasMapApp = context.hasIntentPackage("com.google.android.apps.maps")
            if(hasMapApp) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:45.78392,4.869014?z=17&q=CPE+Lyon,+43+Boulevard+du+11+novembre,+69100+Villeurbanne"))
                context.startActivity(intent)
            }
        }

        private fun comeToParty(){
            val hasMapApp = context.hasIntentPackage("com.google.android.apps.maps")
            if(hasMapApp) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:45.767643,4.8328633?z=17&q=Hôtel+de+Ville+de+Lyon"))
                context.startActivity(intent)
            }
        }

        private fun displaySubFloorMap(){

        }

        private fun displayFloor0Map(){

        }
    }

    enum class Action { Twitter, Email, ComeToMixit, ComeToParty, SubFloorMap, Floor0Map }
}
