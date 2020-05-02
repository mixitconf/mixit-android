package org.mixitconf.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.mixitconf.R
import org.mixitconf.model.entity.Speaker
import org.mixitconf.model.entity.fullname
import org.mixitconf.model.entity.setSpeakerImage
import org.mixitconf.ui.OnSpeakerSelectedListener

class SpeakerListAdapter(private val onSpeakerListener: OnSpeakerSelectedListener) : RecyclerView.Adapter<SpeakerListAdapter.UserViewHolder>() {

    private val items = mutableListOf<Speaker>()

    fun update(speakers: List<Speaker>) {
        items.clear()
        items.addAll(speakers)
        notifyDataSetChanged()
    }

    inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val speakerName: TextView = view.findViewById(R.id.speaker_name)
        val speakerBio: TextView = view.findViewById(R.id.speaker_bio)
        val speakerImage: ImageView = view.findViewById(R.id.speaker_image)
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder = UserViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_speaker_item, parent, false)
                                                                                                                                                                          )

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = items[position]
        holder.apply {
            speakerName.text = user.fullname
            speakerBio.text = user.descriptionFr
            speakerImage.setSpeakerImage(user)
            itemView.setOnClickListener {
                onSpeakerListener.onSpeakerSelected(user.login)
            }
        }
    }

    override fun onViewRecycled(holder: UserViewHolder) {
        super.onViewRecycled(holder)
        holder.apply {
            itemView.setOnClickListener(null)
            speakerImage.setImageDrawable(null)
        }

    }


}
