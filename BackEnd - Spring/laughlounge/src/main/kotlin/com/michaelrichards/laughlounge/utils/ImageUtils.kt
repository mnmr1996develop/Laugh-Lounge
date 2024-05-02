package com.michaelrichards.laughlounge.utils

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream
import org.springframework.stereotype.Component
import java.util.zip.Deflater
import java.util.zip.Inflater

@Component
class ImageUtils {

    companion object {
        fun compressImage(data: ByteArray): ByteArray {
            val deflater = Deflater()
            deflater.setLevel(Deflater.BEST_COMPRESSION)
            deflater.setInput(data)
            deflater.finish()



            val outputStream = ByteArrayOutputStream(data.size)

            val tmp= ByteArray(4 * 1024)

            while (!deflater.finished()){
                val size = deflater.deflate(tmp)
                outputStream.write(tmp, 0, size)
            }
            try {
                outputStream.close()
            }catch(ignored: Exception) {

            }
            return outputStream.toByteArray()
        }

        fun decompressImage(data: ByteArray): ByteArray {
            val inflater = Inflater()
            inflater.setInput(data)




            val outputStream = ByteArrayOutputStream(data.size)

            val tmp= ByteArray(4 * 1024)
            try {
                while (!inflater.finished()){
                    val count = inflater.inflate(tmp)
                    outputStream.write(tmp, 0, count)
                }
                outputStream.close()
            } catch(ignored: Exception) {

            }
            return outputStream.toByteArray()
        }
    }
}