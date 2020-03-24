package com.sky.oa.activity

import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.sky.oa.R
import com.sky.oa.model.PeopleEntity
import com.sky.oa.utils.ExcelUtil
import com.sky.sdk.utils.LogUtils
import jxl.Workbook
import kotlinx.android.synthetic.main.activity_excel.*
import java.io.File
import java.io.FileInputStream
import java.io.InputStream


class ExcelActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_excel)
        bt.setOnClickListener {
            test()
           val list: List<PeopleEntity> =readExcel()
            LogUtils.i("activity=="+list?.get(0)?.name)
        }

    }

    private fun test() {
        //创建文件夹
//        var filePath = Environment.getExternalStorageDirectory().path+ "/AndroidExcelDemo"
        var filePath = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)!!.absolutePath + File.separator + "AExcelDemo"
        try {
            val file = File(filePath)
            if (!file.exists()) {
                file.mkdirs()
            }
        }catch ( e:Exception){
            Log.e("bai",e.message)
        }

        //Excel文件名
        val excelFileName = "/测试.xls"

        //Excel的列字段
        val columnTitles : Array<String> = arrayOf("姓名","项目","年")

        //文件的全路径
        filePath += excelFileName

        //初始化 Excel的文件、表单名称、列字段
        ExcelUtil.initExcel(filePath, "project", columnTitles)

        //内容
        val projectBean = PeopleEntity("漠天", "354", "用excel")

        val list:List<PeopleEntity>  = arrayListOf(projectBean)
        //写入到excel文件里内容
        ExcelUtil.writeObjListToExcel(list, filePath, this)

        //读取Excel的内容
        val resultFromXLS  = ExcelUtil.getXlsData(filePath)
        LogUtils.i(resultFromXLS[0].toString())
    }

    /**
     * 读取Excel数据
     *
     * @return List<Country>
    </Country> */
    private fun readExcel(): List<PeopleEntity> {
        val countryList: MutableList<PeopleEntity> = ArrayList()
        try {
            var filePath = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)!!.absolutePath + File.separator + "AExcelDemo"+"/motian.xlsx"

            // 创建输入流
            val stream: InputStream = FileInputStream(filePath)

            val book: Workbook = Workbook.getWorkbook(stream)
//            book.sheets
            // 获得第一个工作表对象
            val sheet = book.getSheet(0)
            val Rows = sheet.rows
            for (i in 1 until Rows) { //将每一列的数据读取
                val id = sheet.getCell(0, i).contents
                val areaCode = sheet.getCell(1, i).contents
                val subName = sheet.getCell(2, i).contents
                countryList.add(
                    PeopleEntity(
                        id,
                        areaCode,
                        subName
                    )
                )
            }
            book.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return countryList
    }
}