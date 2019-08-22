package com.ly.filelistener

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.annotation.RequiresApi
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileWriter
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity(), FileListener {

    private var ficreate = false
    var fileManager: FileManager? = null
    val pathStr = Environment.getExternalStorageDirectory().toString() + "/laiyu219"
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        registerPermission() //注册权限
        //检查权限
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ||
            checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        ) {
            makeFile(pathStr)  //创建文件
            thread {
                fileManager = FileManager(this, pathStr) //监听目录
                fileManager!!.registerFileListener()  //开始监听
            }
        } else {
            showToast("请打开权限才能使用软件")
            finish()
        }
        bindEvent()
    }

    //注册权限
    private fun registerPermission() {
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ), 1
            )
        }
    }

    //创建文件
    private fun makeFile(pathStr: String) {
        var file = File(pathStr)
        if (!file.exists()) {
            file.mkdir()
        }
    }

    //点击事件
    private fun bindEvent() {
        //在这个目录下创建ly.txt文件
      bt_create.setOnClickListener {
          var fileLyTxt = File(pathStr,"ly.txt")
          if(!fileLyTxt.exists()){
              fileLyTxt.createNewFile()
          }else{
              showToast("文件已经创建，请进行其他操作")
          }
          ficreate = true
      }
        bt_modify.setOnClickListener {
            if(!ficreate){
                showToast("请先创建文件进行此操作")
                return@setOnClickListener
            }
            var fileLyTxt = File(pathStr,"ly.txt")
            try {
                val fw = FileWriter(fileLyTxt, true)
                fw.write("laiyu is bigger than Jiangjian " + "\n")
                fw.close()
            } catch (e: Exception) {
                showToast("写的过程出现了异常，原因不明")
            }
        }
        bt_delete.setOnClickListener {
            if(!ficreate){
                showToast("请先创建文件进行此操作")
                return@setOnClickListener
            }
            var fileLyTxt = File(pathStr,"ly.txt")
            fileLyTxt.delete()
            ficreate = false
        }
    }

    //监听文件 创建
    override fun fileCreate() {
        showLog("fileCreate")
    }

    override fun fileModify() {
        showLog("fileModify")
    }

    override fun fileDelete() {
        showLog("fileDelete")
    }
    //log显示
    fun showLog(s: String) {
        Log.d("lylog", " result = " + s)
    }
    //Toast显示
    private fun showToast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }
}
