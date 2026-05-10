package com.example.parceltrack

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 택배 등록 버튼
        findViewById<Button>(R.id.btnRegister).setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // 바코드 스캔 버튼
        findViewById<Button>(R.id.btnScan).setOnClickListener {
            startActivity(Intent(this, ScanActivity::class.java))
        }

        // 택배 목록 버튼
        findViewById<Button>(R.id.btnList).setOnClickListener {
            startActivity(Intent(this, ListActivity::class.java))
        }

        // ⭐ 테스트 데이터 채우기 버튼
        findViewById<Button>(R.id.btnSeedDummy).setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("테스트 데이터 채우기")
                .setMessage("어제(5/7) 35개 + 오늘(5/8) 12개 = 총 47개의 더미 택배를 추가합니다.\n\n기존 데이터는 유지되고, 송장번호가 중복된 것만 건너뜁니다.")
                .setPositiveButton("추가") { _, _ ->
                    val db = DatabaseHelper(this)
                    val count = db.seedDummyData()
                    Toast.makeText(this, "${count}개의 테스트 데이터가 추가되었습니다", Toast.LENGTH_LONG).show()
                }
                .setNegativeButton("취소", null)
                .show()
        }
    }
}