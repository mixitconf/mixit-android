package org.mixitconf.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_home.*
import org.mixitconf.R
import org.mixitconf.service.hasIntentPackage


class HomeFragment : Fragment() {

    lateinit var onFloorSelectedListener: OnFloorSelectedListener

    /**
     * Interface implemented by parent activity to display a floor map whe user clicks on this button
     */
    interface OnFloorSelectedListener {
        fun onFloorSelected(id: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onFloorSelectedListener = context as OnFloorSelectedListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mapCpeFloor0Button.setOnClickListener({ _ -> onFloorSelectedListener.onFloorSelected("1") })
        mapCpeSubFloorButton.setOnClickListener({ _ -> onFloorSelectedListener.onFloorSelected("0") })
        comeToPartyButton.setOnClickListener(OnGmapButtonClickListener(context, "geo:45.767643,4.8328633?z=17&q=Hôtel+de+Ville+de+Lyon"))
        comeToMiXiTButton.setOnClickListener(OnGmapButtonClickListener(context, "geo:45.78392,4.869014?z=17&q=CPE+Lyon,+43+Boulevard+du+11+novembre,+69100+Villeurbanne"))
    }


    class OnGmapButtonClickListener(val context: Context, val uri: String) : View.OnClickListener {
        override fun onClick(v: View?) {
            val hasMapApp = context.hasIntentPackage("com.google.android.apps.maps")
            if (hasMapApp) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                context.startActivity(intent)
            }
        }

    }
}
