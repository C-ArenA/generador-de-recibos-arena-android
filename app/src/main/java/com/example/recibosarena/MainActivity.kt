package com.example.recibosarena

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.recibosarena.databinding.ActivityMainBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
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
            binding.generateImage.isEnabled = true
            closeKeyboard()

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
            val fileDir: File? = saveBitmap(binding.canvasPreView.canvasToBitmap())
            binding.canvasPreView.invalidate(genericReceipt)
            // Refresh Receipt Number
            val sharedPreferences: SharedPreferences = getSharedPreferences("ReceiptPreferences", Context.MODE_PRIVATE)
            sharedPreferences.edit().putInt("storedReceiptNumber", binding.receiptNumberField.text.toString().toInt()).apply()
            val nextNumber = binding.receiptNumberField.text.toString().toInt() + 1

            binding.receiptNumberField.setText(String.format("%06d", nextNumber))
            clearForm()
            // -------------------------------------------------------
            val uri  = if (Build.VERSION.SDK_INT < 24) {
                Uri.fromFile(fileDir)
            } else {
                Uri.parse(fileDir?.path) // My work-around for new SDKs, worked for me in Android 10 using Solid Explorer Text Editor as the external editor.
            }

            val shareIntent: Intent = Intent().apply{
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, uri)
                type = "image/jpeg"
            }
            startActivity(Intent.createChooser(shareIntent, "compartir recibo imagen"))
            binding.generateImage.isEnabled = false
            /*val intent: Intent = Intent(Intent.ACTION_VIEW)

            Log.i("FILEPATH", uri.toString())


            intent.setDataAndType(uri, "")
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            try {
                startActivity(intent)

            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, "There are no file explorer clients installed.", Toast.LENGTH_SHORT).show()
                Log.i("INTENT", "There are no file explorer clients installed")
            }*/

        } catch (e: IllegalArgumentException) {
            Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
            Log.i("ERRORGEN", e.message.toString())
            return
        }
    }
    private fun saveBitmap(bitmapToSave: Bitmap?): File?{
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
            return file
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "WRONG!: $e", Toast.LENGTH_LONG).show()
            Log.i("Directory", file.toString())
            return null
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

    private fun closeKeyboard() {
        // this will give us the view
        // which is currently focus
        // in this layout
        val view: View? = this.currentFocus
        // if nothing is currently
        // focus then this will protect
        // the app from crash
        if (view != null) {
            // now assign the system
            // service to InputMethodManager
            val manager: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}