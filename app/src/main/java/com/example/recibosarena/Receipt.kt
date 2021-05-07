package com.example.recibosarena


import com.example.recibosarena.databinding.ActivityMainBinding
import com.google.android.material.textfield.TextInputLayout
import com.ibm.icu.text.RuleBasedNumberFormat
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*



class Receipt(private val formBinding: ActivityMainBinding) {
    var number: String = "000000"
        set(stringNumber : String){
            field = if (stringNumber == "") "000000"
                    else String.format("%06d", stringNumber.toInt())
        }
    var date: Date?
    var receiverName: String
    var receiverCI: String
    var giverName: String
    var giverCI: String
    var amount: BigDecimal
    private var _currency: Int = if(formBinding.receiptCurrencyTypeField.isChecked) USD else BOB
    val currency: Int
        get() = when(_currency){
            BOB -> 0
            USD -> 1
            else -> 0
        }
    val currencyString1: String
        get() = when(_currency){
            BOB -> "Bolivianos"
            USD -> "Dólares"
            else -> "Bs"
        }
    var writtenAmount: String
    var concept: String
    var total: BigDecimal
    var onAccount: BigDecimal
    var balance: BigDecimal
    private var _transactionType: Int = when(formBinding.receiptTransactionTypeRadioGroup.checkedRadioButtonId) {
        R.id.receipt_transaction_type_cash_radio_button -> CASH
        R.id.receipt_transaction_type_bank_radio_button -> BANK
        R.id.receipt_transaction_type_cheque_radio_button -> CHEQUE
        else -> CASH
    }
    val transactionType: Int
        get() =  when(_transactionType) {
            CASH -> 0
            CHEQUE -> 1
            BANK -> 2
            else -> 0
        }
    val transactionTypeString: String
        get() =  when(_transactionType) {
            CASH -> "Efectivo"
            BANK -> "Banco"
            CHEQUE -> "Cheque"
            else -> "Efectivo"
        }

    companion object {
        const val BANK = 2
        const val CHEQUE = 1
        const val CASH = 0
        const val USD = 1
        const val BOB = 0
    }

    init {

        // Checks all the input fields before starting
        if(areInputsInvalid()) throw IllegalArgumentException("Debe Rellenar todos los campos")
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
        // Concept
        concept = formBinding.receiptConceptField.text.toString()
        // Amount
        amount = BigDecimal(formBinding.receiptAmountField.text.toString().toDouble())
        amount = amount.setScale(2, RoundingMode.CEILING)
        formBinding.receiptAmountField.setText(amount.toString())
        writtenAmount = convertIntoWords(amount.toDouble(), "es", "BO") + when(_currency) {
            USD -> " Dólares."
            BOB -> " Bolivianos."
            else -> " Bolivianos"
        }
        // Total
        total = BigDecimal(formBinding.receiptTotalField.text.toString().toDouble())
        total = total.setScale(2, RoundingMode.CEILING)
        formBinding.receiptTotalField.setText(total.toString())
        // On Account
        onAccount = BigDecimal(formBinding.receiptOnAccountField.text.toString().toDouble())
        onAccount = onAccount.setScale(2, RoundingMode.CEILING)
        formBinding.receiptOnAccountField.setText(onAccount.toString())
        // Balance
        balance = total - onAccount
        balance = balance.setScale(2, RoundingMode.HALF_UP)

    }


    override fun toString(): String {
        return "\n" +
                "Número de Recibo: $number" + "\n" +
                "Fecha: $date" + "\n" +
                "Moneda: $currencyString1" + "\n" +
                "Cantidad: $amount" + "\n" +
                "Recibí de: $giverName" + "\n" +
                "Con CI: $giverCI" + "\n" +
                "Tipo de transacción: $transactionTypeString" + "\n" +
                "La suma de: $writtenAmount" + "\n" +
                "Concepto: $concept" + "\n" +
                "Total: $total" + "\n" +
                "A cuenta: $onAccount" + "\n" +
                "Saldo: $balance" + "\n" +
                "Recibo conforme: $receiverName" + "\n" +
                "Con CI: $receiverCI" + "\n"
    }

    private fun convertIntoWords(numericalAmount: Double, language: String, country: String) : String{
        val local : Locale = Locale(language, country)
        val rbnf = RuleBasedNumberFormat(local, RuleBasedNumberFormat.SPELLOUT)
        return rbnf.format(numericalAmount)
    }

    private fun areInputsInvalid(): Boolean{
        var areInvalid = false

        // --------------- RECEIVER Name VALIDATION -----------------------------
        (formBinding.receiptReceiverNameField.parent.parent as TextInputLayout).error =
            if (formBinding.receiptReceiverNameField.text.toString() == "") { areInvalid = true
                "Debe escribir el nombre" } else null

        // --------------- RECEIVER CI VALIDATION -----------------------------
        (formBinding.receiptReceiverCiField.parent.parent as TextInputLayout).error =
            if (formBinding.receiptReceiverCiField.text.toString() == "") { areInvalid = true
                "Debe escribir el CI" } else null

        // --------------- GIVER NAME VALIDATION -----------------------------
        (formBinding.receiptGiverNameField.parent.parent as TextInputLayout).error =
            if (formBinding.receiptGiverNameField.text.toString() == "") { areInvalid = true
                "Debe escribir el nombre" } else null

        // --------------- GIVER CI VALIDATION -----------------------------
        (formBinding.receiptGiverCiField.parent.parent as TextInputLayout).error =
            if (formBinding.receiptGiverCiField.text.toString() == "") { areInvalid = true
                "Debe escribir el CI" } else null

        // --------------- CONCEPT VALIDATION -----------------------------
        (formBinding.receiptConceptField.parent.parent as TextInputLayout).error =
            if (formBinding.receiptConceptField.text.toString() == "") { areInvalid = true
                "Campo obligatorio" } else null

        // --------------- AMOUNT VALIDATION -----------------------------
        (formBinding.receiptAmountField.parent.parent as TextInputLayout).error =
            if (formBinding.receiptAmountField.text.toString() == "") { areInvalid = true
                "Ingrese un monto" } else null
        // --------------- TOTAL VALIDATION -----------------------------
        (formBinding.receiptTotalField.parent.parent as TextInputLayout).error =
            if (formBinding.receiptTotalField.text.toString() == "") { areInvalid = true
                "Ingrese un monto" } else null
        // --------------- ON ACCOUNT VALIDATION -----------------------------
        (formBinding.receiptOnAccountField.parent.parent as TextInputLayout).error =
            if (formBinding.receiptOnAccountField.text.toString() == "") { areInvalid = true
                "Ingrese un monto" } else null

        return  areInvalid
    }


}