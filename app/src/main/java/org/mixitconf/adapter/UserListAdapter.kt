package org.mixitconf.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import org.mixitconf.R
import org.mixitconf.fragment.SpeakerFragment
import org.mixitconf.model.Language
import org.mixitconf.model.User
import org.mixitconf.service.fullname
import org.mixitconf.service.setSpeakerImage

class UserListAdapter(private val items: List<User>, val context: Context) : RecyclerView.Adapter<UserListAdapter.UserViewHolder>() {

    inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val speakerName:TextView = view.findViewById(R.id.speaker_name)
        val speakerBio:TextView = view.findViewById(R.id.speaker_bio)
        val speakerImage:ImageView = view.findViewById(R.id.speaker_image)
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder =
            UserViewHolder(LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.fragment_speaker_item, parent, false))

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = items[position]
        holder.apply {
            speakerName.text = user.fullname()
            speakerBio.text = user.description[Language.FRENCH]
            speakerImage.setSpeakerImage(user)
            itemView.setOnClickListener { _ -> (context as SpeakerFragment.OnSpeakerSelectedListener).onSpeakerSelected(user.login) }
        }
    }

    override fun onViewRecycled(holder: UserViewHolder?) {
        super.onViewRecycled(holder)
        holder?.apply {
            itemView.setOnClickListener(null)
            speakerImage.setImageDrawable(null)
        }

    }


}
