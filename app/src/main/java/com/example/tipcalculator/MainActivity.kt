package com.example.tipcalculator

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG = "MainActivity"
private const val INITIAL_TIP = 15

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        seekBartip.progress = INITIAL_TIP
        tip_percentage.text = "$INITIAL_TIP%"
        updateTipPercent(INITIAL_TIP)
        seekBartip.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "onProgressChanged $progress")
                tip_percentage.text = "$progress%"
                updateTipPercent(progress)
                computeTipAndTotal()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        seekBar2.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "onProgressChanged $progress")
                split.text = "$progress"
                computeTipAndTotal()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        edbase.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "afterTextChanged $s")
                computeTipAndTotal()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun updateTipPercent(tipPercent: Int) {
        val tipDescription : String
        when (tipPercent) {
            in 0..9 -> tipDescription = "Poor"
            in 10..14 -> tipDescription = "Acceptable"
            in 15..19 -> tipDescription = "Good"
            in 20..24 -> tipDescription = "Great!"
            else -> tipDescription = "Amazing!!!"
        }
        goodbad.text = tipDescription
        val color = ArgbEvaluator().evaluate(tipPercent.toFloat() / seekBartip.max, ContextCompat.getColor(this, R.color.colorWorst), ContextCompat.getColor(this, R.color.colorBest)) as Int
        goodbad.setTextColor(color)
    }

    private fun computeTipAndTotal() {
        //get the value of the base and tip percentage
        if (edbase.text.toString().isEmpty()) {
            tipresult.text = ""
            totalresult.text = ""
            splitresult.text = ""
            return
        }
        val baseAmount = edbase.text.toString().toDouble()
        val tipPercent = seekBartip.progress
        val split_num = seekBar2.progress
        val tipAmount = baseAmount * tipPercent / 100
        val totalAmount = baseAmount + tipAmount
        val splitAmount = totalAmount / split_num
        tipresult.text = "%.2f".format(tipAmount)
        splitresult.text = "%.2f".format(splitAmount)
        totalresult.text = "%.2f".format(totalAmount)
    }
}
