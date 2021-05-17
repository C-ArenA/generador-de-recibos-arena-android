package com.example.recibosarena

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.pdf.PdfDocument
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.recibosarena.databinding.ActivityMainBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.IllegalArgumentException
import java.text.SimpleDateFormat
import java.util.*

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
        binding.generateImage.setOnClickListener { generateBitmap() }
    }
    private fun setDefaultReceiptNumber(){
        val sharedPreferences: SharedPreferences = getSharedPreferences("ReceiptPreferences", Context.MODE_PRIVATE)
        val nextNumber = sharedPreferences.getInt("storedReceiptNumber", 499) + 1
        binding.receiptNumberField.setText(String.format("%06d", nextNumber))
    }

    private fun createPreview() {
        try {
            val genericReceipt: Receipt = Receipt(binding)
            //binding.receiptDataText.text = genericReceipt.toString()
            Log.i("ReceiptData", genericReceipt.toString())
            binding.canvasPreView.invalidate(genericReceipt)


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

    private fun generateBitmap(){
        try {
            val genericReceipt: Receipt = Receipt(binding)
            Log.i("ReceiptData", genericReceipt.toString())
            saveBitmap(binding.canvasPreView.canvasToBitmap())
            binding.canvasPreView.invalidate(genericReceipt)
            // Refresh Receipt Number
            val sharedPreferences: SharedPreferences = getSharedPreferences("ReceiptPreferences", Context.MODE_PRIVATE)
            sharedPreferences.edit().putInt("storedReceiptNumber", binding.receiptNumberField.toString().toInt()).apply()
            val nextNumber = binding.receiptNumberField.toString().toInt() + 1
            binding.receiptNumberField.setText(String.format("%06d", nextNumber))
            clearForm()
        } catch (e: IllegalArgumentException) {
            Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
            return
        }
    }
    private fun saveBitmap(bitmapToSave: Bitmap?){
        // write the document content
        // val directory: File = filesDir
        // It saves on com.example.testingpdf/test2.pdf
        val directory: File? = getExternalFilesDir(null)
        val newDir: File = File(directory, "genpdf")
        newDir.mkdir()

        val sdf = SimpleDateFormat("yyy_MM_dd-HH_mm_ss")
        val fileName : String = sdf.format(Date()) + ".jpg"
        val file = File(newDir, fileName)

        try {
            bitmapToSave?.compress(Bitmap.CompressFormat.JPEG, 100, FileOutputStream(file))
            Toast.makeText(this, "DONE!: $file", Toast.LENGTH_LONG).show()
            Log.i("Directory", file.toString())
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "WRONG!: $e", Toast.LENGTH_LONG).show()
            Log.i("Directory", file.toString())
        }
    }
    private fun clearForm(){
        binding.receiptReceiverNameField.setText("")
        binding.receiptReceiverCiField.setText("")
        binding.receiptGiverNameField.setText("")
        binding.receiptGiverCiField.setText("")
        binding.receiptAmountField.setText("")
        binding.receiptConceptField.setText("")
        binding.receiptTotalField.setText("")
        binding.receiptOnAccountField.setText("")
    }
}