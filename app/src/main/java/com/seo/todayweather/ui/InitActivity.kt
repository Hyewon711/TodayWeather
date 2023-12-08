package com.seo.todayweather.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.seo.todayweather.R
import com.seo.todayweather.databinding.ActivityInitBinding
import com.seo.todayweather.util.common.PrefManager
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
                styleClickEvent(1)
                PrefManager.getInstance().selectStyle = 1
            }
            ivStyle2.setOnAvoidDuplicateClickFlow {
                styleClickEvent(2)
                PrefManager.getInstance().selectStyle = 2
            }
            ivStyle3.setOnAvoidDuplicateClickFlow {
                styleClickEvent(3)
                PrefManager.getInstance().selectStyle = 3
            }
            ivStyle4.setOnAvoidDuplicateClickFlow {
                styleClickEvent(4)
                PrefManager.getInstance().selectStyle = 4
            }
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    private fun styleClickEvent(position: Int) {
        var initDialogStyle: String = ""
        when(position) {
            1 -> initDialogStyle = getString(R.string.init_dialog_style1)
            2 -> initDialogStyle = getString(R.string.init_dialog_style2)
            3 -> initDialogStyle = getString(R.string.init_dialog_style3)
            4 -> initDialogStyle = getString(R.string.init_dialog_style4)
        }
        MaterialAlertDialogBuilder(this@InitActivity)
            .setTitle("$initDialogStyle ${getString(R.string.init_dialog_title)}")
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