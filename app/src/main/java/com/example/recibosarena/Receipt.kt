package com.example.recibosarena

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.recibosarena.databinding.ActivityMainBinding
import com.ibm.icu.text.RuleBasedNumberFormat
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DateFormat
import java.util.*



class Receipt(private val formBinding: ActivityMainBinding, private val context: Context) {
    var number: String = "000000"
        set(stringNumber : String){
            field = if (stringNumber == "") "000000"
                    else String.format("%06d", stringNumber.toInt())
        }
    var date: Date? = Calendar.getInstance().time

    var receiverName: String = "Mengano Pérez"
    var receiverCI: String = "0000000 LP"
    var giverName: String = "Fulano Fernández"
    var giverCI: String = "1111111 LP"

    var amount: BigDecimal = BigDecimal(0)
    var currency: Int = BOB
    var writtenAmount: String = convertIntoWords(amount.toDouble(), "es", "BO") + "Bolivianos"


    var concept: String = "Probando la aplicación"
    var total: BigDecimal = BigDecimal(0)
    var onAccount: BigDecimal = BigDecimal(0)
    var balance: BigDecimal = total - onAccount
    var transactionType: Int = CASH

    companion object {
        const val BANK = 2
        const val CHEQUE = 1
        const val CASH = 0
        const val USD = 1
        const val BOB = 0
    }

    init {
        // Number Field
        number = formBinding.receiptNumberField.text.toString()
        formBinding.receiptNumberField.setText(number)

        // Date Field
        val myCal = Calendar.getInstance()
        myCal.set(formBinding.receiptDateField.year, formBinding.receiptDateField.month, formBinding.receiptDateField.dayOfMonth)
        date = myCal.time

        // Receiver and giver Fields
        receiverName = formBinding.receiptReceiverNameField.text.toString()
        receiverCI = formBinding.receiptReceiverCiField.text.toString()
        giverName = formBinding.receiptGiverNameField.text.toString()
        giverCI = formBinding.receiptGiverCiField.text.toString()

        // Money
        if (formBinding.receiptAmountField.text.toString() != ""){
            amount = BigDecimal(formBinding.receiptAmountField.text.toString().toDouble())
        }
        amount = amount.setScale(2, RoundingMode.CEILING)
        formBinding.receiptAmountField.setText(amount.toString())
        currency = if(formBinding.receiptCurrencyTypeField.isChecked) USD else BOB
        writtenAmount = convertIntoWords(amount.toDouble(), "es", "BO") + when(currency) {
            USD -> " Dólares."
            BOB -> " Bolivianos."
            else -> " Bolivianos"
        }

        // More Data
        concept = formBinding.receiptConceptField.text.toString()
        if (formBinding.receiptTotalField.text.toString() != "") {
            total = BigDecimal(formBinding.receiptTotalField.text.toString().toDouble())
        }
        total = total.setScale(2, RoundingMode.CEILING)
        formBinding.receiptTotalField.setText(total.toString())
        if (formBinding.receiptOnAccountField.text.toString() != "") {
            onAccount = BigDecimal(formBinding.receiptOnAccountField.text.toString().toDouble())
        }
        onAccount = onAccount.setScale(2, RoundingMode.CEILING)
        formBinding.receiptOnAccountField.setText(onAccount.toString())

        balance = total - onAccount
        balance = balance.setScale(2, RoundingMode.HALF_UP)
        transactionType = when(formBinding.receiptTransactionTypeRadioGroup.checkedRadioButtonId) {
            R.id.receipt_transaction_type_cash_radio_button -> CASH
            R.id.receipt_transaction_type_bank_radio_button -> BANK
            R.id.receipt_transaction_type_cheque_radio_button -> CHEQUE
            else -> CASH
        }
    }


    override fun toString(): String {
        return "\n" +
                "Número de Recibo: $number" + "\n" +
                "Fecha: $date" + "\n" +
                "Moneda: ${when(currency) {BOB->"Bs" 
                    USD->"Dólares"
                    else->"No sé"}}" + "\n" +
                "Cantidad: $amount" + "\n" +
                "Recibí de: $giverName" + "\n" +
                "Con CI: $giverCI" + "\n" +
                "Tipo de transacción: ${when(transactionType) {CASH->"Efectivo"
                BANK-> "Banco"
                CHEQUE-> "Cheque"
                else -> "No sé"}}" + "\n" +
                "La suma de: $writtenAmount" + "\n" +
                "Concepto: $concept" + "\n" +
                "Total: $total" + "\n" +
                "A cuenta: $onAccount" + "\n" +
                "Saldo: $balance" + "\n" +
                "Recibo conforme: $receiverName" + "\n" +
                "Con CI: $receiverCI" + "\n"
    }

    fun convertIntoWords(numericalAmount: Double, language: String, country: String) : String{
        val local : Locale = Locale(language, country)
        val rbnf = RuleBasedNumberFormat(local, RuleBasedNumberFormat.SPELLOUT)
        return rbnf.format(numericalAmount)
    }
}