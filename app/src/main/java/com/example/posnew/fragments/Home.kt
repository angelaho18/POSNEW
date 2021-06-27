package com.example.posnew.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.posnew.News
import com.example.posnew.NewsAdapter
import com.example.posnew.R

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Home : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var interfaceFragment: InterfaceFragment
    private lateinit var newsAdapter : NewsAdapter

    private var MyNews : MutableList<News> = mutableListOf(
        News("https://i.ibb.co/wBYDxLq/beach.jpg", "Lorem Ipsum"),
        News("https://i.ibb.co/dBCHzXQ/paris.jpg", "Dolor Amet")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        interfaceFragment = activity as InterfaceFragment

        val searchView = view.findViewById<SearchView>(R.id.search_view)
        val RecyclerViewHome = view.findViewById<RecyclerView>(R.id.RecyclerViewHome)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    interfaceFragment.search(query)
                }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        newsAdapter = NewsAdapter(MyNews)
        RecyclerViewHome.adapter = newsAdapter
        RecyclerViewHome.layoutManager = LinearLayoutManager(context)

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Home().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}