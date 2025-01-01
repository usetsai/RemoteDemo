package com.usetsai.embedclient

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.usetsai.embedclient.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.surface.setZOrderOnTop(true)

        val motor = MainMotor(this)
        motor.surfacePackage.observe(this) {
            binding.surface.setChildSurfacePackage(it)
        }

        binding.connect.setOnClickListener {
            motor.bind(
                binding.surface.hostToken,
                binding.surface.display.displayId,
                binding.surface.width,
                binding.surface.height
            )
        }
    }
}
