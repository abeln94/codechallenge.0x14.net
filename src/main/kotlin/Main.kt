import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.abs


fun main() {

    val text = "00011111100010110000100000000000111110111000001010010110011000010000001011111111001011011000111101000001011100101000001100110000000011000100010111110111001111011000010100001110100100000000001000001001000110000111000011110111010111011111010000000000001111011000000010110001100001011111000111000100010010000000110010010110100100111010011010100111101011110110000110111010110100111110100010111111111111111111010111110101111110010100000000000010010110010001000000001000100111110110000000111001111100100000111011111000001111100011000111011111000100110011100000000110011000101000000111000101001111000001000001010010110111100011011011011110000001011110011010100010101001110110110000010111001100001111000000001100000011101111111100011101110111101010110001011001001011100110000010100010001011001001110011111101000000100101111111000101100110110010011100111001011100100101111110000101100010111011000101110000111010111011011001100011010010101110011110101001001100101110010000101000100100000101001100100000000011111111111000110111011011000001010101111100010100110100100110010110010011000100011000110000101111100010000000001000110001001011001101000010000011000111011100111100001011000011101101000010010010001100000001010100001101000010011010000100100101011100101101000010110011000001010010110001100000000011100000001011001001000011111010000011111001110100110000000111001001110110000111000101011000100101110110000011101000001011101111000000100010110011001101011000010000110011000000000111011100100100011110110010001010010010111110001001011011001110100110100011101011100010110100111011101101001000101110001001000100011100100101100011110101011111110001011100101110111000101001010000111010101101001010001111111100110110111000110001110101010110000111110101011101011010001111111010010000100101100100110011101110010101111000001101101110100011001101001110110011111010101010110011111011011010000001101101110100111110101011101011011011011011101011011001111100011110101011111010010001101011011110100011010100010011100011011000010100010011010101100011100011111001110101101010011101001000011110111010001101011100101001010110000110111111100110110111001111110101001111010010000110000001100001100111000000010000000000000000"

    println(text.windowed(8, 8))
    File("output.txt").writeBytes(text.windowed(8, 8).map { Integer.parseInt(it, 2).toByte() }.toByteArray())


    return

//    val img = ImageIO.read(File("hidden_toy_story.png"))
    val img = ImageIO.read(File("9788b1d0ecc849920aae9aa182e8ce54088d3684f2af994d1525223f313318c6.png"))

    val visited = mutableSetOf<Pair<Int, Int>>()

    var x = 81
    var y = 114

    val reds = mutableListOf<Int>()
    val greens = mutableListOf<Int>()
    val blues = mutableListOf<Int>()

    val result = buildString {

        fun aprint(a: Any?) = print(a).also { append(a) }

        img@ while (true) {

            visited += x to y
            val red = img.red(x, y)
            val green = img.green(x, y)
            val blue = img.blue(x, y)

//        println("$x,$y -> $red $green $blue")

//            aprint(red.binary(8))
//            print(" ")
//            aprint(green.binary(8))
//            print(" ")
//            aprint(blue.binary(8))
//            println()

            aprint((red - 72).binary(1))
            aprint((green - 72).binary(1))
            aprint((blue - 72).binary(1))

//            if (reds.isNotEmpty()) {
//                aprint(((red > (reds.last()))).bin)
//                aprint(((green > (greens.last()))).bin)
//                aprint(((blue > (blues.last()))).bin)
//            }

//            val d = 75
//            aprint(if (red != d) "1" else "0")
//            aprint(if (blue != d) "1" else "0")
//            aprint(if (green != d) "1" else "0")
//        aprint((blue != green).bin)
//            aprint((green % 2 == 0).bin)
//            aprint((green == 77).bin)

            reds += red
            greens += green
            blues += blue

            for ((dx, dy) in listOf(0 to 1, 1 to 0, 0 to -1, -1 to 0, 1 to -1, 1 to 1, -1 to 1, -1 to -1)) {
                val i = x + dx
                val j = y + dy
                if (i == x && j == y) continue

//                if (img.red(i, j) > 100) continue

                if (abs(img.red(i, j) - red) > 10) continue
                if (abs(img.green(i, j) - green) > 10) continue
                if (abs(img.blue(i, j) - blue) > 10) continue

                if ((i to j) in visited) continue

                x = i
                y = j
                continue@img
            }
            break@img
        }

    }

    val inverted = result.map { (it == '0').bin }.joinToString("")
    println()
    println()
    println()
    println(result.asBinary())
    println()
    println(inverted)
    println(inverted.asBinary())
    println()
    println(result.reversed())
    println(result.reversed().asBinary())
    println()
    println(inverted.reversed())
    println(inverted.reversed().asBinary())

    println()
    println(reds.histogram())
    println(greens.histogram())
    println(blues.histogram())
    println(result.length/8)

//    (visited.minOf { it.second }..visited.maxOf { it.second }).forEach { y ->
//        (visited.minOf { it.first }..visited.maxOf { it.first }).forEach { x ->
//            print(if (x to y in visited) "#" else " ")
//        }
//        println()
//    }

}

private fun String.asBinary() =
    windowed(8, 8).map { Integer.parseInt(it, 2).toChar() }.joinToString("")


private fun <T : Comparable<T>> Iterable<T>.histogram() =
    groupBy { it }.mapValues { it.value.size }.entries.sortedBy { it.key }


fun BufferedImage.red(x: Int, y: Int) = Color(getRGB(x, y)).red
fun BufferedImage.green(x: Int, y: Int) = Color(getRGB(x, y)).green
fun BufferedImage.blue(x: Int, y: Int) = Color(getRGB(x, y)).blue

fun Int.binary(length: Int) = toBigInteger().toString(2).padStart(length, '0').takeLast(length)
val Boolean.bin get() = if (this) '1' else '0'