package com.example.umnlifeapp

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.umnlifeapp.databinding.ActivityInGameBinding

const val KEY_MAKAN = "makan_key"
const val KEY_TIDUR = "tidur_key"
const val KEY_MAIN = "main_key"
const val KEY_BLJR = "belajar_key"
const val KEY_NAME = "name_key"
const val KEY_CHAR = "char_key"
const val KEY_SEMESTER = "semester_key"

class ActivityInGame: AppCompatActivity() {
    private lateinit var binding: ActivityInGameBinding
    private var time = 0
    private var hours = 0
    private val waktuDo = TimeService()
    private val waktuAlertDo = TimeService()
    private var mMediaPlayer: MediaPlayer? = null
    private var timeDesc:Int = 0
    private var char:Int = 0
    private var name:String? = ""
    private lateinit var clickFUnc: OnClickHandler
    private var putarWaktu = TimeService()
    private var saveData: SharedPreferences.Editor? = null
    private var getSaveData: SharedPreferences? = null
    protected var saveFlag = true;
    protected var activitas = "main"
    val alarmTime = TimeService()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        setContentView(R.layout.activity_in_game)

        binding = ActivityInGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState != null) {
            clickFUnc.progressMakan = savedInstanceState.getInt(KEY_MAKAN, 50)
            clickFUnc.progressTidur = savedInstanceState.getInt(KEY_TIDUR, 50)
            clickFUnc.progressMain = savedInstanceState.getInt(KEY_MAIN, 50)
            clickFUnc.progressBljr = savedInstanceState.getInt(KEY_BLJR, 0)
            activitas = savedInstanceState.getString("AKTIFITAS", null)
            char = savedInstanceState.getInt(KEY_CHAR)
            name = savedInstanceState.getString(KEY_NAME).toString()
            clickFUnc = OnClickHandler(binding, char, this)
        }else{
            getSaveData = getSharedPreferences("dataGame", MODE_PRIVATE)
            if(getSaveData?.contains("NAMA") == true){
                defaultValue(false)
                clickFUnc = OnClickHandler(binding, char, this)
                clickFUnc.progressMakan = getSaveData?.getInt("MAKAN", 50)!!
                clickFUnc.progressMain = getSaveData?.getInt("MAIN", 50)!!
                clickFUnc.progressTidur = getSaveData?.getInt("TIDUR", 50)!!
                clickFUnc.progressBljr = getSaveData?.getInt("BLJR", 0)!!
                time = getSaveData?.getInt("TIME", 0)!!
                clickFUnc.semester = getSaveData?.getInt("SEMESTER", 1)!!
                activitas = getSaveData?.getString("AKTIVITAS", null)!!
                binding.barBelajar.progress = clickFUnc.progressBljr
            }else{
                defaultValue(true)
                clickFUnc = OnClickHandler(binding, char, this)
            }

        }

        binding.buttonBljr.setOnClickListener{
            activitas = "belajar"
            clickFUnc.onStudy(this.waktuDo, this.waktuAlertDo)
            if(clickFUnc.progressBljr == 0){
                val intent =  Intent(this, quizTime::class.java)
                startActivity(intent)
            }
        }

        binding.buttonTidur.setOnClickListener{
            alarmTime.resetTimeOut()
            activitas = "tidur"
            binding.viewPopUp.visibility = View.VISIBLE
            Frezze()
            val popupWindow = PopupWindow(this)
            val viewPopUp = layoutInflater.inflate(R.layout.alarm_popups, null) // inflate a new instance
            popupWindow.contentView = viewPopUp

            val languages = resources.getStringArray(R.array.time_alarm_array)
            val adapterItems = ArrayAdapter(this, R.layout.list_items, languages)
            val autoInput: AutoCompleteTextView = viewPopUp.findViewById(R.id.editTextNumberDecimal)
            autoInput.setAdapter(adapterItems)

            val btnAlrm: Button? = viewPopUp.findViewById(R.id.button_alarm)
            val btnCancle : ImageButton? = viewPopUp.findViewById(R.id.button_cancel)

            btnCancle?.setOnClickListener({
                popupWindow.dismiss()
                binding.viewPopUp.visibility = View.GONE
                goAgain()
            })

            btnAlrm?.setOnClickListener {
                if(autoInput.text.toString().toIntOrNull() == null){
                    popupWindow.dismiss()
                    binding.viewPopUp.visibility = View.GONE
                    goAgain()
                }else{
                    popupWindow.dismiss()
                    binding.viewPopUp.visibility = View.GONE
                    time = (60 * (autoInput.text.toString().toInt()))
                    clickFUnc.onTidur()
                    if(mMediaPlayer != null) {
                        mMediaPlayer!!.release()
                        mMediaPlayer = null
                    }
                    playSound(R.raw.alarm)
                    timeDesc = 0

                    alarmTime.setTimeout({
                        if(mMediaPlayer != null) {
                            mMediaPlayer!!.release()
                            mMediaPlayer = null
                        }
                        goAgain()
                        alarmTime.resetTimeOut()}, 3000)
                }
            }

            (viewPopUp.parent as? ViewGroup)?.removeView(viewPopUp)
            popupWindow.showAtLocation(binding.root, Gravity.CENTER, 0, 0)
        }

        binding.buttonMain.setOnClickListener{
            activitas = "main"
            clickFUnc.onMain()
        }
        binding.buttonMakan.setOnClickListener{
            activitas = "makan"
            clickFUnc.onMakan()
        }

        binding.imageButtonInfo.setOnClickListener {
            val Intent =  Intent(this, Info::class.java);
            startActivity(Intent)
        }

        binding.buttonRule.setOnClickListener{
            playSound(R.raw.open_book)
            val IntentRule =  Intent(this, playRules::class.java);
            startActivity(IntentRule)
        }
    }

    private fun countDownDO() {
        waktuDo.setTimeout({ Toast.makeText(this, "Kamu akan di DO jika tidak belajar", Toast.LENGTH_LONG).show()},30000)
        waktuAlertDo.setTimeout({ Toast.makeText(this, "Kamu DI DO", Toast.LENGTH_LONG).show()},60000)
    }

    private fun waktuJalan() {
        putarWaktu.setInterval({
            runOnUiThread {
                plusTime(1)
                val seconds= time%60
                val hours = (time/60)%24
                val nowHours:String = String.format("%02d:%02d",hours,seconds)
                binding.jam.text = nowHours
                tampilWaktu()
            }
        }, 1000)
    }

    fun plusTime(time: Int){
        this.time += time
    }

    fun playSound(source: Int) {
        if (mMediaPlayer != null){
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }

        mMediaPlayer = MediaPlayer.create(this, source)
        mMediaPlayer!!.isLooping = true
        mMediaPlayer!!.start()
    }

    fun defaultValue(save: Boolean){
        if(intent.getStringExtra("char") != null){
            char = intent.getStringExtra("char")!!.toInt()
        }else{
            char = getSaveData?.getInt("CHAR", R.drawable.char1) ?: R.drawable.char1
        }
        val refChar = when(char){
            R.drawable.char1 -> R.drawable.char1
            R.drawable.char2 -> R.drawable.char2
            else -> R.drawable.char3
        }
        binding.character.setImageResource(refChar)

        if ( intent.getStringExtra("name") != null) {
            name = intent.getStringExtra("name")
        }else{
            name = getSaveData?.getString("NAMA", name).toString()
        }
        binding.name.text = name

        if(save){
            binding.bagroundInnerGame.setImageResource(R.drawable.kamar_malam)
            Salam(this).showCustomToast("Selamat Malam " + name, this)
            playSound(R.raw.musikmalam)
        }
        countDownDO()
    }

    private fun tampilWaktu() {
        val salam:String
        val sound:Int
        val bg: Int
        when((time/60)%24){
            in 4 until 10 -> {
                salam ="Selamat Pagi "+ name
                sound = R.raw.musikpagi
            }
            in 10 until 14 -> {
                salam = "Selamat Siang "+ name
                sound = R.raw.musiksiang
            }
            in 14 until 16 -> {
                salam = "Selamat Sore "+ name
                sound = R.raw.musiksore
            }
            else -> {
                salam = "Selamat Malam "+ name
                sound = R.raw.musikmalam
            }
        }


        when((time/60)%24){
            in 4 until 10 ->{
                when(activitas){
                    "main"-> bg = R.drawable.tamu_pagi
                    "makan"-> bg = R.drawable.dapur_pagi
                    "tidur"->bg = R.drawable.kamar_pagi
                    else -> bg = R.drawable.kelas_pagi
                }
            }
            in 10 until 14 -> {
                when (activitas) {
                    "main" -> bg = R.drawable.tamu_siang
                    "makan" -> bg = R.drawable.dapur_siang
                    "tidur" -> bg = R.drawable.kamar_siang
                    else -> bg= R.drawable.kelas_siang
                }
            }
            in 14 until 16 ->{
                when(activitas){
                    "main"-> bg = R.drawable.tamu_sore
                    "makan"-> bg = R.drawable.dapur_sore
                    "tidur"-> bg = R.drawable.kamar_sore
                    else -> bg = R.drawable.kelas_sore
                }
            }
            else -> {
                when (activitas) {
                    "main" -> bg = R.drawable.tamu_malam
                    "makan" -> bg = R.drawable.dapur_malam
                    "tidur" -> bg = R.drawable.kamar_malam
                    else -> bg = R.drawable.kelas_malam
                }
            }
        }


        if(time/60 != this.hours || mMediaPlayer == null){
            binding.bagroundInnerGame.setImageResource(bg)
            Salam(this).showCustomToast(salam,this)
            this.hours = time/60

            if(timeDesc != sound){
                playSound(sound)
                timeDesc = sound
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_MAKAN, clickFUnc.progressMakan)
        outState.putInt(KEY_MAIN, clickFUnc.progressMain)
        outState.putInt(KEY_TIDUR, clickFUnc.progressTidur)
        outState.putInt(KEY_BLJR, clickFUnc.progressBljr)
        outState.putInt(KEY_SEMESTER, clickFUnc.semester)
        outState.putInt("TIME", time)
        outState.putString(KEY_NAME, name)
        outState.putInt(KEY_CHAR, char)
        outState.putString("AKTIFITAS", activitas)
    }

    fun goAgain(){
        waktuJalan()
        countDownDO()
        clickFUnc.intervalMakan()
        clickFUnc.intervalMain()
        clickFUnc.intevalTidur()
    }

    fun Frezze(){
        putarWaktu.resetInterval()
        waktuDo.resetTimeOut()
        waktuAlertDo.resetTimeOut()
        clickFUnc.waktuMain.resetInterval()
        clickFUnc.waktuTidur.resetInterval()
        clickFUnc.waktuMakan.resetInterval()
    }

    override fun onStart() {
        super.onStart()
        goAgain()
        if(intent.getBooleanExtra("jawaban", false) != null && intent.getBooleanExtra("jawaban",false)){
            clickFUnc.semester+=1
            binding.semester.setText(String.format("Semester %d", clickFUnc.semester))
            clickFUnc.maxBljr = clickFUnc.maxBljr * 1.1
            binding.barBelajar.setMax(clickFUnc.maxBljr.toInt())
        }else{
            binding.semester.setText(String.format("Semester %d", clickFUnc.semester))
        }

        if(clickFUnc.semester > 8){
            Frezze()
            saveData = getSharedPreferences("dataGame", MODE_PRIVATE).edit()
            saveData?.clear()
            saveData?.apply()
//            if(mMediaPlayer != null) {
//                mMediaPlayer!!.release()
//                mMediaPlayer = null
//            }
            saveFlag = false;
            val intent =  Intent(this, Congratulations::class.java)
            startActivity(intent)
        }
    }
    override fun onStop() {
        super.onStop()
        saveData = getSharedPreferences("dataGame", MODE_PRIVATE).edit()
        Frezze()
        if(mMediaPlayer != null) {
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
        if(saveFlag && clickFUnc.saveFlag){
            saveData?.putInt("MAKAN", clickFUnc.progressMakan)
            saveData?.putInt("MAIN", clickFUnc.progressMain)
            saveData?.putInt("BLJR", clickFUnc.progressBljr)
            saveData?.putInt("TIDUR", clickFUnc.progressTidur)
            saveData?.putInt("TIME", time)
            saveData?.putInt("SEMESTER", clickFUnc.semester)
            saveData?.putInt("CHAR", char)
            saveData?.putString("NAMA", name)
            saveData?.putString("AKTIVITAS", activitas)
            saveData?.apply()
        }
    }
}