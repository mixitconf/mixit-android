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
import android.widget.LinearLayout
import android.widget.TextView
import org.mixitconf.R
import org.mixitconf.TalkDetailActivity
import org.mixitconf.model.Language
import org.mixitconf.model.Talk
import org.mixitconf.model.TalkFormat.*
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
        val infoLayout = view.findViewById<LinearLayout>(R.id.talkItemTimeContainer)
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

        when (talk.format) {
            TALK, WORKSHOP, KEYNOTE -> {
                paintItemView(holder)
                displayFields(holder)

                holder.image.setImageResource(talk.getTopicDrawableRessource())
                holder.type.setText(talk.format.name)
                holder.description.setText(talk.summary)
                holder.room.setText(context.resources.getIdentifier(talk.room.name.toLowerCase(), "string", context.applicationInfo.packageName))
                holder.itemView.setOnClickListener(OnTalkClickListener(talk, context))
            }
            DAY -> {
                paintItemView(holder, R.color.colorPrimary, nameColor = android.R.color.white)
                holder.name.textAlignment = View.TEXT_ALIGNMENT_CENTER
                hideFields(holder, hideTime = true)
            }
            TIME -> {
                paintItemView(holder, R.color.textShadow, timeColor = android.R.color.white)
                holder.time.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(talk.start))
                hideFields(holder, hideName = true)
            }
            RANDOM, PARTY -> {
                paintItemView(holder, R.color.colorAccent)
                hideFields(holder, hideImage = false)
            }
            SESSION_INTRO, LUNCH, ORGA -> {
                paintItemView(holder)
                hideFields(holder, hideImage = false)
            }
            PAUSE_10_MIN, PAUSE_20_MIN, PAUSE_30_MIN -> {
                paintItemView(holder, R.color.textShadow, timeColor = android.R.color.white)
                hideFields(holder, hideImage = false)
            }
        }
    }

    private fun paintItemView(holder: ViewHolder,
                              background: Int = android.R.color.white,
                              nameColor: Int = android.R.color.black,
                              timeColor: Int = R.color.textShadow) {
        holder.itemView.setBackgroundColor(context.resources.getColor(background));
        holder.name.setTextColor(context.resources.getColor(nameColor))
        holder.time.setTextColor(context.resources.getColor(timeColor))
        holder.name.textAlignment = View.TEXT_ALIGNMENT_INHERIT
    }

    private fun hideFields(holder: ViewHolder, hideName: Boolean = false, hideTime: Boolean = false, hideImage: Boolean = true) {
        holder.type.visibility = View.GONE
        holder.description.visibility = View.GONE
        holder.room.visibility = View.GONE
        holder.langageImage.visibility = View.GONE
        holder.name.visibility = if (hideName) View.GONE else View.VISIBLE
        holder.infoLayout.visibility = if (hideTime) View.GONE else View.VISIBLE
        holder.time.visibility = if (hideTime) View.GONE else View.VISIBLE
        if (hideImage) {
            holder.image.visibility = View.GONE
        } else {
            holder.image.visibility = View.VISIBLE
            holder.image.setImageResource(R.drawable.mxt_topic_empty)
        }
    }

    private fun displayFields(holder: ViewHolder) {
        holder.description.visibility = View.VISIBLE
        holder.room.visibility = View.VISIBLE
        holder.langageImage.visibility = View.VISIBLE
        holder.image.visibility = View.VISIBLE
        holder.name.visibility = View.VISIBLE
        holder.time.visibility = View.VISIBLE
        holder.infoLayout.visibility = View.VISIBLE
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