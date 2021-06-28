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
        News("https://foto.kontan.co.id/tNrGi7jXHvZLFkdDzY9Y2YueIL8=/smart/2016/05/08/1577452072p.jpg", "Bisnis Kemasan masih melaju di tengah kenaikan harga minyak mentah global","https://industri.kontan.co.id/news/bisnis-kemasan-masih-melaju-di-tengah-kenaikan-harga-minyak-mentah-global"),
        News("https://cdn1-production-images-kly.akamaized.net/9rit_iJzlpLopMYtRoP70FMMya4=/1280x720/smart/filters:quality(75):strip_icc():format(jpeg)/kly-media-production/medias/3422105/original/065512800_1617780066-20210407-Jelang_Ramadan__Harga_Bahan_Pangan_Stabil-8.jpg","Pemerintah Harus Waspada kenaikan Harga Pangan Jelang Idul Adha","https://www.liputan6.com/bisnis/read/4589822/pemerintah-harus-waspadai-kenaikan-harga-pangan-jelang-idul-adha"),
        News("https://foto.kontan.co.id/X5Qx96H7ByehcnDMR2zV-vqh4gw=/smart/2021/03/11/336629384p.jpg", "Harga bahan bangunan naik, begini efek domino ke sektor infrastruktur dan properti","https://industri.kontan.co.id/news/harga-bahan-bangunan-naik-begini-efek-domino-ke-sektor-infrastruktur-dan-properti"),
        News("https://statik.tempo.co/data/2021/01/04/id_991883/991883_720.jpg", "BEI: Kapitalisasi Pasar Naik 0,28 Persen di Pekan Keempat Juni 2021","https://bisnis.tempo.co/read/1476964/bei-kapitalisasi-pasar-naik-028-persen-di-pekan-keempat-juni-2021"),
        News("https://cdn0-production-images-kly.akamaized.net/8S0hHB2W_9kejDktfgeN6OsNJv4=/1280x720/smart/filters:quality(75):strip_icc():format(jpeg)/kly-media-production/medias/3492244/original/091729800_1624531846-20210624-Kelangkaan_Tabung_Oksigen_akibat_Lonjakan_Kasus_COVID-19-1.jpg", "Stok Tabung Oksigen di Pasar Pramuka Jakarta Langka Sejak 25 Juni 2021","https://www.liputan6.com/bisnis/read/4592558/stok-tabung-oksigen-di-pasar-pramuka-jakarta-langka-sejak-25-juni-2021")

    )
    //https://industri.kontan.co.id/news/bisnis-kemasan-masih-melaju-di-tengah-kenaikan-harga-minyak-mentah-global
    //https://www.liputan6.com/bisnis/read/4589822/pemerintah-harus-waspadai-kenaikan-harga-pangan-jelang-idul-adha
    //https://industri.kontan.co.id/news/harga-bahan-bangunan-naik-begini-efek-domino-ke-sektor-infrastruktur-dan-properti
    //https://bisnis.tempo.co/read/1476964/bei-kapitalisasi-pasar-naik-028-persen-di-pekan-keempat-juni-2021
    //https://www.liputan6.com/bisnis/read/4592558/stok-tabung-oksigen-di-pasar-pramuka-jakarta-langka-sejak-25-juni-2021
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