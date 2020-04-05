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
import com.ekochkov.intervallearning.utils.OnItemClickListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.ekochkov.intervallearning.WordListAdapter

class CategoryFragment : Fragment(), OnItemClickListener<Word>, CategoryContract.View {

    override fun showWordData(word: Word) {
        wordOriginal.setText(word.original)
        wordTranslate.setText(word.translate)
        categoryName.setText(word.category)
        wordOriginal.clearFocus()
        wordTranslate.clearFocus()
        categoryName.clearFocus()
    }

    override fun showToast(message: String) {
        Toast.makeText(context, message, LENGTH_SHORT).show()
    }

    override fun clearEditText() {
        wordOriginal.setText("")
        wordTranslate.setText("")
        wordOriginal.clearFocus()
        wordTranslate.clearFocus()
        categoryName.clearFocus()
    }

    override fun showCategoryName(category: String?) {
		categoryName.setText(category)
	}
	
	override fun showWordList(list: ArrayList<Word> ) {
		Log.d(LOG_TAG, "words count: ${list.size} ")
        adapter.clearItems()
        adapter.setItems(list)
	}
	
	override fun updateWordList(words: ArrayList<Word> ) {
	
	}
    
	override fun addNewCategory() {
	
	}
	
	override fun addNewWord(word: Word) {
	
	}

    override fun onItemClick(word: Word) {
        presenter.onListItemClicked(word)
    }

    val LOG_TAG = CategoryFragment::class.java.name + " BMTH "

	lateinit var categoryName: EditText
	lateinit var wordOriginal: EditText
	lateinit var wordTranslate: EditText
    lateinit var recyclerView: RecyclerView
    lateinit var presenter: CategoryPresenter
    lateinit var adapter: WordListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_add_category, container, false)
        initView(rootView)
        return rootView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.category_fragment_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean{
        when(item.getItemId()) {
            R.id.save_word-> { presenter.onSaveWordClicked() }
            R.id.delete_word-> { presenter.onDeleteWordClicked() }
        }
        return super.onOptionsItemSelected(item)
    }

    fun initView(rootView: View ) {
        recyclerView = rootView.findViewById(R.id.recycler_view)
        var linearLayoutManager = LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView!!.setLayoutManager(linearLayoutManager)
		
		categoryName = rootView.findViewById(R.id.category_name)
		wordOriginal = rootView.findViewById(R.id.word_original)
		wordTranslate = rootView.findViewById(R.id.word_translate)

        Log.d(LOG_TAG, " запустили")
        adapter = WordListAdapter(this)
        recyclerView!!.setAdapter(adapter)
        var dividerItemDecoration = DividerItemDecoration(recyclerView!!.getContext(),
            LinearLayoutManager.VERTICAL)
        recyclerView!!.addItemDecoration(dividerItemDecoration)

        var roomController = RoomController(context!!)

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
        bundle.putString("wordOriginal", wordOriginal.text.toString())
        bundle.putString("wordTranslate", wordTranslate.text.toString())
		bundle.putString("categoryName", categoryName.text.toString())
        bundle.putInt("status", arguments?.getInt("status")?:1)
        return bundle
    }
}