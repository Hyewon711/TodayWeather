package com.seo.todayweather.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.seo.todayweather.R
import com.seo.todayweather.databinding.ActivityInitBinding
import com.seo.todayweather.util.extension.setOnAvoidDuplicateClickFlow

class InitActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInitBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInitBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        initView()
    }

    /**
     * Init view
     *
     */
    private fun initView() {
        with(binding) {
            spinnerStyle.adapter = ArrayAdapter.createFromResource(
                baseContext,
                R.array.artist_types,
                android.R.layout.simple_list_item_1
            )
            ivStyle1.setOnAvoidDuplicateClickFlow {
                styleClickEvent()
            }
            ivStyle2.setOnAvoidDuplicateClickFlow {
                styleClickEvent()
            }
            ivStyle3.setOnAvoidDuplicateClickFlow {
                styleClickEvent()
            }
            ivStyle4.setOnAvoidDuplicateClickFlow {
                styleClickEvent()
            }
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    private fun styleClickEvent() {
        MaterialAlertDialogBuilder(this@InitActivity)
            .setTitle(resources.getString(R.string.init_dialog_title))
            .setMessage(resources.getString(R.string.init_dialog_contents))
            .setNeutralButton(resources.getString(R.string.init_dialog_negative)) { dialog, which ->
                // Respond to neutral button press
            }
            .setPositiveButton(resources.getString(R.string.init_dialog_positive)) { dialog, which ->
                intent = Intent(this@InitActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            .show()
    }
}