package com.sky.oa.activity

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.sky.design.app.BaseActivity
import com.sky.oa.R
import com.sky.oa.adapter.TestAdapter
import com.sky.oa.model.Inventory
import kotlinx.android.synthetic.main.acitivity_slidrecycler.*
import java.util.*

/**
 * Created by libin on 2020/04/01 6:13 PM Wednesday.
 */
class RecyclerActivity : BaseActivity() {
    override fun getLayoutResId(): Int {
        return R.layout.acitivity_slidrecycler
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider_inset)!!)
        recycler.addItemDecoration(itemDecoration)

        var mInventories = ArrayList<Inventory>()
        var inventory: Inventory
        val random = Random()
        for (i in 0..49) {
            inventory = Inventory()
            inventory.setItemDesc("测试数据$i")
            inventory.setQuantity(random.nextInt(100000))
            inventory.setItemCode("0120816")
            inventory.setDate("20180219")
            inventory.setVolume(random.nextFloat())
            mInventories.add(inventory)
        }
        var adapter = TestAdapter(R.layout.item_inventroy)
        recycler.setAdapter(adapter)
        adapter.datas = mInventories
//        var adapter = InventoryAdapter(this, mInventories)
//        recycler.adapter = adapter
//        adapter.setOnDeleteClickListener { view, position ->
//            mInventories.removeAt(position)
//            adapter.notifyDataSetChanged()
//            recycler.closeMenu()
//        }
    }
}