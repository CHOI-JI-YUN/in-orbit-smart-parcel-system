package com.example.parceltrack

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

class StatisticsActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper
    private lateinit var tvSelectedDate: TextView

    private var selectedDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        db = DatabaseHelper(this)

        tvSelectedDate = findViewById(R.id.tvSelectedDate)

        val today = SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.getDefault()
        ).format(Calendar.getInstance().time)

        selectedDate = today
        tvSelectedDate.text = selectedDate

        loadStatistics(selectedDate)

        findViewById<TextView>(R.id.btnSelectDate).setOnClickListener {
            showDatePicker()
        }

        findViewById<TextView>(R.id.btnBack).setOnClickListener {
            finish()
        }
    }

    private fun showDatePicker() {

        val calendar = Calendar.getInstance()

        DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->

                selectedDate = String.format(
                    Locale.getDefault(),
                    "%04d-%02d-%02d",
                    year,
                    month + 1,
                    dayOfMonth
                )

                tvSelectedDate.text = selectedDate

                loadStatistics(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun loadStatistics(date: String) {

        val allParcels = db.getAllParcels()

        // ⭐ 수정: 날짜+시간 형식("2026-05-08 14:30:00")에서 날짜 부분만 비교
        val dateParcels = allParcels.filter {
            it.createdAt.startsWith(date)
        }

        // 상태별 통계
        val total = dateParcels.size
        val ready = dateParcels.count {
            it.status == "접수 완료"
        }

        val sorting = dateParcels.count {
            it.status == "분류중"
        }

        val shipping = dateParcels.count {
            it.status == "배송중"
        }

        val complete = dateParcels.count {
            it.status == "배송 완료"
        }

        // 구역별 통계
        val zoneA = dateParcels.count {
            it.zone == "A구역"
        }

        val zoneB = dateParcels.count {
            it.zone == "B구역"
        }

        val zoneC = dateParcels.count {
            it.zone == "C구역"
        }

        val zoneD = dateParcels.count {
            it.zone == "D구역"
        }

        val zoneE = dateParcels.count {
            it.zone == "E구역"
        }

        val zoneF = dateParcels.count {
            it.zone == "F구역"
        }

        // 상태 표시
        findViewById<TextView>(R.id.tvTotal).text =
            total.toString()

        findViewById<TextView>(R.id.tvReady).text =
            ready.toString()

        findViewById<TextView>(R.id.tvSorting).text =
            sorting.toString()

        findViewById<TextView>(R.id.tvShipping).text =
            shipping.toString()

        findViewById<TextView>(R.id.tvComplete).text =
            complete.toString()

        // 구역 표시
        findViewById<TextView>(R.id.tvZoneA).text =
            "A구역: ${zoneA}건"

        findViewById<TextView>(R.id.tvZoneB).text =
            "B구역: ${zoneB}건"

        findViewById<TextView>(R.id.tvZoneC).text =
            "C구역: ${zoneC}건"

        findViewById<TextView>(R.id.tvZoneD).text =
            "D구역: ${zoneD}건"

        findViewById<TextView>(R.id.tvZoneE).text =
            "E구역: ${zoneE}건"

        findViewById<TextView>(R.id.tvZoneF).text =
            "F구역: ${zoneF}건"

        val barChartZone = findViewById<BarChart>(R.id.barChartZone)

        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(0f, zoneA.toFloat()))
        entries.add(BarEntry(1f, zoneB.toFloat()))
        entries.add(BarEntry(2f, zoneC.toFloat()))
        entries.add(BarEntry(3f, zoneD.toFloat()))
        entries.add(BarEntry(4f, zoneE.toFloat()))
        entries.add(BarEntry(5f, zoneF.toFloat()))

        val dataSet = BarDataSet(entries, "구역별 택배 수")
        val barData = BarData(dataSet)

        barChartZone.data = barData
        barChartZone.description.isEnabled = false
        barChartZone.animateY(800)
        barChartZone.invalidate()
    }
}