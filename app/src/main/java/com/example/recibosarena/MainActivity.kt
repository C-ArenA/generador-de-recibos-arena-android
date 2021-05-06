package com.example.recibosarena

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.recibosarena.databinding.ActivityMainBinding
import java.lang.Error
import java.lang.IllegalArgumentException

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setDefaultReceiptNumber()
        // We're gonna fill our form with dummy data for testing purposes
        binding.dummyDataButton.setOnClickListener { fillFormWithDummyData() }
        // The other button will create the preview
        binding.previewButton.setOnClickListener { createPreview() }
    }
    private fun setDefaultReceiptNumber(){
        val sharedPreferences: SharedPreferences = getSharedPreferences("ReceiptPreferences", Context.MODE_PRIVATE)
        val nextNumber = sharedPreferences.getInt("storedReceiptNumber", 499) + 1
        binding.receiptNumberField.setText(String.format("%06d", nextNumber))
    }

    private fun createPreview() {
        try {
            val genericReceipt: Receipt = Receipt(binding)
            binding.receiptDataText.text = genericReceipt.toString()
            Log.i("ReceiptData", genericReceipt.toString())
        } catch (e: IllegalArgumentException) {
            Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
            return
        }


    }

    private fun fillFormWithDummyData() {
        with(binding){
            receiptReceiverNameField.setText("Fulano Pérez")
            receiptReceiverCiField.setText("1234567 LP")
            receiptGiverNameField.setText("Juan Mengano")
            receiptGiverCiField.setText("8765432 SC")

            receiptAmountField.setText("0")
            receiptConceptField.setText("Transacción de prueba")
            receiptTotalField.setText("0")
            receiptOnAccountField.setText("0")
        }
    }
}