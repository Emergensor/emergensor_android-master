package team_emergensor.co.jp.emergensor.presentation.analysys

import android.arch.lifecycle.ViewModelProviders
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.graphics.DashPathEffect
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import team_emergensor.co.jp.emergensor.R
import team_emergensor.co.jp.emergensor.databinding.FragmentAnalysysBinding
import team_emergensor.co.jp.emergensor.service.acceleration.AccelerationSensorService


class AnalysysFragment : Fragment() {

    private lateinit var binding: FragmentAnalysysBinding


    private val viewModel by lazy {
        ViewModelProviders.of(this).get(AnalysysViewModel::class.java)
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val actionName = intent?.getStringExtra(AccelerationSensorService.ACTION_KEY)
            if (actionName != null) {
                viewModel.description.postValue("current action: ")
                viewModel.actionName.postValue(actionName)
            }
            val fftData = intent?.getDoubleArrayExtra(AccelerationSensorService.FFT_KEY)
            if (fftData != null) {
                setData(fftData)
            }
        }
    }

    private val filter = IntentFilter(AccelerationSensorService.INTENT_ACTION_UPLOAD)

    override fun onStart() {
        super.onStart()
        if (context != null) {
            LocalBroadcastManager.getInstance(context!!).registerReceiver(receiver, filter)
        }
    }

    override fun onPause() {
        super.onPause()
        if (context != null) {
            LocalBroadcastManager.getInstance(context!!).unregisterReceiver(receiver)
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_analysys, container, false)
        // Inflate the layout for this fragment
        binding.viewModel = viewModel
        val chart = binding.barChart
        chart.apply {
            isDragEnabled = false
            setTouchEnabled(false)
            setScaleEnabled(false)
            setDrawGridBackground(false)
            val xl = chart.xAxis
            xl.position = XAxis.XAxisPosition.BOTTOM
            xl.textColor = Color.BLACK
            xl.mAxisMinimum = 0f
            xl.mAxisMaximum = 5f
            xl.setDrawGridLines(false)
            val leftAxis = chart.axisLeft
            leftAxis.textColor = context?.resources?.getColor(R.color.transparent) ?: Color.BLACK
            leftAxis.setStartAtZero(true)
            leftAxis.isEnabled = false
            leftAxis.setDrawTopYLabelEntry(false)
            leftAxis.setDrawGridLines(false)
            val rightAxis = chart.axisRight
            rightAxis.isEnabled = false
            rightAxis.setDrawTopYLabelEntry(false)
            rightAxis.setDrawGridLines(false)
        }
        binding.setLifecycleOwner(this)
        return binding.root
    }

    private fun setData(data: DoubleArray) {
        val values = arrayListOf<BarEntry>()
        val mChart = binding.barChart

        (0 until data.size).mapTo(values) { BarEntry(it.toFloat(), data[it].toFloat(), null, null) }

        val set1: BarDataSet

        if (mChart.data != null && mChart.data.dataSetCount > 0) {
            (mChart.data.getDataSetByIndex(0) as BarDataSet).values = values
            mChart.data.notifyDataChanged()
            mChart.notifyDataSetChanged()
        } else {
            // create a dataset and give it a type
            set1 = BarDataSet(values, null)

            set1.setDrawIcons(false)
            set1.color = context?.resources?.getColor(R.color.orange) ?: Color.BLACK
            set1.valueTextSize = 0f
            set1.formLineWidth = 1f
            set1.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
            set1.formSize = 15f

            val dataSets = ArrayList<IBarDataSet>()
            dataSets.add(set1) // add the datasets

            // create a data object with the datasets
            val lineData = BarData(dataSets)

            // set data
            mChart.data = lineData
            mChart.data.notifyDataChanged()
            mChart.notifyDataSetChanged()
        }
        mChart.invalidate()
    }

}
