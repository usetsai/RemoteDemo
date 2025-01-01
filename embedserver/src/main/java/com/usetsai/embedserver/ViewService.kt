package com.usetsai.embedserver

import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.display.DisplayManager
import android.os.*
import android.util.Log
import android.view.LayoutInflater
import android.view.SurfaceControlViewHost
import androidx.core.os.bundleOf
import com.usetsai.embedserver.databinding.EmbeddedBinding
import java.util.*

private const val KEY_HOST_TOKEN = "hostToken"
private const val KEY_DISPLAY_ID = "displayId"
private const val KEY_WIDTH = "width"
private const val KEY_HEIGHT = "height"
private const val KEY_SURFACE_PACKAGE = "surfacePackage"

class ViewService : Service() {
    private lateinit var messenger: Messenger
    private val handlerThread = HandlerThread("ViewService")
    private lateinit var binding: EmbeddedBinding

    override fun onCreate() {
        super.onCreate()

        handlerThread.start()

        binding = EmbeddedBinding.inflate(LayoutInflater.from(this))
        var count = 0

        binding.button.text = getString(R.string.caption, count)
        binding.button.setOnClickListener {
            Log.d("ViewService", "button clicked")
            count += 1
            binding.button.text = getString(R.string.caption, count)
        }
        binding.time.text = Date().toString()

        messenger = Messenger(ViewHandler(this, binding, handlerThread.looper))

        Log.d("ViewService", "onCreate() finished")
    }

    override fun onBind(p0: Intent?): IBinder = messenger.binder
}

private class ViewHandler(
    private val context: Context,
    private val binding: EmbeddedBinding,
    looper: Looper
) : Handler(looper) {
    private var host: SurfaceControlViewHost? = null

    override fun handleMessage(msg: Message) {
        Log.d("ViewService", "handleMessage() called")

        msg.data.apply {
            if (host == null) {
                val hostToken = getBinder(KEY_HOST_TOKEN)
                val displayId = getInt(KEY_DISPLAY_ID)
                val width = getInt(KEY_WIDTH)
                val height = getInt(KEY_HEIGHT)
                val display = context.getSystemService(DisplayManager::class.java)
                    .getDisplay(displayId)

                host = SurfaceControlViewHost(context, display, hostToken).apply {
                    setView(binding.root, width, height)

                    val pkg = surfacePackage

                    msg.replyTo.send(Message.obtain().apply {
                        data = bundleOf(KEY_SURFACE_PACKAGE to pkg)
                    })
                }
            } else {
                binding.time.text = Date().toString()
            }
        }
    }
}