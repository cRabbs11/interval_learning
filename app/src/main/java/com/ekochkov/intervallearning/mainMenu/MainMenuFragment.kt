package com.ekochkov.intervallearning.mainMenu

import android.content.Intent
import android.os.Bundle
import android.util.Log

import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ekochkov.intervallearning.CategoryListAdapter
import com.ekochkov.intervallearning.MainActivity
import com.ekochkov.intervallearning.R
import com.ekochkov.intervallearning.interval.IntervalController
import com.ekochkov.intervallearning.interval.IntervalService
import com.ekochkov.intervallearning.room.Category
import com.ekochkov.intervallearning.room.RoomController
import com.ekochkov.intervallearning.room.Word
import com.ekochkov.intervallearning.utils.*
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainMenuFragment : Fragment(), OnViewClickListener<View, Category>, MainMenuContract.View,
    OnViewLongClickListener<View, Category> {

    override fun onViewLongClick(view: View, item: Category) {
        Log.d(LOG_TAG, "onViewLongClick view: ${context?.resources?.getResourceName(view.id)}")
    }

    override fun hideRepeatLayout() {
        notificationLayout.visibility = GONE
    }

    override fun showRepeatLayout(text: String) {
        notificationLayout.visibility = VISIBLE
        notificationInfo.text = text
    }

    override fun startRepeat() {
        Log.d(LOG_TAG, "startRepeat()")
        view?.findNavController()?.navigate(R.id.fragmentRepeat)
    }

    override fun showEmptyCategoryMessage() {
		Log.d(LOG_TAG, "showEmptyCategoryMessage... ")
	}
	
    override fun addNewCategory() {
        var bundle = Bundle()
        bundle.putInt("status", 1)
        view?.findNavController()?.navigate(R.id.fragmentAddCategory, bundle)
    }

    override fun onViewClick(view: View, item: Category) {
        var bundle = Bundle()
        bundle.putString("status", item.status.toString())
        bundle.putString("categoryName", item.category)

        when (view.id) {
            R.id.rightImage -> {presenter.onCategoryStatusClicked(item)}
            else -> {view?.findNavController()?.navigate(R.id.fragmentAddCategory, bundle)}
        }
    }

    override fun showCategoryList(list: ArrayList<Category>) {
        Log.d(LOG_TAG, "coins count: ${list.size} ")
        adapter.clearItems()
        adapter.setItems(list)
    }

    val LOG_TAG = MainMenuFragment::class.java.name + " BMTH "

    lateinit var recyclerView: RecyclerView
    lateinit var floatingBtn: FloatingActionButton
    lateinit var presenter: MainMenuPresenter
    lateinit var adapter: CategoryListAdapter
    lateinit var notificationLayout: LinearLayout
    lateinit var notificationInfo : TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_main_menu, container, false)
        initView(rootView)
        return rootView
    }


    fun initView(rootView: View ) {
        recyclerView = rootView.findViewById(R.id.recycler_view)
        var linearLayoutManager = LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView!!.setLayoutManager(linearLayoutManager)
        notificationInfo = rootView.findViewById(R.id.notification_info)

        floatingBtn = rootView.findViewById(R.id.floating_btn)
        floatingBtn.setOnClickListener {
            presenter.onFloatingBtnClicked()
        }

        notificationLayout = rootView.findViewById(R.id.notification_layout)
        notificationLayout.setOnClickListener {
            presenter.onNotificationClicked()
        }

        Log.d(LOG_TAG, " запустили")
        adapter = CategoryListAdapter(this, this)
        recyclerView!!.setAdapter(adapter)
        var dividerItemDecoration = DividerItemDecoration(recyclerView!!.getContext(),
            LinearLayoutManager.VERTICAL)
        recyclerView!!.addItemDecoration(dividerItemDecoration)

        var roomController = RoomController(context!!)

        var intervalController = IntervalController.getInstance(activity!!)

        presenter = MainMenuPresenter(this, roomController, intervalController)
        presenter.attachView(this)
        presenter.viewIsReady()
    }

    override fun onStart() {
        super.onStart()
        Log.d(LOG_TAG, " onStart()")
    }

    override fun onStop() {
        super.onStop()
        Log.d(LOG_TAG, " onStop()")
    }
}