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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val talk = items.get(position)

        holder.name.setText(talk.title)
        holder.description.setText(talk.summary)
        holder.image.setImageResource(talk.getTopicDrawableRessource())
        holder.room.setText(context.resources.getIdentifier(talk.room.name.toLowerCase(), "string", context.applicationInfo.packageName))
        holder.type.setText(talk.format.name)

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

        holder.itemView.setOnClickListener(OnTalkClickListener(talk, context))
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