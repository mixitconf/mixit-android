package org.mixitconf.view.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.mixitconf.R
import org.mixitconf.getLegacyColor
import org.mixitconf.model.entity.Talk
import org.mixitconf.model.entity.getBgColorDependingOnTime
import org.mixitconf.model.entity.getTimeLabel
import org.mixitconf.model.entity.topicDrawableResource
import org.mixitconf.model.enums.TalkFormat.*
import org.mixitconf.view.ui.OnTalkSelectedListener
import org.mixitconf.visibility


class TalkListAdapter(
        private val onTalkListener: OnTalkSelectedListener, private val ressources: Resources
                     ) : RecyclerView.Adapter<TalkListAdapter.ViewHolder>() {

    private val items = mutableListOf<Talk>()

    fun update(talks: List<Talk>) {
        items.clear()
        items.addAll(talks)
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.talkItemImage)
        val talkLanguage: TextView = view.findViewById(R.id.talkItemLanguage)
        val name: TextView = view.findViewById(R.id.talkItemName)
        val description: TextView = view.findViewById(R.id.talkItemDescription)
        val time: TextView = view.findViewById(R.id.talkItemTime)
        val room: TextView = view.findViewById(R.id.talkItemRoom)
        val type: TextView = view.findViewById(R.id.talkItemType)
        val favoriteImg: ImageView = view.findViewById(R.id.talkFavoriteImg)
        val nonFavoriteImg: ImageView = view.findViewById(R.id.talkNotFavoriteImg)
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_talk_item, parent, false)
                                                                                  )

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        holder.apply {
            itemView.setOnClickListener(null)
            image.setImageDrawable(null)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val talk = items[position]

        holder.apply {
            name.text = talk.title
            time.text = talk.getTimeLabel(ressources)

            when (talk.format) {
                TALK, WORKSHOP, KEYNOTE, KEYNOTE_SURPRISE, CLOSING_SESSION -> {
                    paintItemView(talk.getBgColorDependingOnTime(android.R.color.white))
                    displayFields(talk = talk)
                }
                DAY -> {
                    paintItemView(R.color.colorPrimary, nameColor = android.R.color.white)
                    displayFields(nameOnCenter = true, showTime = false)
                }
                RANDOM, LIGHTNING_TALK -> {
                    paintItemView(talk.getBgColorDependingOnTime(R.color.colorSecondary))
                    displayFields(talk)
                }
                PARTY -> {
                    paintItemView(
                            talk.getBgColorDependingOnTime(R.color.colorAccent), timeColor = android.R.color.white
                                 )
                    displayFields()
                }
                SESSION_INTRO, LUNCH, ORGA, WELCOME -> {
                    paintItemView(talk.getBgColorDependingOnTime(R.color.colorShadow))
                    displayFields()
                }
                PAUSE_10_MIN, PAUSE_25_MIN, PAUSE_30_MIN -> {
                    paintItemView(talk.getBgColorDependingOnTime(R.color.colorShadow))
                    displayFields()
                }

            }
        }

    }

    private fun ViewHolder.paintItemView(
            background: Int, nameColor: Int = android.R.color.black, timeColor: Int = R.color.textShadow
                                        ) {
        itemView.setBackgroundColor(ressources.getLegacyColor(background))
        name.setTextColor(ressources.getLegacyColor(nameColor))
        time.setTextColor(ressources.getLegacyColor(timeColor))
    }

    private fun ViewHolder.displayFields(
            talk: Talk? = null, nameOnCenter: Boolean = false, showTime: Boolean = true
                                        ) {

        name.textAlignment = if (nameOnCenter) View.TEXT_ALIGNMENT_CENTER else View.TEXT_ALIGNMENT_TEXT_START

        val generalFields = arrayListOf(description, room, type, talkLanguage, image, nonFavoriteImg, favoriteImg)

        if (talk == null) {
            itemView.setOnClickListener(null)
            image.setImageDrawable(null)
            generalFields.forEach { it.visibility = View.GONE }

        } else {
            itemView.setOnClickListener { onTalkListener.onTalkSelected(talk.id) }
            generalFields.forEach { it.visibility = View.VISIBLE }
            talkLanguage.visibility = View.GONE
            image.setImageResource(talk.topicDrawableResource)
            type.setText(talk.format.label)
            description.text = talk.summary
            room.text = ressources.getText(talk.room.i18nId)
            nonFavoriteImg.visibility = (!talk.favorite).visibility
            favoriteImg.visibility = talk.favorite.visibility
        }
        time.visibility = showTime.visibility
    }

}
