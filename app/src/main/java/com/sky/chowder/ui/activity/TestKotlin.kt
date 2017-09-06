package com.sky.chowder.ui.activity

import com.sky.utils.LogUtils
import java.io.File

class TestKotlin {

     fun initialize() {
        val s = "abc"
        val str = "$s.length is ${s.length}"
//结果为 abc.length is 3
        LogUtils.i(str)
        val i = 10
        val ss = "i = ${i + 1}"
        LogUtils.i(ss)
        val pow = pow1(3);

        val age = 99
        val typeOfPerson = when (age) {
            0 -> "New born"
            in 1..12 step 2 -> "hello"
            in 139 downTo 19 -> LogUtils.i("Teenager")
            else -> "Adult"
        }
        cases(typeOfPerson)
        loop@ for (i in 1..10) {
            for (j in 1..10) {
                if (i == 5) {
                    continue@loop
                }
                LogUtils.i("Teenager==" + j)
            }
        }
        vars(1, 2, 3, 4, 5)//输出12345
        findFixPoint(9.0)
        val files = File("Test").listFiles()
        val dd = files ?: "empty"
        val length = files?.size ?: 2
        LogUtils.i("length==$length")
        LogUtils.i(files?.size.toString())
        LogUtils.i(dd.toString())
        LogUtils.i(files?.size?.toString() ?: "empty")
        files?.let { LogUtils.i("aAAAAAAAAAAAAAAAAAAAAAA") } ?: LogUtils.i("BBBBBBBBBBBBBBBBBBBB")
        files.let { LogUtils.i("HAKJSDFHKJKJHKJFSDASHK") }
        val x = 10
        val y = 9
        val sumLambda: (Int, Int) -> Int = { x, y -> x + y }
        val sumLamb = { x: Int, y: Int -> x + y }
        val sum = { x: Int, y: Int -> x + y }
        val compare: (String, String) -> Boolean = { a, b -> a.length < b.length }
        fun foo(param: Int) {
            val result = when (param) {
                1 -> "one"
                2 -> "two"
                else -> "three"
            }
        }
        main(ints)

        val map = mapOf("a" to 1, "b" to 2, "c" to 3)
        for (i in map)
            LogUtils.i(i.value.toString())
        LogUtils.i(map.keys.toString())
        val va = map["a"]
    }

    private val ints = listOf(0, 1, 2, 3)

    private fun main(args: List<Int>) {
        foo()
    }

    private fun foo() {
        ints.forEach {
            if (it == 3) return@forEach
            LogUtils.i("forEach==$0it")
        }
    }

    fun arrayOfMinusOnes(size: Int): IntArray {
        return IntArray(size).apply {
            fill(-1)
        }
    }

    fun arrayOfMinusOness(size: Int): Unit {
    }

    infix fun Int.shl(x: Int): Int {
        return 3
    }

    tailrec fun findFixPoint(x: Double = 1.0): Double = if (x == Math.cos(x)) x else findFixPoint(Math.cos(x))
    //等效于下面的代码:
    private fun findFixPoint(): Double {
        var x = 1.0
        while (true) {
            val y = Math.cos(x)
            if (x == y) return y
            x = y
        }
    }

    private fun cases(obj: Any) {
        when (obj) {
            1 -> print("one")
            "hello" -> LogUtils.i("Teenager")
            is Long -> LogUtils.i("Long")
            !is Long -> LogUtils.i("Not a string")
            else -> LogUtils.i("Unknown")
        }
    }

    fun max(a: Int, b: Int): Int {
        return if (a > b) a else b
    }

    //第一种形态
    fun pow(a: Int): Double {
        return Math.pow(a.toDouble(), 2.toDouble());
    }

    // 第二种形态，一个表达式函数体和一个可推断类型
    fun pow1(a: Int) = Math.pow(a.toDouble(), 2.toDouble())
    val sumLamb = { x: Int, y: Int -> x + y }

    fun double(x: Int) = x * 2

    private fun vars(vararg v: Int) {
        for (vt in v) {
            print(vt)
        }
    }

    fun List<Int>.varss(vararg v: Int) {
        for (vt in v) {
            print(vt)
        }
    }

    fun MutableList<Int>.swap(x: Int, y: Int) {
        val temp = this[x] // this 对应 list
        this[x] = this[y]
        this[y] = temp
    }
}