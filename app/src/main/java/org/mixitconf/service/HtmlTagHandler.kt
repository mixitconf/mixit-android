package org.mixitconf.service

import android.text.Editable
import android.text.Html
import android.text.style.BulletSpan
import org.xml.sax.XMLReader
import java.util.*


class HtmlTagHandler : Html.TagHandler {

    private val mListParents = Vector<String>()

    override fun handleTag(opening: Boolean, tag: String, output: Editable, xmlReader: XMLReader) {

        if (tag == "ul" || tag == "ol") {
            if (opening) {
                mListParents.add("ul")
            } else {
                mListParents.remove(tag)
            }
        } else if (tag == "li" && !opening) {
            handleListTag(output)
        }

    }

    private fun handleListTag(output: Editable) {
        if (mListParents.lastElement() == "ul") {
            output.append("\n")
            val split = output.toString().split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            val lastIndex = split.size - 1
            val start = output.length - split[lastIndex].length - 1
            output.setSpan(BulletSpan(15 * mListParents.size), start, output.length, 0)
        }
    }
}