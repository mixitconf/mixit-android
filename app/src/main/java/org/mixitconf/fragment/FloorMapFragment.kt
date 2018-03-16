package org.mixitconf.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_floor.*
import org.mixitconf.MainActivity
import org.mixitconf.R
import org.mixitconf.service.Utils


class FloorMapFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_floor, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if(arguments==null){
            throw IllegalStateException("FloorMapFragment must be initialized with floor id")
        }
        val floorId = arguments.getString(Utils.OBJECT_ID)

        floorImage.setImageResource(if(floorId == "1") R.drawable.mxt_map_floor1 else R.drawable.mxt_map_subfloor)
        floorImage.setOnClickListener { context.startActivity(Intent(context, MainActivity::class.java)) }
    }
}
