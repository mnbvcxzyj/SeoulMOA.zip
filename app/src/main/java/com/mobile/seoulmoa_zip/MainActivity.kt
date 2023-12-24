package com.mobile.seoulmoa_zip

import BaseActivity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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

        adapter = ExhibitionAdapter(ExhibitionAdapter.TYPE_MAIN)
        mainBinding.rvExhibitions.adapter = adapter
        mainBinding.rvExhibitions.layoutManager = LinearLayoutManager(this)

        retrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.api_url))
            .addConverterFactory(GsonConverterFactory.create())
            .build()


        fetchExhibitions()

        adapter.setOnItemClickListener(object : ExhibitionAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val exhibition = adapter.exhibitions?.get(position)
                exhibition?.let {
                    val intent = Intent(this@MainActivity, DetailActivity::class.java).apply {
                        putExtra("exhibition", it)
                    }
                    startActivity(intent)
                }
            }
        })
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

                    root?.listExhibitionOfSeoulMOAInfo?.exhibitions?.forEach { exhibition ->
                        exhibition.info = cleanHtmlString(exhibition.info)
                    }

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
            10,
        )

        call.enqueue(apiCallback)

    }

    // DP_INFO에 html 태그 및 특수문자 제거
    private fun cleanHtmlString(html: String): String {
        var result = html.replace(Regex("<[^>]*>"), "")
        result = result.replace("&lt;", "<")
            .replace("&gt;", ">")
            .replace("&amp;", "&")
            .replace("&quot;", "\"")
            .replace("&apos;", "'")
            .replace("&nbsp;", " ")
        return result
    }

}

