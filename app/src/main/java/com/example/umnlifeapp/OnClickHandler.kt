package com.example.umnlifeapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.umnlifeapp.databinding.ActivityInGameBinding

class OnClickHandler(val binding: ActivityInGameBinding, val char: Int, val context: Context): Activity() {
    public var semester = 1
    public var progressMakan = 50
    public var progressTidur = 50
    public var progressMain = 50
    public var progressBljr = 0
    protected var flagMakan = false
    protected var flagTidur = false
    protected var flagMain = false
    private var charTidur: Int = 0
    private var charMain : Int = 0
    private var charMakan: Int = 0
    private var charBljr : Int = 0
    var maxBljr:Double = 100.0
    public var waktuMain = TimeService()
    public var waktuBelajar = TimeService()
    public var waktuMakan = TimeService()
    public var waktuTidur = TimeService()
    private var toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
    var saveFlag = true

    init{
        when(char){
            R.drawable.char1 -> {
                this.charTidur = R.drawable.char1tidur
                this.charMakan = R.drawable.char1makan
                this.charBljr = R.drawable.char1belajar
                this.charMain = R.drawable.char1main
            }
            R.drawable.char2 ->{
                this.charTidur = R.drawable.char2tidur
                this.charMakan = R.drawable.char2makan
                this.charBljr = R.drawable.char2belajar
                this.charMain = R.drawable.char2main
            }
            R.drawable.char3 -> {
                this.charTidur = R.drawable.char3tidur
                this.charMakan = R.drawable.char3makan
                this.charBljr = R.drawable.char3belajar
                this.charMain = R.drawable.char3main
            }
        }
    }

    fun intervalMain() {
        waktuMain.setInterval({
            runOnUiThread{
                minMain()
                if(progressMain <= 0){
                    progressMain = 0
                    val saveData = context.getSharedPreferences("dataGame", MODE_PRIVATE).edit()
                    saveData?.clear()
                    saveData?.apply()
                    saveFlag = false
                    val intent =  Intent(context, Lose::class.java)
                    intent.putExtra("alasan", "Kamu mati karena stress dan bunuh diri")
                    context.startActivity(intent)
                }else if(flagMain == true && progressMain < 20){
                    flagMain = false
                    toast?.cancel()
                    toast.setText("Kamu sangat sedih")
                    toast.show()
                }else{
                    flagMain = true
                }
            }
        },5000)
    }

    fun intevalTidur() {
        waktuTidur.setInterval({
            runOnUiThread{
                minTidur()
                if(progressTidur <= 0){
                    progressTidur = 0
                    val saveData = context.getSharedPreferences("dataGame", MODE_PRIVATE).edit()
                    saveData?.clear()
                    saveData?.apply()
                    saveFlag = false
                    val intent =  Intent(context, Lose::class.java)
                    intent.putExtra("alasan", "Kamu mati karena tidak tidur")
                    context.startActivity(intent)
                }else if(flagTidur == true && progressTidur < 20){
                    flagTidur = false
                    toast?.cancel()
                    toast.setText("Kamu Kurang Tidur")
                    toast.show()
                }else{
                    flagTidur = true
                }
            }
        },15000)
    }

    fun intervalMakan() {

        waktuMakan.setInterval({
            runOnUiThread() {
                minMakan()
                if (progressMakan <= 0) {
                    progressMakan = 0
                    val saveData = context.getSharedPreferences("dataGame", MODE_PRIVATE).edit()
                    saveData?.clear()
                    saveData?.apply()
                    saveFlag = false
                    val intent =  Intent(context, Lose::class.java)
                    intent.putExtra("alasan", "Kamu mati kelaparan")
                    context.startActivity(intent)
                } else if (flagMakan == true && progressMakan < 20) {
                    flagMakan = false
                    toast?.cancel()
                    toast.setText("Kamu lapar")
                    toast.show()
                } else {
                    flagMakan = true
                }
            }
        },1000)
    }

    fun onMain(){
        minTidur()
        plusMain()
        binding.character.setImageResource(charMain)
        TimeService.setTimeout({binding.character.setImageResource(char)},3000)
    }

    fun onMakan(){
        plusMakan()
        plusMain(5)
        binding.character.setImageResource(charMakan)
        TimeService.setTimeout({binding.character.setImageResource(char)},3000)
    }

    fun onTidur(){
        plusTidur()
        minMakan()
        binding.character.setImageResource(charTidur)
        TimeService.setTimeout({binding.character.setImageResource(char)},3000)
    }

    fun onStudy(waktuDo : TimeService, waktuAlertDo:TimeService){
        minMakan()
        minMain(5)
        progressBljr+=10
        if (progressBljr >= maxBljr){
            progressBljr = 0
        }
        binding.barBelajar.progress = progressBljr
        binding.semester.setText(String.format("Semester %d", semester))
        waktuDo.resetTimeOut()
        waktuAlertDo.resetTimeOut()
        binding.character.setImageResource(charBljr)
        TimeService.setTimeout({binding.character.setImageResource(char)},3000)
        waktuDo.setTimeout({toast?.cancel()
            toast.setText( "Kamu akan di DO jika tidak belajar")
            toast.show()},30000)

        waktuAlertDo.setTimeout({
            toast?.cancel()
            val saveData = context.getSharedPreferences("dataGame", MODE_PRIVATE).edit()
            saveData?.clear()
            saveData?.apply()
            saveFlag = false
            val intent =  Intent(context, Lose::class.java)
            intent.putExtra("alasan", "Kamu di DO karena tidak belajar")
            context.startActivity(intent)}, 60000)
    }

    fun plusMakan(){
        progressMakan+=20
        if(progressMakan > 100) progressMakan = 100
        binding.barMakan.setProgress(progressMakan)
    }

    fun plusMain(plus: Int = 10){
        progressMain+=plus
        if(progressMain > 100) progressMain = 100
        binding.barMain.setProgress(progressMain)
    }

    fun plusTidur(){
        progressTidur+=60
        if(progressTidur > 100) progressTidur = 100
        binding.barTidur.setProgress(progressTidur)
    }

    fun minTidur(){
        progressTidur-=1
        binding.barTidur.setProgress(progressTidur)
    }

    fun minMakan(){
        progressMakan -= 2
        binding.barMakan.setProgress(progressMakan)
    }

    fun minMain(min: Int = 2){
        progressMain-=min
        binding.barMain.setProgress(progressMain)
    }
}