package com.michaelrichards.laughlounge.utils

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream
import org.springframework.stereotype.Component
import java.awt.Color
import java.awt.Font
import java.awt.image.BufferedImage
import java.util.zip.Deflater
import java.util.zip.Inflater
import javax.imageio.ImageIO
import kotlin.random.Random

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

        fun createBasicProfileImage(letter: Char, color: Color? = null): ByteArray {
            val width = 1000
            val height = 1000

            val bufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
            val g2d = bufferedImage.createGraphics()




            val redValue = Random.nextInt(255)
            val greenValue = Random.nextInt(255)
            val blueValue = Random.nextInt(255)

            g2d.color = color ?: Color(redValue, greenValue, blueValue)
            g2d.fillRect(0, 0, width, height)

            g2d.color = Color.BLACK


            val font = Font("Arial", Font.ITALIC, 400)
            g2d.font = font

            val fm = g2d.fontMetrics
            val x = (width - fm.stringWidth(letter.toString())) / 2
            val y = (height - fm.height) / 2 + fm.ascent
            g2d.drawString(letter.toString(), x, y)


            g2d.dispose()

            val byteArrayOutputStream = ByteArrayOutputStream()
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            return byteArray
        }
    }
}