package com.example.recibosarena

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.recibosarena.databinding.ActivityMainBinding
import java.lang.Error

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setDefaultReceiptNumber()

        //val genericReceipt: Receipt = Receipt(binding, this)
        //Log.i("HolaMundo", genericReceipt.toString())
        binding.previewButton.setOnClickListener { createPreview() }
    }
    private fun setDefaultReceiptNumber(){
        val sharedPreferences: SharedPreferences = getSharedPreferences("ReceiptPreferences", Context.MODE_PRIVATE)
        val nextNumber = sharedPreferences.getInt("storedReceiptNumber", 499) + 1
        binding.receiptNumberField.setText(String.format("%06d", nextNumber))
    }

    private fun createPreview() {
        try {
            val genericReceipt: Receipt = Receipt(binding, this)
            Log.i("HolaMundo", genericReceipt.toString())
        } catch (e: Error) {
            Toast.makeText(this,"Rellene todos los campos con datos válidos", Toast.LENGTH_SHORT).show()
            return
        }


    }
}