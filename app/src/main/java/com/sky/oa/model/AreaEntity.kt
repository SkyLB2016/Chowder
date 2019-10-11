package com.sky.oa.model

/**
 * Created by SKY on 2017/7/4.
 */
class AreaEntity {
    var id: String? = null
    var name: String? = null
    var items: List<AreaEntity>? = null
    var level: Int = 0//层级
    var isExpand = false
        set(expand) {
            field = expand
            if (!this.isExpand)
                for (area in items!!)
                    area.isExpand = this.isExpand
        }//是否展开

    var parent: AreaEntity? = null

    /**
     * 是否是根节点
     *
     * @return
     */
    val isRoot: Boolean
        get() = parent == null

    /**
     * 判断父节点是否展开
     *
     * @return
     */
    val isParentExpand: Boolean
        get() = if (parent == null) false else parent!!.isExpand

    /**
     * 子类是否为空
     *
     * @return
     */
    fun hasLeaf(): Boolean {
        return items!!.isEmpty()
    }

}
