package com.sky.oa.activity

import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import com.sky.oa.R
import com.sky.oa.model.PeopleEntity
import com.sky.sdk.utils.FileUtils
import com.sky.sdk.utils.LogUtils
import kotlinx.android.synthetic.main.activity_excel.*
import java.io.File


class ExcelJXLActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_excel)
        bt01.setOnClickListener {
            creatExcel()
        }
        bt02.setOnClickListener {
            var filePath =
                Environment.getExternalStorageDirectory().path + "/老旧线路评估/测试.xlsx"//文件的全路径
            //读取Excel的内容
//            val resultFromXLS = ExcelJXLUtils.getExcelContent(filePath)
//            LogUtils.i(resultFromXLS.toString())
        }
        bt03.setOnClickListener {
            val list: List<PeopleEntity> = readExcelImage()
            LogUtils.i(list?.get(0)?.name)

            val file = list[0].image
            val imagePath=Environment.getExternalStorageDirectory().path + "/Download"
            FileUtils.copyFile(file, File(imagePath),"image.png")
            val bitmap = BitmapFactory.decodeFile(imagePath+"image.png")
            imageView.setImageBitmap(bitmap)
        }
        bt04.setOnClickListener {
        }

    }

    private fun creatExcel() {
        //创建文件夹
        var filePath = Environment.getExternalStorageDirectory().path + "/老旧线路评估"
        val file = File(filePath)
        if (!file.exists()) {
            file.mkdirs()
        }

        val excelFileName = "/测试.xlsx"//Excel文件名
        filePath += excelFileName//文件的全路径
        val sheetName = "页签1"
        val columnTitles: Array<String> = arrayOf("姓名", "性别", "年龄", "地址")//Excel的列字段

        //初始化 Excel的文件、表单名称、列字段
//        ExcelJXLUtils.initExcel(filePath, sheetName, columnTitles)

        val list: MutableList<PeopleEntity> = ArrayList()
        list.add(PeopleEntity("张三", "男", "22", "北京"))
        list.add(PeopleEntity("李四", "女", "33", "上海"))
        list.add(PeopleEntity("赵武", "女", "44", "广州"))
        list.add(PeopleEntity("王六", "男", "55", "深圳"))
        val datas: MutableList<MutableList<String>> = ArrayList()
        var data: MutableList<String>
        for (people in list) {
            data = ArrayList()
            data.add(people.name)
            data.add(people.gender)
            data.add(people.age)
            data.add(people.local)
            datas.add(data)
        }

        //写入到excel文件里内容
//        ExcelJXLUtils.writeContentToExcel(datas, filePath, "页签1", this)
    }

    /**
     * 读取Excel数据
     *
     * @return List<Country>
    </Country> */
    private fun readExcelImage(): List<PeopleEntity> {
        val countryList: MutableList<PeopleEntity> = ArrayList()
//        var filePath =
////            Environment.getExternalStorageDirectory().path + "/Download/image2.xlsx"//文件的全路径
//            Environment.getExternalStorageDirectory().path + "/Download/image.xls"//文件的全路径
//        //读取Excel的内容
//
//        try {
//            // 创建输入流
//            val stream: InputStream = FileInputStream(filePath)
//            val book: Workbook = Workbook.getWorkbook(stream)
//
//
//            // 获得第一个工作表对象
//            val sheet = book.getSheet(0)
//            val Rows = sheet.rows
//            for (i in 2 until Rows) { //将每一列的数据读取
//                val id = sheet.getCell(0, i).contents
//                val areaCode = sheet.getCell(1, i).contents
//                val subName = sheet.getCell(2, i).contents
//                val image = sheet.getDrawing(2) as Drawing
////                image.imageFile
////                val wi = WritableImage(
////                    image,
////                    toSheet.getWorkbook().getDrawingGroup()
////                )
//
//                LogUtils.i("image==${image}")
//                LogUtils.i("imageFile==${image.imageFile}")
//                LogUtils.i("absolutePath==${image.imageFile.absolutePath}")
//                LogUtils.i("column==${image.column}")
//                LogUtils.i("width==${image.width}")
//                LogUtils.i("row==${image.row}")
//                LogUtils.i("height==${image.height}")
//                countryList.add(
//                    PeopleEntity(id, areaCode, image.imageFile)
//                )
//            }
//            book.close()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
        return countryList
    }
}
