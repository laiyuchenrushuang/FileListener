package com.ly.filelistener

import android.os.Build
import android.support.annotation.RequiresApi
import java.io.File
import java.io.IOException
import java.nio.file.*

/**
 * Created by ly on 2019/8/21 17:09
 *
 * Copyright is owned by chengdu haicheng technology
 * co., LTD. The code is only for learning and sharing.
 * It is forbidden to make profits by spreading the code.
 */
class FileManager(filelistenr: FileListener, pathStr: String) {
    var pathStr = pathStr
    var mFileListener: FileListener = filelistenr
    @RequiresApi(Build.VERSION_CODES.O)
    fun registerFileListener() {
        val path = Paths.get(pathStr)
        try {
            val watchService = FileSystems.getDefault().newWatchService()
            path.register(
                watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY,
                StandardWatchEventKinds.ENTRY_DELETE
            )
            while (true) {
                val key = watchService.take()

                for (watchEvent in key.pollEvents()) {

                    val kind = watchEvent.kind()

                    if (kind === StandardWatchEventKinds.OVERFLOW) {
                        continue
                    }
                    //创建事件
                    if (kind === StandardWatchEventKinds.ENTRY_CREATE) {
                        mFileListener.fileCreate()
                    }
                    //修改事件
                    if (kind === StandardWatchEventKinds.ENTRY_MODIFY) {
                        mFileListener.fileModify()
                    }
                    //删除事件
                    if (kind === StandardWatchEventKinds.ENTRY_DELETE) {
                        mFileListener.fileDelete()
                    }
                    // get the filename for the event
                    val watchEventPath = watchEvent as WatchEvent<Path>
                    val filename = watchEventPath.context()
                    // print it out
                    println("[lylog] $kind -> $filename")

                }
                // reset the keyf
                val valid = key.reset()
                // exit loop if the key is not valid (if the directory was
                // deleted,for
                if (!valid) {
                    break
                }
            }

        } catch (ex: IOException) {
            System.err.println(ex)
        } catch (ex: InterruptedException) {
            System.err.println(ex)
        }
    }
}
