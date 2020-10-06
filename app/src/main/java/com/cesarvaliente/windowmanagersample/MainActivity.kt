package com.cesarvaliente.windowmanagersample

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.window.WindowManager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.Executor

class MainActivity : AppCompatActivity() {
    private lateinit var wm: WindowManager

    private fun runOnUiThreadExecutor(): Executor {
        val handler = Handler(Looper.getMainLooper())
        return Executor() {
            handler.post(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        wm = WindowManager(this, null)
        wm.registerDeviceStateChangeCallback(
            runOnUiThreadExecutor(),
            { deviceState ->
                device_state_change_text.text = "Posture: ${deviceState.toString()}"
            })
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        wm.registerLayoutChangeCallback(
            runOnUiThreadExecutor(),
            { windowLayoutInfo ->
                layout_change_text.text = windowLayoutInfo.toString()
            })
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val windowLayoutInfo = wm.windowLayoutInfo
        if (windowLayoutInfo.displayFeatures.size > 0) {
            configuration_changed.text = "Spanned"
        } else {
            configuration_changed.text = "Single screen mode - unspanned"
        }
    }
}