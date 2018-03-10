package org.mixitconf.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import org.mixitconf.R
import org.mixitconf.model.Language
import org.mixitconf.model.Talk
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class TalkListAdapter(val listener: OnClickListener<Talk>, val items: List<Talk>, val context: Context) :
        RecyclerView.Adapter<TalkListAdapter.ViewHolder>() {

    val DATE_FORMAT = SimpleDateFormat("EEE", Locale.getDefault())

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
                DATE_FORMAT.format(talk.start),
                DateFormat.getTimeInstance(DateFormat.SHORT).format(talk.start),
                DateFormat.getTimeInstance(DateFormat.SHORT).format(talk.end)
        ))

        if(talk.language == Language.ENGLISH){
            holder.langageImage.setImageResource(R.drawable.mxt_flag_en)
        }
        else{
            holder.langageImage.setImageResource(R.drawable.mxt_flag_fr)
        }

        //holder.bind(items.get(position))
        //holder.listen(items.get(position))
        //if(!talk.dummy){
        //    itemView.setOnClickListener { v -> listener.invoke(talk) }
        //}
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView
        val langageImage: ImageView
        val name: TextView
        val description: TextView
        val time: TextView
        val room: TextView
        val type: TextView

        init {
            image = view.findViewById(R.id.talk_image)
            langageImage = view.findViewById(R.id.talk_image_language)
            name = view.findViewById(R.id.talk_name)
            description = view.findViewById(R.id.talk_description)
            time = view.findViewById(R.id.talk_time)
            room = view.findViewById(R.id.talk_room)
            type= view.findViewById(R.id.talk_type)
        }
    }
}