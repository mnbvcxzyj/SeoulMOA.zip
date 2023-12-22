package com.mobile.seoulmoa_zip

import BaseActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobile.seoulmoa_zip.data.Exhibition
import com.mobile.seoulmoa_zip.data.ExhibitionRoot
import com.mobile.seoulmoa_zip.databinding.ActivityMainBinding
import com.mobile.seoulmoa_zip.network.IExhibitionService
import com.mobile.seoulmoa_zip.ui.ExhibitionAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : BaseActivity() {
    private val TAG = "MainActivity"

    private lateinit var retrofit: Retrofit

    lateinit var mainBinding: ActivityMainBinding
    lateinit var adapter: ExhibitionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        setupToolbar()

        adapter = ExhibitionAdapter()
        mainBinding.rvExhibitions.adapter = adapter
        mainBinding.rvExhibitions.layoutManager = LinearLayoutManager(this)

        retrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.api_url))
            .addConverterFactory(GsonConverterFactory.create())
            .build()


        fetchExhibitions()
    }

    private fun fetchExhibitions() {
        val service = retrofit.create(IExhibitionService::class.java)


        val apiCallback = object : Callback<ExhibitionRoot> {
            override fun onResponse(
                call: Call<ExhibitionRoot>,
                response: Response<ExhibitionRoot>
            ) {
                if (response.isSuccessful) {
                    val root: ExhibitionRoot? = response.body()
                    adapter.exhibitions = root?.listExhibitionOfSeoulMOAInfo?.exhibitions
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@MainActivity, "전시 정보를 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<ExhibitionRoot>, t: Throwable) {
                Toast.makeText(this@MainActivity, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        val call = service.getExhibitions(
            getString(R.string.api_key),
            getString(R.string.api_type),
            getString(R.string.api_service),
            1,
            100,
        )

        call.enqueue(apiCallback)

    }

    private fun showExhibitionDetail(exhibition: Exhibition) {

    }
}

