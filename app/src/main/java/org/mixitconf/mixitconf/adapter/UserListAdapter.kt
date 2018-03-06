package org.mixitconf.mixitconf.adapter

import android.os.Environment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import org.mixitconf.mixitconf.R
import org.mixitconf.mixitconf.model.User
import java.io.File

class UserListAdapter(val listener: OnMemberClickListener,
                      val items: List<User>) : RecyclerView.Adapter<UserListAdapter.MemberViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_speaker_item, parent, false)
        return MemberViewHolder(view)
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        holder.bind(items[position])
        holder.listen(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class MemberViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val speaker_name: TextView
        private val speaker_bio: TextView
        private val speaker_flag: TextView
        private val speaker_image: ImageView

        init {
            speaker_image = view.findViewById(R.id.speaker_image)
            speaker_name = view.findViewById(R.id.speaker_name)
            speaker_bio = view.findViewById(R.id.speaker_bio)
            speaker_flag = view.findViewById(R.id.speaker_flag)
        }

        fun bind(user: User) {
            val context = itemView.context

            speaker_name.setText("${user.firstname} ${user.lastname}".trim())
            speaker_bio.setText("${user.company}")

            // Speaker images are downloaded on the app startup
            val speakerImage = File(context.getExternalFilesDir(Environment.DIRECTORY_DCIM), "speaker_${user.firstname}_${user.lastname}")

            if (speakerImage.exists()) {
                Picasso
                        .with(context)
                        .load(speakerImage)
                        .resizeDimen(R.dimen.speaker_image_size, R.dimen.speaker_image_size)
                        .placeholder(R.drawable.ic_unknown_24dp)
                        .into(speaker_image)
            } else {
                speaker_image.setImageResource(R.drawable.ic_unknown_24dp)
            }
        }

        fun listen(user: User) {
            itemView.setOnClickListener { v -> listener.onTalkClicked(user) }
        }
    }

    interface OnMemberClickListener {
        fun onTalkClicked(user: User)
    }
}
