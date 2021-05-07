package com.example.recibosarena

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * TODO: document your custom view class.
 */
class CanvasPreView : View {

    private var _exampleString: String? = null // TODO: use a default from R.string...
    private var _exampleColor: Int = Color.RED // TODO: use a default from R.color...
    private var _exampleDimension: Float = 0f // TODO: use a default from R.dimen...

    private lateinit var textPaint: TextPaint
    private var textWidth: Float = 0f
    private var textHeight: Float = 0f

    /**
     * The text to draw
     */
    var exampleString: String?
        get() = _exampleString
        set(value) {
            _exampleString = value
            invalidateTextPaintAndMeasurements()
        }

    /**
     * The font color
     */
    var exampleColor: Int
        get() = _exampleColor
        set(value) {
            _exampleColor = value
            invalidateTextPaintAndMeasurements()
        }

    /**
     * In the example view, this dimension is the font size.
     */
    var exampleDimension: Float
        get() = _exampleDimension
        set(value) {
            _exampleDimension = value
            invalidateTextPaintAndMeasurements()
        }

    /**
     * In the example view, this drawable is drawn above the text.
     */
    var exampleDrawable: Drawable? = null

    inner class ReceiptTextField(val paperX: Float, val paperY: Float, var text: String = "XXXXXXXX"){
        val x = paperToScreenWidthScale(paperX)
        val y = paperToScreenHeightScale(paperY)
    }
    lateinit var receipt: Receipt
    // The "r" before the name indicates that its part of the Receipt
    lateinit var rAmount : ReceiptTextField
    lateinit var rNumber : ReceiptTextField
    lateinit var rDate : ReceiptTextField
    lateinit var rReceivedFrom : ReceiptTextField
    lateinit var rLiteralAmount : ReceiptTextField
    lateinit var rConcept : ReceiptTextField
    lateinit var rTotal : ReceiptTextField
    lateinit var rOnAccount : ReceiptTextField
    lateinit var rBalance : ReceiptTextField
    lateinit var rGiverName : ReceiptTextField
    lateinit var rGiverCI : ReceiptTextField
    lateinit var rReceiverName : ReceiptTextField
    lateinit var rReceiverCI : ReceiptTextField
    private var rTransactionType: Int = 0

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        rAmount = ReceiptTextField(42f, 178f)
        rNumber = ReceiptTextField(367f, 178f)
        rDate = ReceiptTextField(472f, 178f)

        rReceivedFrom = ReceiptTextField(103f, 206f)
        rLiteralAmount = ReceiptTextField(119f, 244f)
        rConcept = ReceiptTextField(105f, 283f)

        rTotal = ReceiptTextField(108f, 344f)
        rOnAccount = ReceiptTextField(292f, 344f)
        rBalance = ReceiptTextField(472f, 344f)

        rGiverName = ReceiptTextField(105f, 428f)
        rGiverCI = ReceiptTextField(105f, 443f)
        rReceiverName = ReceiptTextField(376f, 428f)
        rReceiverCI = ReceiptTextField(376f, 443f)
        // Load attributes
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.CanvasPreView, defStyle, 0
        )

        _exampleString = a.getString(
            R.styleable.CanvasPreView_exampleString
        )
        _exampleColor = a.getColor(
            R.styleable.CanvasPreView_exampleColor,
            exampleColor
        )
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        _exampleDimension = a.getDimension(
            R.styleable.CanvasPreView_exampleDimension,
            exampleDimension
        )

        if (a.hasValue(R.styleable.CanvasPreView_exampleDrawable)) {
            exampleDrawable = a.getDrawable(
                R.styleable.CanvasPreView_exampleDrawable
            )
            exampleDrawable?.callback = this
        }

        a.recycle()

        // Set up a default TextPaint object
        textPaint = TextPaint().apply {
            flags = Paint.ANTI_ALIAS_FLAG
            textAlign = Paint.Align.LEFT
        }

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements()
    }

    private fun invalidateTextPaintAndMeasurements() {
        textPaint.let {
            it.textSize = exampleDimension
            it.color = exampleColor
            textWidth = it.measureText(exampleString)
            textHeight = it.fontMetrics.bottom
        }
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        val paddingLeft = paddingLeft
        val paddingTop = paddingTop
        val paddingRight = paddingRight
        val paddingBottom = paddingBottom

        val contentWidth = width - paddingLeft - paddingRight
        val contentHeight = height - paddingTop - paddingBottom


        // Draw the example drawable on top of the text.
        exampleDrawable?.let {
            it.setBounds(
                paddingLeft, paddingTop,
                paddingLeft + contentWidth, paddingTop + contentHeight
            )
            it.draw(canvas)
        }

        drawData(canvas)
    }
    private fun paperToScreenWidthScale(length: Float): Float{
        val paperWidth = 612
        return length * width / paperWidth
    }
    private fun paperToScreenHeightScale(length: Float): Float{
        val paperHeight = 468
        return length * height / paperHeight
    }
    private fun drawData(canvas: Canvas){


        val paint = Paint()
        paint.color = Color.BLACK
        Log.i("Amount", rAmount.text + rAmount.x + rAmount.y)
        canvas.drawText(rAmount.text, rAmount.x, rAmount.y, paint)
        canvas.drawText(rNumber.text, rNumber.x, rNumber.y, paint)
        canvas.drawText(rDate.text, rDate.x, rDate.y, paint)
        canvas.drawText(rReceivedFrom.text, rReceivedFrom.x, rReceivedFrom.y, paint)
        canvas.drawText(rLiteralAmount.text, rLiteralAmount.x, rLiteralAmount.y, paint)
        canvas.drawText(rConcept.text, rConcept.x, rConcept.y, paint)
        canvas.drawText(rTotal.text, rTotal.x, rTotal.y, paint)
        canvas.drawText(rOnAccount.text, rOnAccount.x, rOnAccount.y, paint)
        canvas.drawText(rBalance.text, rBalance.x, rBalance.y, paint)
        canvas.drawText(rGiverName.text, rGiverName.x, rGiverName.y, paint)
        canvas.drawText(rGiverCI.text, rGiverCI.x, rGiverCI.y, paint)
        canvas.drawText(rReceiverName.text, rReceiverName.x, rReceiverName.y, paint)
        canvas.drawText(rReceiverCI.text, rReceiverCI.x, rReceiverCI.y, paint)
        val rTransactionTypeX = paperToScreenWidthScale(when(rTransactionType){
            0 -> 435f
            1 -> 502f
            2 -> 562f
            else -> 436f
        })
        canvas.drawText("✔", rTransactionTypeX, paperToScreenHeightScale(204f), paint)
    }


    fun invalidate(TheReceipt: Receipt) {
        receipt = TheReceipt

        val locale = when(receipt.currency) {
            0 -> Locale("es_BO", "BO")
            1 -> Locale("en_US", "US")
            else -> Locale("es_BO", "BO")
        }
        rAmount = ReceiptTextField(42f, 178f, NumberFormat.getCurrencyInstance(locale).format(receipt.amount))
        rNumber = ReceiptTextField(367f, 178f, receipt.number)
        val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        rDate = ReceiptTextField(472f, 178f,  dateFormatter.format(receipt.date))

        rReceivedFrom = ReceiptTextField(103f, 206f, receipt.giverName)
        rLiteralAmount = ReceiptTextField(119f, 244f, receipt.writtenAmount)
        rConcept = ReceiptTextField(105f, 283f, receipt.concept)

        rTotal = ReceiptTextField(108f, 344f, NumberFormat.getCurrencyInstance(locale).format(receipt.total))
        rOnAccount = ReceiptTextField(292f, 344f, NumberFormat.getCurrencyInstance(locale).format(receipt.onAccount))
        rBalance = ReceiptTextField(472f, 344f, NumberFormat.getCurrencyInstance(locale).format(receipt.balance))

        rGiverName = ReceiptTextField(105f, 428f, receipt.giverName)
        rGiverCI = ReceiptTextField(105f, 443f, receipt.giverCI)
        rReceiverName = ReceiptTextField(376f, 428f, receipt.receiverName)
        rReceiverCI = ReceiptTextField(376f, 443f, receipt.receiverCI)

        rTransactionType = receipt.transactionType
        super.invalidate()
    }

}
