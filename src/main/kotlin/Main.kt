import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO


fun main() {
    val img = ImageIO.read(File("hidden_toy_story.png"))

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
                if (img.red(i, j) > 100) continue
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
    println(visited.size)

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