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

            // Speaker images are downloaded on the app startup
            val speakerImage = File(context.getExternalFilesDir(Environment.DIRECTORY_DCIM), "speaker_${user.firstname}_${user.lastname}")

            if (speakerImage.exists()) {
                Picasso
                        .with(context)
                        .load(speakerImage)
                        .resizeDimen(R.dimen.item_image_size, R.dimen.item_image_size)
                        .placeholder(R.drawable.ic_unknown_24dp)
                        .into(this.speakerImage)
            } else {
                this.speakerImage.setImageResource(R.drawable.ic_unknown_24dp)
            }
        }

        fun listen(user: User) {
            itemView.setOnClickListener { v -> listener.invoke(user) }
        }
    }
}
