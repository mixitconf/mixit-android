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

class UserListAdapter(val items: List<User>, val context: Context) : RecyclerView.Adapter<UserListAdapter.UserViewHolder>() {

    inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val speakerName = view.findViewById<TextView>(R.id.speaker_name)
        val speakerBio = view.findViewById<TextView>(R.id.speaker_bio)
        val speakerImage = view.findViewById<ImageView>(R.id.speaker_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_speaker_item, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = items.get(position)

        holder.speakerName.text = user.fullname()
        holder.speakerBio.text = user.description.get(Language.FRENCH)
        holder.speakerImage.setSpeakerImage(user)

        holder.itemView.setOnClickListener({ _ -> (context as SpeakerFragment.OnSpeakerSelectedListener).onSpeakerSelected(user.login) })
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView?) {
        super.onDetachedFromRecyclerView(recyclerView)
        recyclerView?.setOnClickListener(null)
    }

    override fun getItemCount(): Int {
        return items.size
    }

}
