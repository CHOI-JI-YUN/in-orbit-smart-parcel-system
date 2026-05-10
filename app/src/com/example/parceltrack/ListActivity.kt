package com.example.parceltrack

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper
    private lateinit var adapter: ParcelAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var etSearch: EditText
    private lateinit var btnSearch: Button
    private lateinit var tvEmpty: TextView
    private lateinit var spinnerStatusFilter: Spinner

    private val handler = Handler(Looper.getMainLooper())

    private val refreshRunnable = object : Runnable {
        override fun run() {
            loadCurrentList()
            handler.postDelayed(this, 60 * 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        WindowCompat.setDecorFitsSystemWindows(window, true)
        window.statusBarColor = Color.parseColor("#4361EE")

        db = DatabaseHelper(this)

        recyclerView = findViewById(R.id.recyclerView)
        etSearch = findViewById(R.id.etSearch)
        btnSearch = findViewById(R.id.btnSearch)
        tvEmpty = findViewById(R.id.tvEmpty)
        spinnerStatusFilter = findViewById(R.id.spinnerStatusFilter)

        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ParcelAdapter(mutableListOf()) { parcel ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("PARCEL_ID", parcel.id)
            startActivity(intent)
        }

        recyclerView.adapter = adapter

        val filterList = mutableListOf("전체")
        filterList.addAll(Parcel.STATUS_LIST)

        val spinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            filterList
        )

        spinnerAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spinnerStatusFilter.adapter = spinnerAdapter

        spinnerStatusFilter.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    loadCurrentList()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        btnSearch.setOnClickListener {
            val query = etSearch.text.toString().trim()

            val list = if (query.isEmpty()) {
                db.getAllParcels()
            } else {
                db.searchParcels(query)
            }

            adapter.updateList(list)
            updateEmptyView(list.isEmpty())

            if (query.isNotEmpty() && list.isEmpty()) {
                Toast.makeText(this, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<Button>(R.id.btnStatistics).setOnClickListener {
            startActivity(Intent(this, StatisticsActivity::class.java))
        }

        findViewById<Button>(R.id.btnBack).setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        loadCurrentList()
        handler.post(refreshRunnable)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(refreshRunnable)
    }

    private fun loadCurrentList() {
        val selected = spinnerStatusFilter.selectedItem?.toString() ?: "전체"

        val list = if (selected == "전체") {
            db.getAllParcels()
        } else {
            db.getAllParcels().filter {
                it.status == selected
            }
        }

        adapter.updateList(list)
        updateEmptyView(list.isEmpty())
    }

    private fun updateEmptyView(isEmpty: Boolean) {
        tvEmpty.visibility = if (isEmpty) View.VISIBLE else View.GONE
        recyclerView.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }
}