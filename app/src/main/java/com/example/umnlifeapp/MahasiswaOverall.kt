package com.example.umnlifeapp

class MahasiswaOverall(val nama:String, var kenyang: Int = 50,
                       var energi: Int = 50,
                       var motivasi: Int = 50, var pengetahuan: Int = 0)
    {
        //sdsd

        fun kurangiPengetahuan(angka: Int) {
            if(pengetahuan  > 0)
                pengetahuan -= angka
        }

        fun kurangiKenyang(angka: Int) {
            if(kenyang > 0)
                kenyang -= angka
        }

        fun kurangiEnergi(angka: Int) {
            if(energi > 0)
                energi -= angka
        }

        fun kurangiMotivasi(angka: Int) {
            if(motivasi > 0)
                motivasi -= angka
        }
    }