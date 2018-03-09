package org.mixitconf.mixitconf.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import org.mixitconf.mixitconf.R
import org.mixitconf.mixitconf.model.Language
import org.mixitconf.mixitconf.model.Talk
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class TalkListAdapter(val listener: OnClickListener<Talk>,
                      val items: List<Talk>) : RecyclerView.Adapter<TalkListAdapter.TalkViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TalkViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_talk_item, parent, false)
        return TalkViewHolder(view)
    }

    override fun onBindViewHolder(holder: TalkViewHolder, position: Int) {
        holder.bind(items[position])
        holder.listen(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class TalkViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val image: ImageView
        private val langageImage: ImageView
        private val name: TextView
        private val description: TextView
        private val time: TextView
        private val room: TextView
        private val type: TextView

        init {
            image = view.findViewById(R.id.talk_image)
            langageImage = view.findViewById(R.id.talk_image_language)
            name = view.findViewById(R.id.talk_name)
            description = view.findViewById(R.id.talk_description)
            time = view.findViewById(R.id.talk_time)
            room = view.findViewById(R.id.talk_room)
            type= view.findViewById(R.id.talk_type)
        }

        fun bind(talk: Talk) {
            val context = itemView.context

            name.setText(talk.title)
            description.setText(talk.summary)
            image.setImageResource(getTopicDrawableRessource(talk.topic))
            room.setText(context.resources.getIdentifier(talk.room.name.toLowerCase(), "string", context.applicationInfo.packageName))
            type.setText(talk.format.name)

            val sdf = SimpleDateFormat("EEE", Locale.getDefault())
            time.setText(String.format(context.resources.getString(R.string.talk_time_range),
                        sdf.format(talk.start),
                        DateFormat.getTimeInstance(DateFormat.SHORT).format(talk.start),
                        DateFormat.getTimeInstance(DateFormat.SHORT).format(talk.end)
            ))

            if(talk.language == Language.ENGLISH){
                langageImage.setImageResource(R.drawable.mxt_flag_en)
            }
            else{
                langageImage.setImageResource(R.drawable.mxt_flag_fr)
            }
        }

        fun listen(talk: Talk) {
            itemView.setOnClickListener { v -> listener.invoke(talk) }
        }
    }

    private fun getTopicDrawableRessource(topic:String): Int = when(topic){
        "aliens" -> R.drawable.mxt_topic_alien
        "design" -> R.drawable.mxt_topic_design
        "hacktivism" -> R.drawable.mxt_topic_hacktivism
        "learn" -> R.drawable.mxt_topic_learn
        "makers" -> R.drawable.mxt_topic_maker
        "team" -> R.drawable.mxt_topic_team
        else -> R.drawable.mxt_topic_design
    }
}