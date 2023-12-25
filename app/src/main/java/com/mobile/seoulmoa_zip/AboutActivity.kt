package com.mobile.seoulmoa_zip;

import BaseActivity
import android.os.Bundle
import com.mobile.seoulmoa_zip.databinding.ActivityAboutBinding
class AboutActivity : BaseActivity() {
  private lateinit var aboutBinding: ActivityAboutBinding

        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                aboutBinding = ActivityAboutBinding.inflate(layoutInflater)
                setContentView(aboutBinding.root)
                setupToolbar()
        }
}

