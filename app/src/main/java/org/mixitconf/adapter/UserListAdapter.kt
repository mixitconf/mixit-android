package org.mixitconf.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.mixitconf.R
import org.mixitconf.fragment.SpeakerFragment
import org.mixitconf.model.Language
import org.mixitconf.model.User
import org.mixitconf.service.fullname
import org.mixitconf.service.setSpeakerImage

class UserListAdapter(val onSpeakerListener: SpeakerFragment.OnSpeakerSelectedListener) : RecyclerView.Adapter<UserListAdapter.UserViewHolder>() {

    private val items = mutableListOf<User>()

    fun update(users: List<User>) {
        items.clear()
        items.addAll(users)
        notifyDataSetChanged()
    }

    inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val speakerName: TextView = view.findViewById(R.id.speaker_name)
        val speakerBio: TextView = view.findViewById(R.id.speaker_bio)
        val speakerImage: ImageView = view.findViewById(R.id.speaker_image)
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
