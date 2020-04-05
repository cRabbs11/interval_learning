package com.ekochkov.intervallearning

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.ekochkov.intervallearning.interval.IntervalLearning
import com.ekochkov.intervallearning.room.Word
import com.ekochkov.intervallearning.utils.OnItemClickListener
import kotlin.collections.ArrayList


open class WordListAdapter(clickListener: OnItemClickListener<Word>) : RecyclerView.Adapter<WordHolder>() {

    val LOG_TAG = CategoryListAdapter::class.java.name + " BMTH "

    private var clickListener: OnItemClickListener<Word>
    private var words = arrayListOf<Word>()
    private lateinit var context: Context

    init {
        Log.d(LOG_TAG, "init")
        this.clickListener=clickListener
    }

    fun setItems(list: ArrayList<Word>) {
        words = list
        notifyDataSetChanged()
    }

    fun clearItems() {
        words.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordHolder {
        Log.d(LOG_TAG, "onCreateViewHolder")
        val inflater = LayoutInflater.from(parent.getContext())
        context = parent.context
        val view = inflater.inflate(R.layout.two_image_two_line_list_item, parent, false)
        return WordHolder(view)
    }

    override fun onBindViewHolder(holder: WordHolder, position: Int) {
        Log.d(LOG_TAG, "onBindViewHolder")
        val word = words[position]
		holder.mainText.text = "${word.original}-${word.translate}"
        var intervalLearning = IntervalLearning()
        var dayToRepeat = intervalLearning.getDaysToRepeat(word.repeat_time!!.toLong())
        holder.secondaryText.text= "дней осталось: ${dayToRepeat}"
        holder.category_item.setOnClickListener {
            clickListener.onItemClick(word)
        }
    }

    override fun getItemCount(): Int {
        //Log.d(LOG_TAG, "getItemCount ${coins.size}")
        return words.size
    }


}

class WordHolder(
        itemView: View) : RecyclerView.ViewHolder(itemView) {

	var mainText: TextView
	var secondaryText: TextView
    var left_view: ImageView
    var right_view: ImageView
    var category_item: ConstraintLayout

    init {
		mainText = itemView.findViewById(R.id.mainText)
		secondaryText = itemView.findViewById(R.id.secondaryText)
        left_view = itemView.findViewById(R.id.leftImage)
        right_view = itemView.findViewById(R.id.rightImage)
        category_item = itemView.findViewById(R.id.category_item)
    }
}