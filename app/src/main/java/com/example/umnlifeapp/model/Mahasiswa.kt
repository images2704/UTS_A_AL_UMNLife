package com.example.umnlifeapp.model

class Mahasiswa(val nama:String, private var kenyang: Int = 50,
                var energi: Int = 50,
                var motivasi: Int = 50, var pengetahuan: Int = 0)
{
    fun tambahiKenyang(angka: Int) {
        kenyang += angka
    }

    fun kurangiKenyang(angka: Int) {
        kenyang -= angka
    }

    fun tambahiPengetahuan(angka: Int) {
        pengetahuan += angka
    }

    fun tambahiEnergi(angka: Int) {
        energi += angka
    }

    fun kurangiEnergi(angka: Int) {
        energi -= angka
    }

    fun kurangiPengetahuan(angka: Int) {
        pengetahuan -= angka
    }

    fun tambahiMotivasi(angka: Int) {
        motivasi += angka
    }

    fun kurangiMotivasi(angka: Int) {
        motivasi -= angka
    }
}