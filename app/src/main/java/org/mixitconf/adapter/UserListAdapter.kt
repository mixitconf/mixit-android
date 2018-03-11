package org.mixitconf.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import org.mixitconf.R
import org.mixitconf.SpeakerDetailActivity
import org.mixitconf.model.Language
import org.mixitconf.model.User
import org.mixitconf.service.SpeakerService
import org.mixitconf.service.markdownToHtml

class UserListAdapter(val items: List<User>, val context: Context) : RecyclerView.Adapter<UserListAdapter.UserViewHolder>() {

    inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val speakerName = view.findViewById<TextView>(R.id.speaker_name)
        val speakerBio = view.findViewById<TextView>(R.id.speaker_bio)
        val speakerImage = view.findViewById<ImageView>(R.id.speaker_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_speaker_item, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = items.get(position)

        holder.speakerName.setText("${user.firstname} ${user.lastname}".trim())
        holder.speakerBio.setText(user.description.get(Language.FRENCH)?.markdownToHtml())
        SpeakerService.getInstance(context).findSpeakerImage(holder.speakerImage, user)

        holder.itemView.setOnClickListener(UserListAdapter.OnSpeakerClickListener(user, context))
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView?) {
        super.onDetachedFromRecyclerView(recyclerView)
        recyclerView?.setOnClickListener(null)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class OnSpeakerClickListener(val user: User, val context: Context) : View.OnClickListener {
        override fun onClick(view: View?) {
            val intent = Intent(context, SpeakerDetailActivity::class.java).apply {
                putExtra(SpeakerDetailActivity.SPEAKER_LOGIN, user.login)
            }
            context.startActivity(intent)
        }

    }
}
