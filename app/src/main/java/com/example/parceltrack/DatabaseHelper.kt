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
        const val DATABASE_VERSION = 8

        const val TABLE_PARCELS = "parcels"
        const val COL_ID = "id"
        const val COL_TRACKING_NUMBER = "tracking_number"
        const val COL_RECEIVER = "receiver"
        const val COL_REGION = "region"
        const val COL_ZONE = "zone"
        const val COL_STATUS = "status"
        const val COL_CREATED_AT = "created_at"
        const val COL_AUTO_MODE = "auto_mode"
        const val COL_SORTING_AT = "sorting_at"
        const val COL_SHIPPING_AT = "shipping_at"
        const val COL_COMPLETE_AT = "complete_at"
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
                $COL_AUTO_MODE INTEGER NOT NULL DEFAULT 1,
                $COL_SORTING_AT TEXT NOT NULL DEFAULT '',
                $COL_SHIPPING_AT TEXT NOT NULL DEFAULT '',
                $COL_COMPLETE_AT TEXT NOT NULL DEFAULT ''
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PARCELS")
        onCreate(db)
    }

    // ===== 더미 데이터 =====

    fun seedDummyData(): Int {
        val db = writableDatabase
        val cal = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        var insertedCount = 0

        // ============ 5월 7일 - 53개 ============
        val may07List = listOf(
            DummyParcel("20260507001", "김철수", "서울시 강남구 테헤란로 123", "06:05"),
            DummyParcel("20260507002", "이영희", "경기도 성남시 분당구 정자동", "06:20"),
            DummyParcel("20260507003", "박민수", "부산광역시 해운대구 우동", "06:35"),
            DummyParcel("20260507004", "최지은", "대전광역시 유성구 봉명동", "06:50"),
            DummyParcel("20260507005", "정현우", "광주광역시 서구 치평동", "07:05"),
            DummyParcel("20260507006", "강서연", "강원도 춘천시 효자동", "07:20"),
            DummyParcel("20260507007", "윤도현", "제주특별자치도 제주시", "07:35"),
            DummyParcel("20260507008", "임수아", "인천광역시 연수구 송도동", "07:50"),
            DummyParcel("20260507009", "조영민", "서울시 마포구 합정동", "08:05"),
            DummyParcel("20260507010", "한지원", "경기도 수원시 영통구", "08:20"),
            DummyParcel("20260507011", "오세훈", "대구광역시 수성구 범어동", "08:35"),
            DummyParcel("20260507012", "장하늘", "충청북도 청주시 흥덕구", "08:50"),
            DummyParcel("20260507013", "송예린", "서울시 송파구 잠실동", "09:05"),
            DummyParcel("20260507014", "권혁준", "경기도 고양시 일산서구", "09:20"),
            DummyParcel("20260507015", "배수지", "울산광역시 남구 삼산동", "09:35"),
            DummyParcel("20260507016", "유재석", "충청남도 천안시 동남구", "09:50"),
            DummyParcel("20260507017", "신민아", "전라북도 전주시 완산구", "10:05"),
            DummyParcel("20260507018", "황민호", "전라남도 여수시 학동", "10:20"),
            DummyParcel("20260507019", "노영심", "경상북도 포항시 북구", "10:35"),
            DummyParcel("20260507020", "김연아", "서울시 종로구 사직동", "10:50"),
            DummyParcel("20260507021", "이승기", "경기도 용인시 수지구", "11:05"),
            DummyParcel("20260507022", "박서준", "서울시 강서구 화곡동", "11:20"),
            DummyParcel("20260507023", "최수영", "경기도 안양시 동안구", "11:35"),
            DummyParcel("20260507024", "정우성", "부산광역시 동래구 사직동", "11:50"),
            DummyParcel("20260507025", "강동원", "대구광역시 달서구 두류동", "12:05"),
            DummyParcel("20260507026", "손예진", "경기도 부천시 원미구", "12:20"),
            DummyParcel("20260507027", "현빈", "서울시 서초구 반포동", "12:35"),
            DummyParcel("20260507028", "전지현", "경상남도 창원시 의창구", "12:50"),
            DummyParcel("20260507029", "공유", "광주광역시 북구 두암동", "13:05"),
            DummyParcel("20260507030", "박보검", "충청북도 충주시 연수동", "13:20"),
            DummyParcel("20260507031", "수지", "경기도 평택시 비전동", "13:35"),
            DummyParcel("20260507032", "아이유", "서울시 영등포구 여의도동", "13:50"),
            DummyParcel("20260507033", "남주혁", "강원도 원주시 단계동", "14:05"),
            DummyParcel("20260507034", "한소희", "전라남도 목포시 상동", "14:20"),
            DummyParcel("20260507035", "차은우", "경기도 화성시 동탄", "14:35"),
            DummyParcel("20260507036", "김희선", "서울시 노원구 상계동", "14:50"),
            DummyParcel("20260507037", "김수현", "부산광역시 사하구 다대동", "15:05"),
            DummyParcel("20260507038", "이정재", "경기도 의정부시 가능동", "15:20"),
            DummyParcel("20260507039", "정해인", "대전광역시 대덕구 오정동", "15:35"),
            DummyParcel("20260507040", "박신혜", "충청남도 공주시 신관동", "15:50"),
            DummyParcel("20260507041", "이민호", "광주광역시 남구 봉선동", "16:05"),
            DummyParcel("20260507042", "고윤정", "경상남도 진주시 평거동", "16:20"),
            DummyParcel("20260507043", "변요한", "전라북도 익산시 영등동", "16:35"),
            DummyParcel("20260507044", "조정석", "강원도 속초시 교동", "16:50"),
            DummyParcel("20260507045", "임지연", "인천광역시 남동구 구월동", "17:05"),
            DummyParcel("20260507046", "유아인", "서울시 성동구 성수동", "17:20"),
            DummyParcel("20260507047", "조여정", "경기도 광명시 철산동", "17:35"),
            DummyParcel("20260507048", "이제훈", "대구광역시 북구 산격동", "17:50"),
            DummyParcel("20260507049", "한가인", "충청북도 제천시 청전동", "18:10"),
            DummyParcel("20260507050", "김혜수", "전라남도 순천시 조례동", "18:30"),
            DummyParcel("20260507051", "이성경", "경상북도 안동시 옥동", "19:00"),
            DummyParcel("20260507052", "정유미", "경기도 시흥시 정왕동", "19:30"),
            DummyParcel("20260507053", "이종석", "서울시 동작구 사당동", "20:00")
        )
        cal.set(2026, Calendar.MAY, 7)
        for (p in may07List) {
            if (existsByTracking(db, p.tracking)) continue
            val (h, m) = p.time.split(":").map { it.toInt() }
            cal.set(Calendar.HOUR_OF_DAY, h); cal.set(Calendar.MINUTE, m); cal.set(Calendar.SECOND, 0)
            insertDummy(db, p.tracking, p.receiver, p.region, df.format(cal.time))
            insertedCount++
        }

        // ============ 5월 8일 - 38개 (고정 날짜) ============
        val may08List = listOf(
            DummyParcel("20260508001", "이재용", "서울시 강남구 역삼동", "06:10"),
            DummyParcel("20260508002", "정의선", "경기도 성남시 분당구", "06:25"),
            DummyParcel("20260508003", "이서진", "부산광역시 수영구 광안동", "06:40"),
            DummyParcel("20260508004", "김태희", "대전광역시 서구 둔산동", "06:55"),
            DummyParcel("20260508005", "정지소", "인천광역시 부평구 산곡동", "07:10"),
            DummyParcel("20260508006", "송중기", "경기도 안산시 단원구", "07:25"),
            DummyParcel("20260508007", "송혜교", "서울시 용산구 한남동", "07:40"),
            DummyParcel("20260508008", "고소영", "경기도 김포시 풍무동", "07:55"),
            DummyParcel("20260508009", "장동건", "강원도 강릉시 교동", "08:10"),
            DummyParcel("20260508010", "원빈", "충청남도 아산시 배방읍", "08:25"),
            DummyParcel("20260508011", "조인성", "광주광역시 동구 학동", "08:40"),
            DummyParcel("20260508012", "이병헌", "경상북도 경주시 황성동", "08:55"),
            DummyParcel("20260508013", "김민희", "서울시 광진구 자양동", "09:10"),
            DummyParcel("20260508014", "권상우", "경기도 부천시 소사구", "09:25"),
            DummyParcel("20260508015", "김민정", "대구광역시 동구 신암동", "09:40"),
            DummyParcel("20260508016", "한효주", "충청남도 서산시 동문동", "09:55"),
            DummyParcel("20260508017", "김우빈", "전라북도 군산시 나운동", "10:10"),
            DummyParcel("20260508018", "이광수", "경상남도 김해시 내동", "10:25"),
            DummyParcel("20260508019", "박민영", "강원도 동해시 천곡동", "10:40"),
            DummyParcel("20260508020", "지창욱", "서울시 성북구 길음동", "10:55"),
            DummyParcel("20260508021", "도경수", "경기도 이천시 부발읍", "11:10"),
            DummyParcel("20260508022", "박해진", "부산광역시 금정구 장전동", "11:25"),
            DummyParcel("20260508023", "김유정", "전라남도 광양시 중동", "11:40"),
            DummyParcel("20260508024", "옥택연", "서울시 양천구 목동", "11:55"),
            DummyParcel("20260508025", "박은빈", "대전광역시 중구 대흥동", "12:10"),
            DummyParcel("20260508026", "이종혁", "충청북도 음성군 음성읍", "12:30"),
            DummyParcel("20260508027", "김다미", "경기도 파주시 금촌동", "13:00"),
            DummyParcel("20260508028", "유연석", "광주광역시 광산구 운남동", "13:30"),
            DummyParcel("20260508029", "전종서", "경상북도 구미시 송정동", "14:00"),
            DummyParcel("20260508030", "이도현", "강원도 태백시 황지동", "14:30"),
            DummyParcel("20260508031", "안소희", "인천광역시 서구 검단", "15:00"),
            DummyParcel("20260508032", "이수혁", "전라북도 정읍시 시기동", "15:30"),
            DummyParcel("20260508033", "김새론", "서울시 은평구 응암동", "16:00"),
            DummyParcel("20260508034", "남궁민", "경상남도 양산시 물금읍", "16:30"),
            DummyParcel("20260508035", "윤시윤", "대구광역시 서구 평리동", "17:00"),
            DummyParcel("20260508036", "정소민", "경기도 동두천시 생연동", "17:30"),
            DummyParcel("20260508037", "박형식", "충청남도 보령시 대천동", "18:00"),
            DummyParcel("20260508038", "안효섭", "전라남도 나주시 빛가람동", "18:30")
        )
        cal.set(2026, Calendar.MAY, 8)
        for (p in may08List) {
            if (existsByTracking(db, p.tracking)) continue
            val (h, m) = p.time.split(":").map { it.toInt() }
            cal.set(Calendar.HOUR_OF_DAY, h); cal.set(Calendar.MINUTE, m); cal.set(Calendar.SECOND, 0)
            insertDummy(db, p.tracking, p.receiver, p.region, df.format(cal.time))
            insertedCount++
        }

        // ============ 5월 27일 - 51개 ============
        val may27List = listOf(
            DummyParcel("20260527001", "김민준", "서울시 강남구 논현동", "06:10"),
            DummyParcel("20260527002", "이서아", "경기도 성남시 중원구", "06:25"),
            DummyParcel("20260527003", "박지훈", "부산광역시 동구 범일동", "06:40"),
            DummyParcel("20260527004", "최예나", "대전광역시 동구 원동", "06:55"),
            DummyParcel("20260527005", "정다은", "광주광역시 서구 금호동", "07:10"),
            DummyParcel("20260527006", "강태양", "강원도 춘천시 석사동", "07:25"),
            DummyParcel("20260527007", "윤채원", "제주특별자치도 서귀포시", "07:40"),
            DummyParcel("20260527008", "임현수", "인천광역시 계양구 계산동", "07:55"),
            DummyParcel("20260527009", "조성민", "서울시 강동구 천호동", "08:10"),
            DummyParcel("20260527010", "한수빈", "경기도 하남시 덕풍동", "08:25"),
            DummyParcel("20260527011", "오지호", "대구광역시 북구 칠성동", "08:40"),
            DummyParcel("20260527012", "장유진", "충청북도 청주시 상당구", "08:55"),
            DummyParcel("20260527013", "송민아", "서울시 중랑구 면목동", "09:10"),
            DummyParcel("20260527014", "권도윤", "경기도 남양주시 별내동", "09:25"),
            DummyParcel("20260527015", "배나연", "울산광역시 동구 전하동", "09:40"),
            DummyParcel("20260527016", "유하준", "충청남도 홍성군 홍성읍", "09:55"),
            DummyParcel("20260527017", "신지원", "전라북도 완주군 삼례읍", "10:10"),
            DummyParcel("20260527018", "황서연", "전라남도 보성군 보성읍", "10:25"),
            DummyParcel("20260527019", "노태준", "경상북도 영주시 휴천동", "10:40"),
            DummyParcel("20260527020", "김소율", "서울시 도봉구 창동", "10:55"),
            DummyParcel("20260527021", "이준영", "경기도 광주시 경안동", "11:10"),
            DummyParcel("20260527022", "박하윤", "서울시 관악구 봉천동", "11:25"),
            DummyParcel("20260527023", "최시우", "경기도 군포시 산본동", "11:40"),
            DummyParcel("20260527024", "정재원", "부산광역시 북구 화명동", "11:55"),
            DummyParcel("20260527025", "강유나", "대구광역시 수성구 만촌동", "12:10"),
            DummyParcel("20260527026", "손지민", "경기도 오산시 원동", "12:25"),
            DummyParcel("20260527027", "현태양", "서울시 은평구 불광동", "12:40"),
            DummyParcel("20260527028", "전주원", "경상남도 통영시 무전동", "12:55"),
            DummyParcel("20260527029", "공민재", "광주광역시 남구 주월동", "13:10"),
            DummyParcel("20260527030", "박수현", "충청북도 진천군 진천읍", "13:25"),
            DummyParcel("20260527031", "수민지", "경기도 포천시 소흘읍", "13:40"),
            DummyParcel("20260527032", "아준서", "서울시 강북구 번동", "13:55"),
            DummyParcel("20260527033", "남다현", "강원도 홍천군 홍천읍", "14:10"),
            DummyParcel("20260527034", "한예진", "전라남도 광양시 광양읍", "14:25"),
            DummyParcel("20260527035", "차도현", "경기도 양주시 덕계동", "14:40"),
            DummyParcel("20260527036", "김희진", "서울시 금천구 가산동", "14:55"),
            DummyParcel("20260527037", "김우진", "부산광역시 연제구 연산동", "15:10"),
            DummyParcel("20260527038", "이채린", "경기도 구리시 인창동", "15:25"),
            DummyParcel("20260527039", "정하은", "대전광역시 유성구 전민동", "15:40"),
            DummyParcel("20260527040", "박진우", "충청남도 당진시 당진1동", "15:55"),
            DummyParcel("20260527041", "이수민", "광주광역시 광산구 월계동", "16:10"),
            DummyParcel("20260527042", "고은서", "경상남도 밀양시 내이동", "16:25"),
            DummyParcel("20260527043", "변준혁", "전라북도 남원시 왕정동", "16:40"),
            DummyParcel("20260527044", "조하린", "강원도 삼척시 남양동", "16:55"),
            DummyParcel("20260527045", "임도연", "인천광역시 강화군 강화읍", "17:10"),
            DummyParcel("20260527046", "유선재", "서울시 중구 신당동", "17:25"),
            DummyParcel("20260527047", "조예슬", "경기도 의왕시 내손동", "17:40"),
            DummyParcel("20260527048", "이재민", "대구광역시 달성군 화원읍", "17:55"),
            DummyParcel("20260527049", "한별이", "충청북도 옥천군 옥천읍", "18:15"),
            DummyParcel("20260527050", "김혜린", "전라남도 담양군 담양읍", "18:35"),
            DummyParcel("20260527051", "이성민", "경상북도 영천시 완산동", "19:05")
        )
        cal.set(2026, Calendar.MAY, 27)
        for (p in may27List) {
            if (existsByTracking(db, p.tracking)) continue
            val (h, m) = p.time.split(":").map { it.toInt() }
            cal.set(Calendar.HOUR_OF_DAY, h); cal.set(Calendar.MINUTE, m); cal.set(Calendar.SECOND, 0)
            insertDummy(db, p.tracking, p.receiver, p.region, df.format(cal.time))
            insertedCount++
        }

        // ============ 5월 28일 - 47개 ============
        val may28List = listOf(
            DummyParcel("20260528001", "김도윤", "서울시 서대문구 홍제동", "06:15"),
            DummyParcel("20260528002", "이하린", "경기도 용인시 처인구", "06:30"),
            DummyParcel("20260528003", "박준서", "부산광역시 중구 광복동", "06:45"),
            DummyParcel("20260528004", "최나연", "대전광역시 서구 탄방동", "07:00"),
            DummyParcel("20260528005", "정민호", "광주광역시 북구 오치동", "07:15"),
            DummyParcel("20260528006", "강하늘이", "강원도 원주시 무실동", "07:30"),
            DummyParcel("20260528007", "윤지유", "제주특별자치도 제주시 이도동", "07:45"),
            DummyParcel("20260528008", "임태양", "인천광역시 동구 금창동", "08:00"),
            DummyParcel("20260528009", "조민준", "서울시 강서구 등촌동", "08:15"),
            DummyParcel("20260528010", "한가람", "경기도 안성시 공도읍", "08:30"),
            DummyParcel("20260528011", "오수아", "대구광역시 달서구 본리동", "08:45"),
            DummyParcel("20260528012", "장태준", "충청북도 증평군 증평읍", "09:00"),
            DummyParcel("20260528013", "송지아", "서울시 노원구 월계동", "09:15"),
            DummyParcel("20260528014", "권유준", "경기도 여주시 여주1동", "09:30"),
            DummyParcel("20260528015", "배소연", "울산광역시 울주군 언양읍", "09:45"),
            DummyParcel("20260528016", "유지원", "충청남도 논산시 취암동", "10:00"),
            DummyParcel("20260528017", "신태호", "전라북도 정읍시 연지동", "10:15"),
            DummyParcel("20260528018", "황다은", "전라남도 해남군 해남읍", "10:30"),
            DummyParcel("20260528019", "노주원", "경상북도 상주시 만산동", "10:45"),
            DummyParcel("20260528020", "김루나", "서울시 마포구 망원동", "11:00"),
            DummyParcel("20260528021", "이민재", "경기도 과천시 중앙동", "11:15"),
            DummyParcel("20260528022", "박소율", "서울시 동대문구 전농동", "11:30"),
            DummyParcel("20260528023", "최진우", "경기도 연천군 연천읍", "11:45"),
            DummyParcel("20260528024", "정서윤", "부산광역시 기장군 기장읍", "12:00"),
            DummyParcel("20260528025", "강민서", "대구광역시 동구 신서동", "12:15"),
            DummyParcel("20260528026", "손하준", "경기도 가평군 가평읍", "12:30"),
            DummyParcel("20260528027", "현도연", "서울시 종로구 창신동", "12:45"),
            DummyParcel("20260528028", "전유나", "경상남도 거제시 고현동", "13:00"),
            DummyParcel("20260528029", "공서준", "광주광역시 동구 소태동", "13:15"),
            DummyParcel("20260528030", "박채원", "충청북도 보은군 보은읍", "13:30"),
            DummyParcel("20260528031", "수예림", "경기도 양평군 양평읍", "13:45"),
            DummyParcel("20260528032", "아지수", "서울시 서초구 방배동", "14:00"),
            DummyParcel("20260528033", "남현준", "강원도 평창군 평창읍", "14:15"),
            DummyParcel("20260528034", "한소율", "전라남도 장흥군 장흥읍", "14:30"),
            DummyParcel("20260528035", "차수민", "경기도 양주시 회천동", "14:45"),
            DummyParcel("20260528036", "김나희", "서울시 강남구 개포동", "15:00"),
            DummyParcel("20260528037", "김준혁", "부산광역시 강서구 명지동", "15:15"),
            DummyParcel("20260528038", "이예지", "경기도 시흥시 배곧동", "15:30"),
            DummyParcel("20260528039", "정현서", "대전광역시 동구 가양동", "15:45"),
            DummyParcel("20260528040", "박도현", "충청남도 예산군 예산읍", "16:00"),
            DummyParcel("20260528041", "이서준", "광주광역시 서구 쌍촌동", "16:15"),
            DummyParcel("20260528042", "고지훈", "경상남도 하동군 하동읍", "16:30"),
            DummyParcel("20260528043", "변서아", "전라북도 임실군 임실읍", "16:45"),
            DummyParcel("20260528044", "조태양", "강원도 철원군 철원읍", "17:00"),
            DummyParcel("20260528045", "임나래", "인천광역시 옹진군 북도면", "17:15"),
            DummyParcel("20260528046", "유가온", "서울시 성동구 옥수동", "17:30"),
            DummyParcel("20260528047", "조수빈", "경기도 가평군 청평면", "17:45")
        )
        cal.set(2026, Calendar.MAY, 28)
        for (p in may28List) {
            if (existsByTracking(db, p.tracking)) continue
            val (h, m) = p.time.split(":").map { it.toInt() }
            cal.set(Calendar.HOUR_OF_DAY, h); cal.set(Calendar.MINUTE, m); cal.set(Calendar.SECOND, 0)
            insertDummy(db, p.tracking, p.receiver, p.region, df.format(cal.time))
            insertedCount++
        }

        // ============ 5월 29일 - 51개 (고정 날짜, 배송 완료 될 것) ============
        val may29List = listOf(
            DummyParcel("20260529001", "김서준", "서울시 강남구 삼성동", "06:00"),
            DummyParcel("20260529002", "이주아", "경기도 성남시 수정구", "06:12"),
            DummyParcel("20260529003", "박민서", "부산광역시 남구 대연동", "06:24"),
            DummyParcel("20260529004", "최하준", "대전광역시 중구 선화동", "06:36"),
            DummyParcel("20260529005", "정지안", "광주광역시 광산구 첨단동", "06:48"),
            DummyParcel("20260529006", "강유진", "강원도 춘천시 후평동", "07:00"),
            DummyParcel("20260529007", "윤서율", "인천광역시 남동구 논현동", "07:10"),
            DummyParcel("20260529008", "임가현", "서울시 마포구 상암동", "07:20"),
            DummyParcel("20260529009", "조태민", "경기도 수원시 장안구", "07:30"),
            DummyParcel("20260529010", "한도윤", "대구광역시 중구 동성로", "07:40"),
            DummyParcel("20260529011", "오나영", "충청북도 청주시 서원구", "07:50"),
            DummyParcel("20260529012", "장수빈", "서울시 송파구 문정동", "08:00"),
            DummyParcel("20260529013", "송채원", "경기도 고양시 덕양구", "08:08"),
            DummyParcel("20260529014", "권지호", "울산광역시 중구 학산동", "08:16"),
            DummyParcel("20260529015", "배유나", "충청남도 천안시 서북구", "08:24"),
            DummyParcel("20260529016", "유진서", "전라북도 전주시 덕진구", "08:32"),
            DummyParcel("20260529017", "신하윤", "전라남도 여수시 돌산읍", "08:40"),
            DummyParcel("20260529018", "황도현", "경상북도 포항시 남구", "08:48"),
            DummyParcel("20260529019", "노예슬", "서울시 종로구 평창동", "08:56"),
            DummyParcel("20260529020", "김태윤", "경기도 용인시 기흥구", "09:05"),
            DummyParcel("20260529021", "이나현", "서울시 강서구 방화동", "09:15"),
            DummyParcel("20260529022", "박준영", "경기도 안양시 만안구", "09:25"),
            DummyParcel("20260529023", "최서아", "부산광역시 사상구 학장동", "09:35"),
            DummyParcel("20260529024", "정민재", "대구광역시 달서구 감삼동", "09:50"),
            DummyParcel("20260529025", "강하윤", "광주광역시 동구 산수동", "10:05"),
            DummyParcel("20260529026", "손유빈", "경기도 부천시 중동", "10:20"),
            DummyParcel("20260529027", "현지수", "서울시 서초구 잠원동", "10:35"),
            DummyParcel("20260529028", "전도영", "경상남도 창원시 마산합포구", "10:50"),
            DummyParcel("20260529029", "공서현", "충청북도 충주시 교현동", "11:05"),
            DummyParcel("20260529030", "박하진", "강원도 원주시 행구동", "11:20"),
            DummyParcel("20260529031", "김도현", "서울시 영등포구 당산동", "11:40"),
            DummyParcel("20260529032", "이지안", "경기도 화성시 봉담읍", "12:00"),
            DummyParcel("20260529033", "남유진", "인천광역시 서구 청라동", "12:20"),
            DummyParcel("20260529034", "한예나", "전라남도 목포시 용당동", "12:40"),
            DummyParcel("20260529035", "차민호", "경기도 광명시 하안동", "13:00"),
            DummyParcel("20260529036", "김시은", "서울시 노원구 중계동", "13:20"),
            DummyParcel("20260529037", "이도영", "부산광역시 금정구 서동", "13:40"),
            DummyParcel("20260529038", "정채린", "경기도 의정부시 호원동", "14:00"),
            DummyParcel("20260529039", "박태준", "대전광역시 유성구 관평동", "14:20"),
            DummyParcel("20260529040", "최예린", "충청남도 공주시 금흥동", "14:45"),
            DummyParcel("20260529041", "강시우", "광주광역시 남구 월산동", "15:10"),
            DummyParcel("20260529042", "윤하민", "경상남도 진주시 신안동", "15:35"),
            DummyParcel("20260529043", "임재현", "전라북도 익산시 부송동", "16:00"),
            DummyParcel("20260529044", "조유나", "강원도 속초시 영랑동", "16:25"),
            DummyParcel("20260529045", "한도영", "인천광역시 남동구 만수동", "16:50"),
            DummyParcel("20260529046", "오지수", "서울시 성동구 행당동", "17:15"),
            DummyParcel("20260529047", "장민채", "경기도 광명시 소하동", "17:40"),
            DummyParcel("20260529048", "송태현", "대구광역시 북구 침산동", "18:05"),
            DummyParcel("20260529049", "권채아", "충청북도 제천시 의림동", "18:30"),
            DummyParcel("20260529050", "배도윤", "전라남도 순천시 연향동", "19:00"),
            DummyParcel("20260529051", "유서진", "경상북도 안동시 용상동", "19:30")
        )
        cal.set(2026, Calendar.MAY, 29)
        for (p in may29List) {
            if (existsByTracking(db, p.tracking)) continue
            val (h, m) = p.time.split(":").map { it.toInt() }
            cal.set(Calendar.HOUR_OF_DAY, h); cal.set(Calendar.MINUTE, m); cal.set(Calendar.SECOND, 0)
            insertDummy(db, p.tracking, p.receiver, p.region, df.format(cal.time))
            insertedCount++
        }

        // ============ 5월 30일 (오늘) - 17개, 오전 6~9시, 접수 완료 ============
        val may30List = listOf(
            DummyParcel("20260530001", "김하준", "서울시 강남구 대치동", "06:02"),
            DummyParcel("20260530002", "이소율", "경기도 성남시 분당구 야탑동", "06:15"),
            DummyParcel("20260530003", "박지우", "부산광역시 해운대구 좌동", "06:28"),
            DummyParcel("20260530004", "최유진", "대전광역시 서구 관저동", "06:41"),
            DummyParcel("20260530005", "정서윤", "광주광역시 북구 운암동", "06:54"),
            DummyParcel("20260530006", "강민재", "강원도 춘천시 퇴계동", "07:05"),
            DummyParcel("20260530007", "윤태양", "인천광역시 연수구 옥련동", "07:16"),
            DummyParcel("20260530008", "임채원", "서울시 마포구 연남동", "07:27"),
            DummyParcel("20260530009", "조예빈", "경기도 수원시 팔달구", "07:38"),
            DummyParcel("20260530010", "한시우", "대구광역시 수성구 지산동", "07:49"),
            DummyParcel("20260530011", "오도현", "충청북도 청주시 흥덕구", "08:00"),
            DummyParcel("20260530012", "장서아", "서울시 송파구 가락동", "08:12"),
            DummyParcel("20260530013", "송유찬", "경기도 고양시 일산동구", "08:24"),
            DummyParcel("20260530014", "권나은", "울산광역시 남구 신정동", "08:36"),
            DummyParcel("20260530015", "배준호", "충청남도 천안시 동남구", "08:48"),
            DummyParcel("20260530016", "유서현", "전라북도 전주시 완산구", "08:55"),
            DummyParcel("20260530017", "신지훈", "경상남도 김해시 삼안동", "09:00"),
            DummyParcel("20260530777", "최지윤", "서울특별시 강남구", "11:00")
        )
        cal.set(2026, Calendar.MAY, 30)
        for (p in may30List) {
            if (existsByTracking(db, p.tracking)) continue
            val (h, m) = p.time.split(":").map { it.toInt() }
            cal.set(Calendar.HOUR_OF_DAY, h); cal.set(Calendar.MINUTE, m); cal.set(Calendar.SECOND, 0)
            insertDummy(db, p.tracking, p.receiver, p.region, df.format(cal.time))
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
        val (sortingAt, shippingAt, completeAt) = Parcel.generateTransitionTimes(createdAt)
        val values = ContentValues().apply {
            put(COL_TRACKING_NUMBER, tracking)
            put(COL_RECEIVER, receiver)
            put(COL_REGION, region)
            put(COL_ZONE, zone)
            put(COL_STATUS, "접수 완료")
            put(COL_CREATED_AT, createdAt)
            put(COL_AUTO_MODE, 1)
            put(COL_SORTING_AT, sortingAt)
            put(COL_SHIPPING_AT, shippingAt)
            put(COL_COMPLETE_AT, completeAt)
        }
        db.insert(TABLE_PARCELS, null, values)
    }

    private data class DummyParcel(
        val tracking: String,
        val receiver: String,
        val region: String,
        val time: String
    )

    // ===== CRUD =====

    fun insertParcel(parcel: Parcel): Long {
        val db = writableDatabase
        val (sortingAt, shippingAt, completeAt) = Parcel.generateTransitionTimes(parcel.createdAt)
        val values = ContentValues().apply {
            put(COL_TRACKING_NUMBER, parcel.trackingNumber)
            put(COL_RECEIVER, parcel.receiver)
            put(COL_REGION, parcel.region)
            put(COL_ZONE, parcel.zone)
            put(COL_STATUS, parcel.status)
            put(COL_CREATED_AT, parcel.createdAt)
            put(COL_AUTO_MODE, parcel.autoMode)
            put(COL_SORTING_AT, sortingAt)
            put(COL_SHIPPING_AT, shippingAt)
            put(COL_COMPLETE_AT, completeAt)
        }
        return db.insert(TABLE_PARCELS, null, values)
    }

    fun getAllParcels(): List<Parcel> {
        autoProgressAll()
        val parcels = mutableListOf<Parcel>()
        val db = readableDatabase
        val cursor = db.query(TABLE_PARCELS, null, null, null, null, null, "$COL_TRACKING_NUMBER DESC")
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
            null, null, "$COL_TRACKING_NUMBER DESC"
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

    fun updateStatusManual(id: Int, newStatus: String) {
        val db = writableDatabase
        val now = Parcel.nowDateTime()

        val cursor = db.query(
            TABLE_PARCELS,
            arrayOf(COL_SORTING_AT, COL_SHIPPING_AT),
            "$COL_ID = ?", arrayOf(id.toString()),
            null, null, null
        )
        var oldSortingAt = ""
        var oldShippingAt = ""
        cursor.use {
            if (it.moveToFirst()) {
                oldSortingAt = it.getString(it.getColumnIndexOrThrow(COL_SORTING_AT)) ?: ""
                oldShippingAt = it.getString(it.getColumnIndexOrThrow(COL_SHIPPING_AT)) ?: ""
            }
        }

        val (sortingAt, shippingAt, completeAt) = Parcel.regenerateFromStatus(
            newStatus, now, oldSortingAt, oldShippingAt
        )

        val values = ContentValues().apply {
            put(COL_STATUS, newStatus)
            if (sortingAt != null) put(COL_SORTING_AT, sortingAt)
            if (shippingAt != null) put(COL_SHIPPING_AT, shippingAt)
            if (completeAt != null) put(COL_COMPLETE_AT, completeAt)
        }
        db.update(TABLE_PARCELS, values, "$COL_ID = ?", arrayOf(id.toString()))
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

    // ===== 자동 진행 =====

    private fun autoProgressAll() {
        val db = writableDatabase
        val cursor = db.query(
            TABLE_PARCELS,
            arrayOf(COL_ID, COL_STATUS, COL_SORTING_AT, COL_SHIPPING_AT, COL_COMPLETE_AT),
            null, null, null, null, null
        )
        cursor.use {
            while (it.moveToNext()) {
                val id = it.getInt(it.getColumnIndexOrThrow(COL_ID))
                val currentStatus = it.getString(it.getColumnIndexOrThrow(COL_STATUS))
                val sortingAt = it.getString(it.getColumnIndexOrThrow(COL_SORTING_AT)) ?: ""
                val shippingAt = it.getString(it.getColumnIndexOrThrow(COL_SHIPPING_AT)) ?: ""
                val completeAt = it.getString(it.getColumnIndexOrThrow(COL_COMPLETE_AT)) ?: ""

                val expected = Parcel.expectedStatusFromTimes(sortingAt, shippingAt, completeAt)
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
            arrayOf(COL_STATUS, COL_SORTING_AT, COL_SHIPPING_AT, COL_COMPLETE_AT),
            "$COL_ID = ?", arrayOf(id.toString()),
            null, null, null
        )
        cursor.use {
            if (it.moveToFirst()) {
                val currentStatus = it.getString(it.getColumnIndexOrThrow(COL_STATUS))
                val sortingAt = it.getString(it.getColumnIndexOrThrow(COL_SORTING_AT)) ?: ""
                val shippingAt = it.getString(it.getColumnIndexOrThrow(COL_SHIPPING_AT)) ?: ""
                val completeAt = it.getString(it.getColumnIndexOrThrow(COL_COMPLETE_AT)) ?: ""

                val expected = Parcel.expectedStatusFromTimes(sortingAt, shippingAt, completeAt)
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
        val sortingAtIdx = c.getColumnIndex(COL_SORTING_AT)
        val shippingAtIdx = c.getColumnIndex(COL_SHIPPING_AT)
        val completeAtIdx = c.getColumnIndex(COL_COMPLETE_AT)
        return Parcel(
            id = c.getInt(c.getColumnIndexOrThrow(COL_ID)),
            trackingNumber = c.getString(c.getColumnIndexOrThrow(COL_TRACKING_NUMBER)),
            receiver = c.getString(c.getColumnIndexOrThrow(COL_RECEIVER)),
            region = c.getString(c.getColumnIndexOrThrow(COL_REGION)),
            zone = c.getString(c.getColumnIndexOrThrow(COL_ZONE)),
            status = c.getString(c.getColumnIndexOrThrow(COL_STATUS)),
            createdAt = c.getString(c.getColumnIndexOrThrow(COL_CREATED_AT)),
            autoMode = autoMode,
            sortingAt = if (sortingAtIdx >= 0) (c.getString(sortingAtIdx) ?: "") else "",
            shippingAt = if (shippingAtIdx >= 0) (c.getString(shippingAtIdx) ?: "") else "",
            completeAt = if (completeAtIdx >= 0) (c.getString(completeAtIdx) ?: "") else ""
        )
    }
}
