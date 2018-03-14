package org.mixitconf

import android.app.Activity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_floor_map.*

class FloorMapActivity : Activity() {

    companion object {
        val FLOOR_ID = "floorId"
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_floor_map)

        val level = intent.getIntExtra(FLOOR_ID, -1)

        floorImage.setImageResource(if (level == -1) R.drawable.mxt_map_subfloor else R.drawable.mxt_map_floor1)
        floorImage.setOnClickListener { finish() }
    }

}
