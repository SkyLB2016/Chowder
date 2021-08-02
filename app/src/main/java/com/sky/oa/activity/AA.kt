package com.sky.oa.activity

import com.sky.oa.model.Person

fun main() {
//    val a = "${'$'}999.99"
//    println(a)
    val array = Array(20) { value: Int -> value }
    array.forEach {
        println(it)
    }
    println()
    for (i in array) {

    }
    fun a(){
        val per =Person(10,"person")
        per.id
//    per.name
        println(per.id)
    }
    a()
}