package com.arga.bfaa.submission3.ui.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import com.arga.bfaa.submission3.R
import com.arga.bfaa.submission3.broadcast.AlarmReceiver
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val PREFS_NAME = "setting_pref"
        private const val DAILY = "daily"
    }

    private lateinit var alarmReceiver: AlarmReceiver
    private lateinit var mSharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        alarmReceiver = AlarmReceiver()
        mSharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (supportActionBar != null) {
            supportActionBar?.title = getString(R.string.setting)
        }

        change_language.setOnClickListener(this)

        swDaily.isChecked = mSharedPreferences.getBoolean(DAILY, false)
        swDaily.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                alarmReceiver.setDailyAlarm(
                    applicationContext,
                    AlarmReceiver.TYPE_REPEATING,
                    getString(R.string.daily_message)
                )
            } else {
                alarmReceiver.cancelAlarm(applicationContext, AlarmReceiver.TYPE_REPEATING)
            }
            saveChange(isChecked)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.change_language -> {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
            }
        }
    }

    private fun saveChange(value: Boolean) {
        val editor = mSharedPreferences.edit()
        editor.putBoolean(DAILY, value)
        editor.apply()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }
}
