package com.example.umnlifeapp.model

class Mahasiswa(val nama:String, private var kenyang: Int = 50,
                var energi: Int = 50,
                var motivasi: Int = 50, var pengetahuan: Int = 0)
{

    //sdsd
    fun tambahiKenyang(angka: Int) {
        kenyang += angka
    }

    fun kurangiKenyang(angka: Int) {
        kenyang -= angka
    }
}