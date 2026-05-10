package com.example.parceltrack

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.*

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "parceltrack.db"
        const val DATABASE_VERSION = 5

        const val TABLE_PARCELS = "parcels"
        const val COL_ID = "id"
        const val COL_TRACKING_NUMBER = "tracking_number"
        const val COL_RECEIVER = "receiver"
        const val COL_REGION = "region"
        const val COL_ZONE = "zone"
        const val COL_STATUS = "status"
        const val COL_CREATED_AT = "created_at"
        const val COL_AUTO_MODE = "auto_mode"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_PARCELS (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_TRACKING_NUMBER TEXT NOT NULL,
                $COL_RECEIVER TEXT NOT NULL,
                $COL_REGION TEXT NOT NULL,
                $COL_ZONE TEXT NOT NULL,
                $COL_STATUS TEXT NOT NULL,
                $COL_CREATED_AT TEXT NOT NULL,
                $COL_AUTO_MODE INTEGER NOT NULL DEFAULT 1
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PARCELS")
        onCreate(db)
    }

    // ⭐ 수동 더미 삽입 (메인 버튼에서 호출)
    // 어제(5/7) 53개 - 모두 배송 완료
    // 오늘(5/8) 38개 - 시간대별로 다양한 상태
    fun seedDummyData(): Int {
        val db = writableDatabase
        val cal = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        var insertedCount = 0
        val nowMs = System.currentTimeMillis()

        // ===========================================
        // 어제 (5월 7일) - 53개 / 24시간 다 지남 → 배송 완료
        // 시간대 : 06:00 ~ 22:00 사이에 분포
        // ===========================================
        val yesterdayList = listOf(
            DummyParcel("20250507001", "김철수", "서울시 강남구 테헤란로 123", "06:05"),
            DummyParcel("20250507002", "이영희", "경기도 성남시 분당구 정자동", "06:20"),
            DummyParcel("20250507003", "박민수", "부산광역시 해운대구 우동", "06:35"),
            DummyParcel("20250507004", "최지은", "대전광역시 유성구 봉명동", "06:50"),
            DummyParcel("20250507005", "정현우", "광주광역시 서구 치평동", "07:05"),
            DummyParcel("20250507006", "강서연", "강원도 춘천시 효자동", "07:20"),
            DummyParcel("20250507007", "윤도현", "제주특별자치도 제주시", "07:35"),
            DummyParcel("20250507008", "임수아", "인천광역시 연수구 송도동", "07:50"),
            DummyParcel("20250507009", "조영민", "서울시 마포구 합정동", "08:05"),
            DummyParcel("20250507010", "한지원", "경기도 수원시 영통구", "08:20"),
            DummyParcel("20250507011", "오세훈", "대구광역시 수성구 범어동", "08:35"),
            DummyParcel("20250507012", "장하늘", "충청북도 청주시 흥덕구", "08:50"),
            DummyParcel("20250507013", "송예린", "서울시 송파구 잠실동", "09:05"),
            DummyParcel("20250507014", "권혁준", "경기도 고양시 일산서구", "09:20"),
            DummyParcel("20250507015", "배수지", "울산광역시 남구 삼산동", "09:35"),
            DummyParcel("20250507016", "유재석", "충청남도 천안시 동남구", "09:50"),
            DummyParcel("20250507017", "신민아", "전라북도 전주시 완산구", "10:05"),
            DummyParcel("20250507018", "황민호", "전라남도 여수시 학동", "10:20"),
            DummyParcel("20250507019", "노영심", "경상북도 포항시 북구", "10:35"),
            DummyParcel("20250507020", "김연아", "서울시 종로구 사직동", "10:50"),
            DummyParcel("20250507021", "이승기", "경기도 용인시 수지구", "11:05"),
            DummyParcel("20250507022", "박서준", "서울시 강서구 화곡동", "11:20"),
            DummyParcel("20250507023", "최수영", "경기도 안양시 동안구", "11:35"),
            DummyParcel("20250507024", "정우성", "부산광역시 동래구 사직동", "11:50"),
            DummyParcel("20250507025", "강동원", "대구광역시 달서구 두류동", "12:05"),
            DummyParcel("20250507026", "손예진", "경기도 부천시 원미구", "12:20"),
            DummyParcel("20250507027", "현빈", "서울시 서초구 반포동", "12:35"),
            DummyParcel("20250507028", "전지현", "경상남도 창원시 의창구", "12:50"),
            DummyParcel("20250507029", "공유", "광주광역시 북구 두암동", "13:05"),
            DummyParcel("20250507030", "박보검", "충청북도 충주시 연수동", "13:20"),
            DummyParcel("20250507031", "수지", "경기도 평택시 비전동", "13:35"),
            DummyParcel("20250507032", "아이유", "서울시 영등포구 여의도동", "13:50"),
            DummyParcel("20250507033", "남주혁", "강원도 원주시 단계동", "14:05"),
            DummyParcel("20250507034", "한소희", "전라남도 목포시 상동", "14:20"),
            DummyParcel("20250507035", "차은우", "경기도 화성시 동탄", "14:35"),
            DummyParcel("20250507036", "김희선", "서울시 노원구 상계동", "14:50"),
            DummyParcel("20250507037", "김수현", "부산광역시 사하구 다대동", "15:05"),
            DummyParcel("20250507038", "이정재", "경기도 의정부시 가능동", "15:20"),
            DummyParcel("20250507039", "정해인", "대전광역시 대덕구 오정동", "15:35"),
            DummyParcel("20250507040", "박신혜", "충청남도 공주시 신관동", "15:50"),
            DummyParcel("20250507041", "이민호", "광주광역시 남구 봉선동", "16:05"),
            DummyParcel("20250507042", "고윤정", "경상남도 진주시 평거동", "16:20"),
            DummyParcel("20250507043", "변요한", "전라북도 익산시 영등동", "16:35"),
            DummyParcel("20250507044", "조정석", "강원도 속초시 교동", "16:50"),
            DummyParcel("20250507045", "임지연", "인천광역시 남동구 구월동", "17:05"),
            DummyParcel("20250507046", "유아인", "서울시 성동구 성수동", "17:20"),
            DummyParcel("20250507047", "조여정", "경기도 광명시 철산동", "17:35"),
            DummyParcel("20250507048", "이제훈", "대구광역시 북구 산격동", "17:50"),
            DummyParcel("20250507049", "한가인", "충청북도 제천시 청전동", "18:10"),
            DummyParcel("20250507050", "김혜수", "전라남도 순천시 조례동", "18:30"),
            DummyParcel("20250507051", "이성경", "경상북도 안동시 옥동", "19:00"),
            DummyParcel("20250507052", "정유미", "경기도 시흥시 정왕동", "19:30"),
            DummyParcel("20250507053", "이종석", "서울시 동작구 사당동", "20:00")
        )

        cal.set(2026, Calendar.MAY, 7)
        for (p in yesterdayList) {
            if (existsByTracking(db, p.tracking)) continue
            val (hour, minute) = p.time.split(":").map { it.toInt() }
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            cal.set(Calendar.SECOND, 0)
            insertDummy(db, p.tracking, p.receiver, p.region, df.format(cal.time))
            insertedCount++
        }

        // ===========================================
        // 오늘 (5월 8일) - 38개 / 시간 분포로 상태 다양
        // 현재 시간 기준 역으로 계산:
        //   - 24시간 이상 전 → 배송 완료 (불가, 오늘이므로)
        //   - 2~24시간 전 → 배송중
        //   - 30분~2시간 전 → 분류중
        //   - 30분 이내 → 접수 완료
        // ===========================================

        // 시간 분포 (현재 시간으로부터 N분 전):
        // - 접수 완료 10개: 0~25분 전
        // - 분류중    10개: 35~110분 전
        // - 배송중    18개: 130~1400분 전 (2시간~23시간)
        val todayList = listOf(
            // 접수 완료 (지금~25분 전, 30분 미만)
            DummyParcel("20250508001", "이재용", "서울시 강남구 역삼동", 2L),
            DummyParcel("20250508002", "정의선", "경기도 성남시 분당구", 5L),
            DummyParcel("20250508003", "이서진", "부산광역시 수영구 광안동", 8L),
            DummyParcel("20250508004", "김태희", "대전광역시 서구 둔산동", 11L),
            DummyParcel("20250508005", "정지소", "인천광역시 부평구 산곡동", 14L),
            DummyParcel("20250508006", "송중기", "경기도 안산시 단원구", 17L),
            DummyParcel("20250508007", "송혜교", "서울시 용산구 한남동", 20L),
            DummyParcel("20250508008", "고소영", "경기도 김포시 풍무동", 22L),
            DummyParcel("20250508009", "장동건", "강원도 강릉시 교동", 24L),
            DummyParcel("20250508010", "원빈", "충청남도 아산시 배방읍", 26L),

            // 분류중 (35분~110분 전, 30분~2시간)
            DummyParcel("20250508011", "조인성", "광주광역시 동구 학동", 35L),
            DummyParcel("20250508012", "이병헌", "경상북도 경주시 황성동", 42L),
            DummyParcel("20250508013", "김민희", "서울시 광진구 자양동", 50L),
            DummyParcel("20250508014", "권상우", "경기도 부천시 소사구", 58L),
            DummyParcel("20250508015", "김민정", "대구광역시 동구 신암동", 65L),
            DummyParcel("20250508016", "한효주", "충청남도 서산시 동문동", 75L),
            DummyParcel("20250508017", "김우빈", "전라북도 군산시 나운동", 85L),
            DummyParcel("20250508018", "이광수", "경상남도 김해시 내동", 95L),
            DummyParcel("20250508019", "박민영", "강원도 동해시 천곡동", 105L),
            DummyParcel("20250508020", "지창욱", "서울시 성북구 길음동", 112L),

            // 배송중 (130분~1400분 전, 2시간~23시간)
            DummyParcel("20250508021", "도경수", "경기도 이천시 부발읍", 135L),
            DummyParcel("20250508022", "박해진", "부산광역시 금정구 장전동", 160L),
            DummyParcel("20250508023", "김유정", "전라남도 광양시 중동", 200L),
            DummyParcel("20250508024", "옥택연", "서울시 양천구 목동", 240L),
            DummyParcel("20250508025", "박은빈", "대전광역시 중구 대흥동", 290L),
            DummyParcel("20250508026", "이종혁", "충청북도 음성군 음성읍", 340L),
            DummyParcel("20250508027", "김다미", "경기도 파주시 금촌동", 400L),
            DummyParcel("20250508028", "유연석", "광주광역시 광산구 운남동", 470L),
            DummyParcel("20250508029", "전종서", "경상북도 구미시 송정동", 540L),
            DummyParcel("20250508030", "이도현", "강원도 태백시 황지동", 620L),
            DummyParcel("20250508031", "안소희", "인천광역시 서구 검단", 700L),
            DummyParcel("20250508032", "이수혁", "전라북도 정읍시 시기동", 800L),
            DummyParcel("20250508033", "김새론", "서울시 은평구 응암동", 900L),
            DummyParcel("20250508034", "남궁민", "경상남도 양산시 물금읍", 1000L),
            DummyParcel("20250508035", "윤시윤", "대구광역시 서구 평리동", 1100L),
            DummyParcel("20250508036", "정소민", "경기도 동두천시 생연동", 1200L),
            DummyParcel("20250508037", "박형식", "충청남도 보령시 대천동", 1300L),
            DummyParcel("20250508038", "안효섭", "전라남도 나주시 빛가람동", 1400L)
        )

        for (p in todayList) {
            if (existsByTracking(db, p.tracking)) continue
            // 현재 시간 - p.minutesAgo 분
            val createdMs = nowMs - (p.minutesAgo!! * 60 * 1000)
            val createdAt = df.format(Date(createdMs))
            insertDummy(db, p.tracking, p.receiver, p.region, createdAt)
            insertedCount++
        }

        return insertedCount
    }

    private fun existsByTracking(db: SQLiteDatabase, tracking: String): Boolean {
        val cursor = db.query(
            TABLE_PARCELS, arrayOf(COL_ID),
            "$COL_TRACKING_NUMBER = ?", arrayOf(tracking),
            null, null, null
        )
        cursor.use { return it.count > 0 }
    }

    private fun insertDummy(
        db: SQLiteDatabase,
        tracking: String, receiver: String, region: String,
        createdAt: String
    ) {
        val zone = Parcel.classifyZone(region)
        val values = ContentValues().apply {
            put(COL_TRACKING_NUMBER, tracking)
            put(COL_RECEIVER, receiver)
            put(COL_REGION, region)
            put(COL_ZONE, zone)
            put(COL_STATUS, "접수 완료")
            put(COL_CREATED_AT, createdAt)
            put(COL_AUTO_MODE, 1)
        }
        db.insert(TABLE_PARCELS, null, values)
    }

    // 어제용 (시간 문자열) / 오늘용 (분 단위 과거) 둘 다 처리
    private data class DummyParcel(
        val tracking: String,
        val receiver: String,
        val region: String,
        val time: String = "",
        val minutesAgo: Long? = null
    ) {
        constructor(
            tracking: String, receiver: String, region: String, minutesAgo: Long
        ) : this(tracking, receiver, region, "", minutesAgo)
    }

    // ===== 이하 기존 함수들 =====

    fun insertParcel(parcel: Parcel): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_TRACKING_NUMBER, parcel.trackingNumber)
            put(COL_RECEIVER, parcel.receiver)
            put(COL_REGION, parcel.region)
            put(COL_ZONE, parcel.zone)
            put(COL_STATUS, parcel.status)
            put(COL_CREATED_AT, parcel.createdAt)
            put(COL_AUTO_MODE, parcel.autoMode)
        }
        return db.insert(TABLE_PARCELS, null, values)
    }

    fun getAllParcels(): List<Parcel> {
        autoProgressAll()
        val parcels = mutableListOf<Parcel>()
        val db = readableDatabase
        val cursor = db.query(TABLE_PARCELS, null, null, null, null, null, "$COL_CREATED_AT DESC")
        cursor.use {
            while (it.moveToNext()) {
                parcels.add(cursorToParcel(it))
            }
        }
        return parcels
    }

    fun searchParcels(query: String): List<Parcel> {
        autoProgressAll()
        val parcels = mutableListOf<Parcel>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_PARCELS, null,
            "$COL_TRACKING_NUMBER LIKE ? OR $COL_RECEIVER LIKE ? OR $COL_REGION LIKE ? OR $COL_ZONE LIKE ? OR $COL_STATUS LIKE ?",
            arrayOf("%$query%", "%$query%", "%$query%", "%$query%", "%$query%"),
            null, null, "$COL_CREATED_AT DESC"
        )
        cursor.use {
            while (it.moveToNext()) {
                parcels.add(cursorToParcel(it))
            }
        }
        return parcels
    }

    fun getParcelById(id: Int): Parcel? {
        autoProgressOne(id)
        val db = readableDatabase
        val cursor = db.query(
            TABLE_PARCELS, null,
            "$COL_ID = ?", arrayOf(id.toString()), null, null, null
        )
        cursor.use {
            if (it.moveToFirst()) return cursorToParcel(it)
        }
        return null
    }

    fun findByTrackingNumber(trackingNumber: String): Parcel? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_PARCELS, null,
            "$COL_TRACKING_NUMBER = ?", arrayOf(trackingNumber), null, null, null
        )
        cursor.use {
            if (it.moveToFirst()) {
                val parcel = cursorToParcel(it)
                autoProgressOne(parcel.id)
                return getParcelById(parcel.id)
            }
        }
        return null
    }

    fun updateStatus(id: Int, newStatus: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_STATUS, newStatus)
        }
        return db.update(TABLE_PARCELS, values, "$COL_ID = ?", arrayOf(id.toString()))
    }

    fun updateParcel(parcel: Parcel): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_TRACKING_NUMBER, parcel.trackingNumber)
            put(COL_RECEIVER, parcel.receiver)
            put(COL_REGION, parcel.region)
            put(COL_ZONE, parcel.zone)
            put(COL_STATUS, parcel.status)
        }
        return db.update(TABLE_PARCELS, values, "$COL_ID = ?", arrayOf(parcel.id.toString()))
    }

    fun deleteParcel(id: Int): Int {
        val db = writableDatabase
        return db.delete(TABLE_PARCELS, "$COL_ID = ?", arrayOf(id.toString()))
    }

    // ⭐ 자동 진행: 사용자가 수동으로 더 앞 단계로 바꿔놨다면 건드리지 않음
    private fun autoProgressAll() {
        val db = writableDatabase
        val cursor = db.query(
            TABLE_PARCELS,
            arrayOf(COL_ID, COL_CREATED_AT, COL_STATUS),
            null, null, null, null, null
        )
        cursor.use {
            while (it.moveToNext()) {
                val id = it.getInt(it.getColumnIndexOrThrow(COL_ID))
                val createdAt = it.getString(it.getColumnIndexOrThrow(COL_CREATED_AT))
                val currentStatus = it.getString(it.getColumnIndexOrThrow(COL_STATUS))
                val expected = Parcel.expectedStatus(createdAt)
                // 현재 상태보다 더 진행된 단계일 때만 갱신 (앞으로만 가기)
                if (Parcel.STATUS_LIST.indexOf(expected) > Parcel.STATUS_LIST.indexOf(currentStatus)) {
                    val values = ContentValues().apply { put(COL_STATUS, expected) }
                    db.update(TABLE_PARCELS, values, "$COL_ID = ?", arrayOf(id.toString()))
                }
            }
        }
    }

    private fun autoProgressOne(id: Int) {
        val db = writableDatabase
        val cursor = db.query(
            TABLE_PARCELS,
            arrayOf(COL_CREATED_AT, COL_STATUS),
            "$COL_ID = ?", arrayOf(id.toString()),
            null, null, null
        )
        cursor.use {
            if (it.moveToFirst()) {
                val createdAt = it.getString(it.getColumnIndexOrThrow(COL_CREATED_AT))
                val currentStatus = it.getString(it.getColumnIndexOrThrow(COL_STATUS))
                val expected = Parcel.expectedStatus(createdAt)
                // 현재 상태보다 더 진행된 단계일 때만 갱신
                if (Parcel.STATUS_LIST.indexOf(expected) > Parcel.STATUS_LIST.indexOf(currentStatus)) {
                    val values = ContentValues().apply { put(COL_STATUS, expected) }
                    db.update(TABLE_PARCELS, values, "$COL_ID = ?", arrayOf(id.toString()))
                }
            }
        }
    }

    private fun cursorToParcel(c: android.database.Cursor): Parcel {
        val autoModeIdx = c.getColumnIndex(COL_AUTO_MODE)
        val autoMode = if (autoModeIdx >= 0) c.getInt(autoModeIdx) else 1
        return Parcel(
            id = c.getInt(c.getColumnIndexOrThrow(COL_ID)),
            trackingNumber = c.getString(c.getColumnIndexOrThrow(COL_TRACKING_NUMBER)),
            receiver = c.getString(c.getColumnIndexOrThrow(COL_RECEIVER)),
            region = c.getString(c.getColumnIndexOrThrow(COL_REGION)),
            zone = c.getString(c.getColumnIndexOrThrow(COL_ZONE)),
            status = c.getString(c.getColumnIndexOrThrow(COL_STATUS)),
            createdAt = c.getString(c.getColumnIndexOrThrow(COL_CREATED_AT)),
            autoMode = autoMode
        )
    }
}