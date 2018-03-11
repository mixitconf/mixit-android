package org.mixitconf.adapter

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import org.mixitconf.R
import org.mixitconf.TalkDetailActivity
import org.mixitconf.model.Language
import org.mixitconf.model.Talk
import org.mixitconf.service.Utils
import java.text.DateFormat


class TalkListAdapter(val items: List<Talk>, val context: Context) : RecyclerView.Adapter<TalkListAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val image = view.findViewById<ImageView>(R.id.talkItemImage)
        val langageImage = view.findViewById<ImageView>(R.id.talkItemImageLanguage)
        val name = view.findViewById<TextView>(R.id.talkItemName)
        val description = view.findViewById<TextView>(R.id.talkItemDescription)
        val time = view.findViewById<TextView>(R.id.talkItemTime)
        val room = view.findViewById<TextView>(R.id.talkItemRoom)
        val type = view.findViewById<TextView>(R.id.talkItemType)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TalkListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_talk_item, parent, false)
        return ViewHolder(view)
    }

    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val talk = items.get(position)

        holder.name.setText(talk.title)
        holder.time.setText(String.format(context.resources.getString(R.string.talk_time_range),
                Utils.DATE_FORMAT.format(talk.start),
                DateFormat.getTimeInstance(DateFormat.SHORT).format(talk.start),
                DateFormat.getTimeInstance(DateFormat.SHORT).format(talk.end)
        ))

        if (talk.language == Language.ENGLISH) {
            holder.langageImage.setImageResource(R.drawable.mxt_flag_en)
        } else {
            holder.langageImage.setImageResource(R.drawable.mxt_flag_fr)
        }

        if (talk.format.isConference()) {
            holder.image.setImageResource(talk.getTopicDrawableRessource())
            holder.type.setText(talk.format.name)
            holder.description.setText(talk.summary)
            holder.room.setText(context.resources.getIdentifier(talk.room.name.toLowerCase(), "string", context.applicationInfo.packageName))

            holder.itemView.setOnClickListener(OnTalkClickListener(talk, context))
            holder.itemView.setBackgroundColor(context.resources.getColor(android.R.color.white));
            holder.name.setTextColor(context.resources.getColor(android.R.color.black))
        } else {
            holder.itemView.setBackgroundColor(context.resources.getColor(R.color.colorPrimary));
            holder.name.setTextColor(context.resources.getColor(android.R.color.white))
            holder.name.textAlignment = View.TEXT_ALIGNMENT_CENTER
            holder.time.visibility = View.GONE
            holder.description.visibility = View.GONE
            holder.room.visibility = View.GONE
            holder.langageImage.visibility = View.GONE
            holder.image.visibility = View.GONE
            holder.type.visibility = View.GONE
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView?) {
        super.onDetachedFromRecyclerView(recyclerView)
        recyclerView?.setOnClickListener(null)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class OnTalkClickListener(val talk: Talk, val context: Context) : View.OnClickListener {
        override fun onClick(view: View?) {
            val intent = Intent(context, TalkDetailActivity::class.java).apply {
                putExtra(TalkDetailActivity.TALK_ID, talk.id)
            }
            context.startActivity(intent)
        }

    }
}