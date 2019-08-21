package com.ly.filelistener

/**
 * Created by ly on 2019/8/21 17:07
 *
 * Copyright is owned by chengdu haicheng technology
 * co., LTD. The code is only for learning and sharing.
 * It is forbidden to make profits by spreading the code.
 */
interface FileListener {
    fun fileCreate()
    fun fileModify()
    fun fileDelete()
}