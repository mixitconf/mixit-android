package org.mixitconf.adapter

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import org.mixitconf.R
import org.mixitconf.fragment.TalkFragment
import org.mixitconf.model.Language
import org.mixitconf.model.Talk
import org.mixitconf.model.TalkFormat.*
import org.mixitconf.service.getBgColorDependingOnTime
import org.mixitconf.service.getColorLegacy
import org.mixitconf.service.getRoomLabel
import org.mixitconf.service.getTimeLabel


class TalkListAdapter(private val items: List<Talk>, val context: Context) : RecyclerView.Adapter<TalkListAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val image:ImageView = view.findViewById(R.id.talkItemImage)
        val talkLanguage:TextView = view.findViewById(R.id.talkItemLanguage)
        val name:TextView = view.findViewById(R.id.talkItemName)
        val description:TextView = view.findViewById(R.id.talkItemDescription)
        val time:TextView = view.findViewById(R.id.talkItemTime)
        val room:TextView = view.findViewById(R.id.talkItemRoom)
        val type:TextView = view.findViewById(R.id.talkItemType)
        val infoLayout:LinearLayout = view.findViewById(R.id.talkItemTimeContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TalkListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_talk_item, parent, false)
        return ViewHolder(view)
    }

    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val talk = items[position]

        holder.name.text = talk.title
        holder.time.text = talk.getTimeLabel(context)

        when (talk.format) {
            TALK, WORKSHOP, KEYNOTE -> {
                paintItemView(holder, talk.getBgColorDependingOnTime(android.R.color.white))
                displayFields(holder)

                holder.talkLanguage.visibility = if (talk.language == Language.ENGLISH) View.VISIBLE else View.GONE
                holder.image.setImageResource(talk.getTopicDrawableResource())
                holder.type.text = talk.format.name
                holder.description.text = talk.summary
                holder.room.setText(talk.getRoomLabel(context))
                holder.itemView.setOnClickListener({ _ -> (context as TalkFragment.OnTalkSelectedListener).onTalkSelected(talk.id) })
            }
            DAY -> {
                paintItemView(holder, talk.getBgColorDependingOnTime(R.color.colorPrimary), nameColor = android.R.color.white)
                holder.name.textAlignment = View.TEXT_ALIGNMENT_CENTER
                hideFields(holder, hideTime = true)
            }
            RANDOM, PARTY -> {
                paintItemView(holder, talk.getBgColorDependingOnTime(R.color.colorAccent), timeColor = android.R.color.white)
                hideFields(holder, hideImage = false)
            }
            SESSION_INTRO, LUNCH, ORGA -> {
                paintItemView(holder, talk.getBgColorDependingOnTime(R.color.colorShadow))
                hideFields(holder, hideImage = false)
            }
            PAUSE_10_MIN, PAUSE_20_MIN, PAUSE_30_MIN -> {
                paintItemView(holder, talk.getBgColorDependingOnTime(R.color.colorShadow))
                hideFields(holder, hideImage = false)
            }
        }

    }

    private fun paintItemView(holder: ViewHolder,
                              background: Int,
                              nameColor: Int = android.R.color.black,
                              timeColor: Int = R.color.textShadow) {
        holder.itemView.setBackgroundColor(context.getColorLegacy(background))
        holder.name.setTextColor(context.getColorLegacy(nameColor))
        holder.time.setTextColor(context.getColorLegacy(timeColor))
        holder.name.textAlignment = View.TEXT_ALIGNMENT_INHERIT
    }

    private fun hideFields(holder: ViewHolder, hideName: Boolean = false, hideTime: Boolean = false, hideImage: Boolean = true) {
        holder.type.visibility = View.GONE
        holder.description.visibility = View.GONE
        holder.room.visibility = View.GONE
        holder.talkLanguage.visibility = View.GONE
        holder.name.visibility = if (hideName) View.GONE else View.VISIBLE
        holder.infoLayout.visibility = if (hideTime) View.GONE else View.VISIBLE
        holder.time.visibility = if (hideTime) View.GONE else View.VISIBLE
        holder.itemView.setOnClickListener(null)
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
        holder.image.visibility = View.VISIBLE
        holder.type.visibility = View.VISIBLE
        holder.name.visibility = View.VISIBLE
        holder.time.visibility = View.VISIBLE
        holder.infoLayout.visibility = View.VISIBLE
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        recyclerView.setOnClickListener(null)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}