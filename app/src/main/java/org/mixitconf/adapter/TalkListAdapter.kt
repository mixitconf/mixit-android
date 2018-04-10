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

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TalkListAdapter.ViewHolder =
            ViewHolder(LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.fragment_talk_item, parent, false))

    override fun onViewRecycled(holder: TalkListAdapter.ViewHolder?) {
        super.onViewRecycled(holder)
        holder?.apply {
            itemView.setOnClickListener(null)
            image.setImageDrawable(null)
        }

    }

    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val talk = items[position]

        holder.apply {
            name.text = talk.title
            time.text = talk.getTimeLabel(context)

            when (talk.format) {
                TALK, WORKSHOP, KEYNOTE -> {
                    paintItemView(talk.getBgColorDependingOnTime(android.R.color.white))
                    displayFields()
                    talkLanguage.visibility = if (talk.language == Language.ENGLISH) View.VISIBLE else View.GONE
                    image.setImageResource(talk.getTopicDrawableResource())
                    type.text = talk.format.name
                    description.text = talk.summary
                    room.setText(talk.getRoomLabel(context))
                    itemView.setOnClickListener({ _ -> (context as TalkFragment.OnTalkSelectedListener).onTalkSelected(talk.id) })

                }
                DAY -> {
                    paintItemView(
                            talk.getBgColorDependingOnTime(R.color.colorPrimary),
                            nameColor = android.R.color.white)
                    name.textAlignment = View.TEXT_ALIGNMENT_CENTER
                    hideFields(hideTime = true)
                }
                RANDOM, PARTY -> {
                    paintItemView(talk.getBgColorDependingOnTime(R.color.colorAccent), timeColor = android.R.color.white)
                    holder.hideFields(hideImage = false)
                }
                SESSION_INTRO, LUNCH, ORGA -> {
                    paintItemView(talk.getBgColorDependingOnTime(R.color.colorShadow))
                    holder.hideFields(hideImage = false)
                }
                PAUSE_10_MIN, PAUSE_20_MIN, PAUSE_30_MIN -> {
                    paintItemView(talk.getBgColorDependingOnTime(R.color.colorShadow))
                    holder.hideFields(hideImage = false)
                }
            }

        }

    }

    private fun ViewHolder.paintItemView(
                              background: Int,
                              nameColor: Int = android.R.color.black,
                              timeColor: Int = R.color.textShadow) {
        itemView.setBackgroundColor(context.getColorLegacy(background))
        name.setTextColor(context.getColorLegacy(nameColor))
        time.setTextColor(context.getColorLegacy(timeColor))
        name.textAlignment = View.TEXT_ALIGNMENT_INHERIT
    }

    private fun ViewHolder.displayFields() {
        description.visibility = View.VISIBLE
        room.visibility = View.VISIBLE
        image.visibility = View.VISIBLE
        type.visibility = View.VISIBLE
        name.visibility = View.VISIBLE
        time.visibility = View.VISIBLE
        infoLayout.visibility = View.VISIBLE
    }

    private fun ViewHolder.hideFields(
            hideName: Boolean = false,
            hideTime: Boolean = false,
            hideImage: Boolean = true) {

        type.visibility = View.GONE
        description.visibility = View.GONE
        room.visibility = View.GONE
        talkLanguage.visibility = View.GONE
        name.visibility = if (hideName) View.GONE else View.VISIBLE
        infoLayout.visibility = if (hideTime) View.GONE else View.VISIBLE
        time.visibility = if (hideTime) View.GONE else View.VISIBLE
        itemView.setOnClickListener(null)
        if (hideImage) {
            image.visibility = View.GONE
        } else {
            image.visibility = View.VISIBLE
            image.setImageResource(R.drawable.mxt_topic_empty)
        }
    }

}
