package com.ekochkov.intervallearning.category

import android.os.Bundle
import android.util.Log

import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ekochkov.intervallearning.R
import com.ekochkov.intervallearning.room.RoomController
import com.ekochkov.intervallearning.room.Word
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.ekochkov.intervallearning.WordListAdapter
import com.ekochkov.intervallearning.category.addWord.AddWordFragment
import com.ekochkov.intervallearning.room.RoomController.Companion.WORD_LIST_FILTER_BY_ORIGINAL
import com.ekochkov.intervallearning.room.RoomController.Companion.WORD_LIST_FILTER_BY_REPEAT_TIME
import com.ekochkov.intervallearning.room.RoomController.Companion.WORD_LIST_FILTER_BY_TRANSLATE
import com.ekochkov.intervallearning.room.WordDatabase
import com.ekochkov.intervallearning.utils.OnWordItemClickListener
import com.ekochkov.intervallearning.utils.QuestionDialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CategoryFragment : Fragment(), OnWordItemClickListener<Word>, AddWordFragment.AddWordDialogListener, CategoryContract.View {

    override fun onDialogPositiveClick(bundle: Bundle) {
        //Log.d(LOG_TAG, "onDialogPositiveClick: ${value} ")
        presenter.onAddWordClicked(bundle)
    }

    override fun onDialogNegativeClick() {

    }

    override fun onDeleteClick(item: Word) {
        var deleteQuestionDialog = QuestionDialogFragment(
            "вы хотите удалить слово ${item.original}?",
            "да",
            "нет",
            object: QuestionDialogFragment.QuestionDialogListener {
                override fun onPositiveClick() {
                    presenter.onDeleteWordClicked(item)
                }
                override fun onNegativeClick() {
                }
            })

        deleteQuestionDialog.show(getFragmentManager()!!, "delete_word_dialog")
    }

    override fun onChangeClick(item: Word) {
        var bundle = Bundle()
        bundle.putString("categoryName", item.category)
        bundle.putString("wordOriginal", item.original)
        bundle.putString("wordTranslate", item.translate)
        openAddWordFragment(bundle)
    }

    override fun showWordData(word: Word) {

    }

    override fun showToast(message: String) {
        Toast.makeText(context, message, LENGTH_SHORT).show()
    }

    override fun clearEditText() {

    }

    override fun showCategoryName(category: String?) {

	}
	
	override fun showWordList(list: ArrayList<Word> ) {
		Log.d(LOG_TAG, "words count: ${list.size} ")
        adapter.clearItems()
        adapter.setItems(list)

        if (list.isEmpty()) {
            emptyRecyclerMessageLayout.visibility= View.VISIBLE
        } else {
            emptyRecyclerMessageLayout.visibility= View.GONE
        }
	}
	
	override fun updateWordList(words: ArrayList<Word> ) {
	
	}
    
	override fun addNewCategory() {
	
	}
	
	override fun addNewWord(word: Word) {
	
	}

    val LOG_TAG = CategoryFragment::class.java.name + " BMTH "

    lateinit var recyclerView: RecyclerView
    lateinit var floatingBtn: FloatingActionButton
    lateinit var presenter: CategoryPresenter
    lateinit var adapter: WordListAdapter
    lateinit var emptyRecyclerMessageLayout: LinearLayout
	
	override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_add_category, container, false)
        initView(rootView)
        return rootView
    }
	
	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
	
	override fun onOptionsItemSelected(item: MenuItem): Boolean{
        when(item.getItemId()) {
            R.id.menu_options_search-> { handleSearch(item) }
            R.id.menu_options_filter_by_original -> {adapter.orderWordListBy(WORD_LIST_FILTER_BY_ORIGINAL)}
            R.id.menu_options_filter_by_translate -> {adapter.orderWordListBy(WORD_LIST_FILTER_BY_TRANSLATE)}
            R.id.menu_options_filter_by_repeat_time -> {adapter.orderWordListBy(WORD_LIST_FILTER_BY_REPEAT_TIME)}
        }
        return super.onOptionsItemSelected(item)
    }
	
	/*
	* Поиск по словам
	*/
	private fun handleSearch(item: MenuItem) {
        var searchView = item.getActionView() as SearchView
        item.expandActionView()
		var likeQuery = ""
        searchView.setQuery(likeQuery, false)
        //var selectionArgs= ""
		var category = arguments!!.getString("categoryName")

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.equals("")) {                
                    //selectionArgs =  arrayOf("%${query}%")
					var search = "%${query}%"
                    if (category!=null) {
                        presenter.searchWords(category, search)
                    }

                }
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                //selectionArgs =  arrayOf("%${newText}%")
                var search = "%${query}%"
                if (category!=null) {
                    presenter.searchWords(category, search)
                }
                return true
            }
        })
    }

    fun initView(rootView: View ) {
        recyclerView = rootView.findViewById(R.id.recycler_view)
        var linearLayoutManager = LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView!!.setLayoutManager(linearLayoutManager)
        emptyRecyclerMessageLayout = rootView.findViewById(R.id.empty_recycler_message_layout)

        floatingBtn = rootView.findViewById(R.id.floating_btn)
        floatingBtn.setOnClickListener {
            //dialogPresenter.onShowAddDialogClicked()
            var bundle = Bundle()
            var category = arguments!!.getString("categoryName")
            bundle.putString("categoryName", category)
            openAddWordFragment(bundle)
        }

        Log.d(LOG_TAG, " запустили")
        adapter = WordListAdapter(this)
        recyclerView!!.setAdapter(adapter)
        var dividerItemDecoration = DividerItemDecoration(recyclerView!!.getContext(),
            LinearLayoutManager.VERTICAL)
        recyclerView!!.addItemDecoration(dividerItemDecoration)

        var wordDatabase = WordDatabase.DatabaseProvider.getInstance(context!!)
        var roomController = RoomController(wordDatabase)

        presenter = CategoryPresenter(roomController)
        presenter.attachView(this)
        presenter.viewIsReady()
    }
	
	override fun getBundle(): Bundle? {
        Log.d(LOG_TAG, "getBundle")
        val bundle = arguments       
        return bundle
    }
	
	override fun getWordData(): Bundle? {
        Log.d(LOG_TAG, "getBundle")
        var bundle = Bundle()

        bundle.putInt("status", arguments?.getInt("status")?:1)
        return bundle
    }

    private fun openAddWordFragment(bundle: Bundle) {
        var addWordFragment = AddWordFragment(this)
        addWordFragment.arguments = bundle

        var fragmentManager = getFragmentManager()
        var fragmentTransaction = fragmentManager!!.beginTransaction()
        addWordFragment.show(fragmentTransaction, "dialog")
    }
}