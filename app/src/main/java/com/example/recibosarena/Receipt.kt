package com.example.recibosarena

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.recibosarena.databinding.ActivityMainBinding
import java.math.BigDecimal
import java.util.*


class Receipt(private val formBinding: ActivityMainBinding, private val context: Context) {
    var number: String = if (formBinding.receiptNumberField.text.toString() == "") generateReceiptNumber() else formBinding.receiptNumberField.text.toString()
    var currency: Int = BOB
    var amount: BigDecimal = BigDecimal(0)
    var writtenAmount: String = ""
    var date: Date = Calendar.getInstance().time
    var transactionType: Int = CASH
    var giverName: String = ""
    var giverCI: String = ""
    var receiverName: String = ""
    var receiverCI: String = ""
    var concept: String = ""
    var total: BigDecimal = BigDecimal(0)
    var onAccount: BigDecimal = BigDecimal(0)
    var balance: BigDecimal = BigDecimal(0)

    companion object {
        const val BANK = 2
        const val CHEQUE = 1
        const val CASH = 0
        const val USD = 1
        const val BOB = 0
    }

    override fun toString(): String {
        return "\n" +
                "Número de Recibo: $number" + "\n" +
                "Fecha: $date" + "\n" +
                "Moneda (0->Bs, 1->Sus): $currency" + "\n" +
                "Cantidad: $amount" + "\n" +
                "Recibí de: $giverName" + "\n" +
                "Con CI: $giverCI" + "\n" +
                "Tipo de transacción: $transactionType" + "\n" +
                "La suma de: $writtenAmount" + "\n" +
                "Concepto: $concept" + "\n" +
                "Total: $total" + "\n" +
                "A cuenta: $onAccount" + "\n" +
                "Saldo: $balance" + "\n" +
                "Recibo conforme: $receiverName" + "\n" +
                "Con CI: $receiverCI" + "\n"
    }

    private fun generateReceiptNumber(): String {

        val sharedPreferences: SharedPreferences = context.getSharedPreferences("ReceiptPreferences", Context.MODE_PRIVATE)

        // Next times:
        val nextNumber = sharedPreferences.getInt("storedReceiptNumber", 499) + 1
        with(sharedPreferences.edit()) {
            putInt("storedReceiptNumber", nextNumber)
            apply()
        }
        Log.i("SharedPreferences:", sharedPreferences.toString())
        return String.format("%06d", nextNumber)
    }
}