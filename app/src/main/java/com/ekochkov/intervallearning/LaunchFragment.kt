package ru.evgenykochkov.moneta.catalog.coin

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.evgenykochkov.moneta.MainActivity.ftrans
import ru.evgenykochkov.moneta.R
import ru.evgenykochkov.moneta.catalog.vid_chekana.RecyclerVidChekanaAdapter
import ru.evgenykochkov.moneta.change.OnItemClickListener
import ru.evgenykochkov.moneta.database.DBController
import ru.evgenykochkov.moneta.model.VidChekana

class StartFragment : Fragment() {

    val LOG_TAG = StartFragment::class.java.name + " BMTH "

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_start, container, false)
        return rootView
    }
}