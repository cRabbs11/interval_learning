package com.ekochkov.intervallearning.mainMenu

import android.os.Bundle
import android.util.Log

import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ekochkov.intervallearning.CategoryListAdapter

import com.ekochkov.intervallearning.interval.IntervalController
import com.ekochkov.intervallearning.mainMenu.addCategory.AddCategoryDialogFragment
import com.ekochkov.intervallearning.room.Category
import com.ekochkov.intervallearning.room.RoomController
import com.ekochkov.intervallearning.utils.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.lifecycle.ViewModelProviders
import com.ekochkov.intervallearning.R
import com.ekochkov.intervallearning.room.RoomModel
import com.ekochkov.intervallearning.room.WordDatabase


class MainMenuFragment : Fragment(), OnViewClickListener<View, Category>, MainMenuContract.View,
    AddCategoryDialogFragment.AddWordDialogListener {

    override fun onDialogPositiveClick(bundle: Bundle) {
        bundle.putString("status", "1")
        view?.findNavController()?.navigate(R.id.action_fragmentMainMenu_to_fragmentAddCategory, bundle)
    }

    override fun onDialogNegativeClick() {

    }

    override fun hideRepeatLayout() {
        notificationBtn.visibility = GONE
    }

    override fun showRepeatLayout(text: String) {
        notificationBtn.visibility = VISIBLE
        notificationBtn.text = text
    }

    override fun startRepeat() {
        Log.d(LOG_TAG, "startRepeat()")
        view?.findNavController()?.navigate(R.id.fragmentRepeat)
    }

    override fun showEmptyCategoryMessage() {
		Log.d(LOG_TAG, "showEmptyCategoryMessage... ")
	}
	
    override fun addNewCategory() {
        presenter.createCategoryName(object: SimpleCallback<String> {
            override fun onResult(item: String) {
                var bundle = Bundle()
                bundle.putString("categoryName", item)
                openAddCategoryFragment(bundle)
            }
        })
    }

    override fun onViewClick(view: View, item: Category) {
        var bundle = Bundle()
        bundle.putString("status", item.status.toString())
        bundle.putString("categoryName", item.category)

        when (view.id) {
            R.id.rightImage -> {presenter.onCategoryStatusClicked(item)}
            else -> {view?.findNavController()?.navigate(R.id.action_fragmentMainMenu_to_fragmentAddCategory, bundle)}
        }
    }

    override fun showCategoryList(list: ArrayList<Category>) {
        Log.d(LOG_TAG, "coins count: ${list.size} ")
        adapter.clearItems()
        adapter.setItems(list)
        if (list.isEmpty()) {
            emptyRecyclerMessageLayout.visibility= VISIBLE
        } else {
            emptyRecyclerMessageLayout.visibility= GONE
        }
    }

    val LOG_TAG = MainMenuFragment::class.java.name + " BMTH "

    lateinit var recyclerView: RecyclerView
    lateinit var floatingBtn: FloatingActionButton
    lateinit var presenter: MainMenuPresenter
    lateinit var adapter: CategoryListAdapter
    lateinit var notificationBtn: Button
    lateinit var emptyRecyclerMessageLayout: LinearLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_main_menu, container, false)
        initView(rootView)
        return rootView
    }


    fun initView(rootView: View ) {
        recyclerView = rootView.findViewById(R.id.recycler_view)
        var linearLayoutManager = LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView!!.setLayoutManager(linearLayoutManager)
        emptyRecyclerMessageLayout = rootView.findViewById(R.id.empty_recycler_message_layout)

        floatingBtn = rootView.findViewById(R.id.floating_btn)
        floatingBtn.setOnClickListener {
            presenter.onFloatingBtnClicked()
        }

        notificationBtn = rootView.findViewById(R.id.notification_btn)
        notificationBtn.setOnClickListener {
            presenter.onNotificationClicked()
        }

        Log.d(LOG_TAG, " запустили")
        adapter = CategoryListAdapter(this)
        recyclerView!!.setAdapter(adapter)
        var dividerItemDecoration = DividerItemDecoration(recyclerView!!.getContext(),
            LinearLayoutManager.VERTICAL)
        recyclerView!!.addItemDecoration(dividerItemDecoration)

        var wordDatabase = WordDatabase.DatabaseProvider.getInstance(context!!)
        var roomController = RoomController(wordDatabase)

        var intervalController = IntervalController.getInstance(activity!!)

        val model = ViewModelProviders.of(this).get(RoomModel::class.java!!)

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

    private fun openAddCategoryFragment(bundle: Bundle) {
        var addCategoryFragment = AddCategoryDialogFragment(this)
        addCategoryFragment.arguments = bundle

        var fragmentManager = getFragmentManager()
        var fragmentTransaction = fragmentManager!!.beginTransaction()
        addCategoryFragment.show(fragmentTransaction, "dialog")
    }
}