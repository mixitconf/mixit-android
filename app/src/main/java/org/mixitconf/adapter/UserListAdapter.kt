package org.mixitconf.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import org.mixitconf.R
import org.mixitconf.model.User
import org.mixitconf.service.SpeakerService

class UserListAdapter(val listener: OnClickListener<User>,
                      val items: List<User>) : RecyclerView.Adapter<UserListAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_speaker_item, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(items[position])
        holder.listen(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val speakerName: TextView
        private val speakerBio: TextView
        private val speakerFlag: TextView
        private val speakerImage: ImageView

        init {
            speakerImage = view.findViewById(R.id.speaker_image)
            speakerName = view.findViewById(R.id.speaker_name)
            speakerBio = view.findViewById(R.id.speaker_bio)
            speakerFlag = view.findViewById(R.id.speaker_flag)
        }

        fun bind(user: User) {
            val context = itemView.context

            speakerName.setText("${user.firstname} ${user.lastname}".trim())
            speakerBio.setText("${if (user.company == null) "" else user.company}")
            SpeakerService.getInstance(context).findSpeakerImage(speakerImage, user)
        }

        fun listen(user: User) {
            itemView.setOnClickListener { v -> listener.invoke(user) }
        }
    }
}
