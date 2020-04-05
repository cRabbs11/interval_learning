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
import com.ekochkov.intervallearning.room.Category
import com.ekochkov.intervallearning.room.Word
import com.ekochkov.intervallearning.utils.Animator
import com.ekochkov.intervallearning.utils.OnItemClickListener
import com.ekochkov.intervallearning.utils.OnViewClickListener


open class CategoryListAdapter(clickListener: OnViewClickListener<View, Category>) : RecyclerView.Adapter<CategoryHolder>() {

    val LOG_TAG = CategoryListAdapter::class.java.name + " BMTH "

    private var clickListener: OnViewClickListener<View, Category>
    private var categoryList = arrayListOf<Category>()
    private lateinit var context: Context

    init {
        Log.d(LOG_TAG, "init")
        this.clickListener=clickListener
    }

    fun setItems(list: ArrayList<Category>) {
        categoryList = list
        notifyDataSetChanged()
    }

    fun clearItems() {
        categoryList.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        Log.d(LOG_TAG, "onCreateViewHolder")
        val inflater = LayoutInflater.from(parent.getContext())
        context = parent.context
        val view = inflater.inflate(R.layout.one_image_two_line_list_item, parent, false)
        return CategoryHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        Log.d(LOG_TAG, "onBindViewHolder")
        val category = categoryList[position]
		holder.mainText.text = category.category
        holder.secondaryText.text= "${category.wordCount} слов в категории"
        holder.mainText.setOnClickListener {
            clickListener.onViewClick(it, category)
        }
        holder.secondaryText.setOnClickListener {
            clickListener.onViewClick(it, category)
        }
        holder.right_view.setOnClickListener {
            clickListener.onViewClick(it, category)
            animationRun(holder.right_view, Animator.HIDE_ANIMATION)
        }

        Log.d(LOG_TAG, "category.status: ${category.status}")
        if (category.status==1) {
            holder.right_view.setImageResource(R.drawable.ic_repeat_status_active_green_24dp)
        } else {
            holder.right_view.setImageResource(R.drawable.ic_repeat_status_off_red_24dp)
        }

        if (!holder.right_view.isEnabled) {
            animationRun(holder.right_view, Animator.SHOW_ANIMATION)
        }
    }

    override fun getItemCount(): Int {
        //Log.d(LOG_TAG, "getItemCount ${coins.size}")
        return categoryList.size
    }

    fun animationRun(view: View, typeAnim: Int) {
        var animator = Animator()
        if (typeAnim==Animator.SHOW_ANIMATION) {
            animator.showMoveFromRight(view)
        } else if (typeAnim==Animator.HIDE_ANIMATION) {
            animator.hideMoveToRight(view)
        }

    }
}

class CategoryHolder(
        itemView: View) : RecyclerView.ViewHolder(itemView) {

	var mainText: TextView
	var secondaryText: TextView
    var right_view: ImageView
    var category_item: ConstraintLayout

    init {
		mainText = itemView.findViewById(R.id.mainText)
		secondaryText = itemView.findViewById(R.id.secondaryText)
        right_view = itemView.findViewById(R.id.rightImage)
        category_item = itemView.findViewById(R.id.category_item)
    }
}