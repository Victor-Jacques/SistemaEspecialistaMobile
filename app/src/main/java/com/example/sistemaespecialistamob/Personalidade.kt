package com.example.sistemaespecialistamob

class Personalidade {
    data class Personalidade(
        val dominancia: List<Int> = mutableListOf(0),
        val a: List<Int> = mutableListOf(0),
        val b: List<Int> = mutableListOf(0),
        val c: List<Int> = mutableListOf(0)
    )
}