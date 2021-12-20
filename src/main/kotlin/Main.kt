/**
 * 1) Picture without alpha, no alpha hidden
 * 2) PNG with valid IEND at the end, no hidden data
 * 3) Tested some color modifications, nothing obvious, the line seem to be a specific greyscale
 * 4) The line is a separate colorspace, and without aliasing (the rest of the image is). Something hidden here.
 * 5) Made a program to traverse the line and report the pixels in order
 * 6) Trying to convert the pixels to something in binary that when decoded produces something
 *
 * 7) After lots (LOTS) of tests, the git first commit is output???? what????
 * 8) New tablet, now in color, just a bit of tweaking of the traversal algorithm and I'm good to go
 * 9) Same git output, but now the payload is a random bytecode, what if I write is as file...
 * 10) Aja! It's a gzip (the header 1f  8b  08). Another tablet, third tweak...
 * 11) Another compressed text
 *
 * 12) Finally, after almost decoding the lzw algorithm by myself, I did it
 *
 * thanks for the challenge
 */
import java.awt.Color
import java.io.File
import java.security.MessageDigest
import javax.imageio.ImageIO
import kotlin.experimental.or
import kotlin.math.abs

// ----------------- challenges ------------ //

/**
 * Main
 */
fun main() {
    firstTablet()
    secondTablet()
    thirdTablet()
}

/**
 * Solves the first tablet
 */
fun firstTablet() {
    // decode and print
    val result = decodeTablet("hidden_toy_story.png")
    println(result.asBinary())
}

/**
 * Solves the second tablet
 */
fun secondTablet() {
    // decode and print
    val result = decodeTablet("9788b1d0ecc849920aae9aa182e8ce54088d3684f2af994d1525223f313318c6.png")
    println(result.asBinary())

    // then write the data into a file
    File("output.zip").writeBytes(result.drop(20 * 8).take(274 * 8).asBytes())
}

/**
 * Solves the third tablet
 */
fun thirdTablet() {
    // decode and print
    val result = decodeTablet("056deccabd65794ad9f54c379c03912b2c81d60938a5e7c85086e45094e93a5c.png")
    println(result.asBinary())

    // then decode the LZW
    val final = decodeLZW(result.drop(20 * 8).take(607 * 8))
    println(final)

    // finally calculate the sha256 and write to file
    File("output.txt").writeText(final.sha256())
}

// ------------ algorithms ------------- //

/**
 * Decodes a generic tablet for the challenge
 */
fun decodeTablet(file: String) = buildString {

    // read image
    val img = ImageIO.read(File(file))

    // initial data (hardcoded position)
    val visited = mutableSetOf<Pair<Int, Int>>()
    var x = 81
    var y = 114

    // path algorithm, follow points (until there are no new points to follow)
    while (x to y != visited.lastOrNull()) {

        // mark as visited and get the color
        visited += x to y
        val color = Color(img.getRGB(x, y))
        val red = color.red
        val green = color.green
        val blue = color.blue

        // append its last bits to the output string
        append((red % 2 != 0).bin)
        append((green % 2 != 0).bin)
        append((blue % 2 != 0).bin)

        // for each neighbour
        for ((dx, dy) in listOf(
            // 4 sides
            0 to 1, 1 to 0, 0 to -1, -1 to 0,
            // 4 diagonals
            1 to -1, 1 to 1, -1 to 1, -1 to -1,
            // 'jumps'
            0 to 2, 2 to 0, 0 to -2, -2 to 0
        )) {

            // get next point
            val i = x + dx
            val j = y + dy

            // ignore already visited
            if ((i to j) in visited) continue

            // check if the color difference is small, otherwise skip
            val newColor = Color(img.getRGB(i, j))
            if (abs(newColor.red - red) > 5) continue
            if (abs(newColor.green - green) > 5) continue
            if (abs(newColor.blue - blue) > 5) continue

            // mark as next point and repeat
            x = i
            y = j
            break
        }
    }

    // for debug: print the full path
    (visited.minOf { it.second }..visited.maxOf { it.second }).forEach { py ->
        (visited.minOf { it.first }..visited.maxOf { it.first }).forEach { px ->
            // print visited nodes
            print(if (px to py in visited) "#" else " ")
        }
        println()
    }
}

/**
 * Decodes a LZW string
 * 'Funny' thing, I discovered the algorithm by myself.
 * The 18, the 3, the 1=literal 0=code, the position of each command bit, etc
 * But there is a negative index at the beginning that was impossible to decipher
 * Later, I remembered the video hint and found that the algorithm ... is exactly as I discovered, ouch, but nice I guess
 * It turns out there is a negative padding of...spaces, so that was it
 */
fun decodeLZW(text: String) = buildString {
    // prepare data to read as a fifo queue
    val window = ArrayDeque(text.windowed(8, 8))

    // prepare the 'negative' sliding window data as 18 spaces. This is what I couldn't discover myself
    val data = " ".repeat(18).toMutableList()

    // now start decoding
    while (window.isNotEmpty()) {
        // take the next command, reverse and focus on each binary digit
        for (command in window.removeFirst().reversed()) {
            // skip if there is no more data to read
            if (window.isEmpty()) break

            // if the digit is 1, it's a literal
            if (command == '1') {
                // just get the data and append it directly (to the data and to the output)
                val char = window.removeFirst().toChar()
                data += char
                append(char)
            } else {
                // if '0', it's a sliding window command [aaaa bbbb  cccc dddd]
                val c = window.removeFirst() + window.removeFirst()
                // the position is 18 + [cccc aaaa bbbb], but the sliding window starts at -18 so we need to add 18 twice
                val p = 18 * 2 + (c.substring(8, 12) + c.take(8)).as3Int()
                // the length is [dddd]
                val l = 3 + c.takeLast(4).toUByte(2).toByte()

                // get message and append (data & output)
                val message = data.subList(p, p + l).joinToString("")
                message.toCharArray().forEach { data += it }
                append(message)
            }
        }
    }
}

// ------------ functions -------- //

/**
 * if true, '1' else '0'
 */
val Boolean.bin get() = if (this) '1' else '0'

/**
 * Converts a 0/1 string of 3 bytes into an int
 */
// we need to extend the sign to 4 bytes, but then can't parse as int (negative is not allowed)
// so instead parse as long and cast
private fun String.as3Int() = (get(0).toString().repeat(20) + this).toLong(2).toInt()

/**
 * Converts a 0/1 string into a byte array
 */
private fun String.asBytes(): ByteArray {
    // for some reason the easy parseInt doesn't work correctly here
    //    return windowed(8, 8).map { Integer.parseInt(it, 2).toByte() }.toByteArray()

    // so instead use a manual approach
    val data = ByteArray(length / 8)
    for (i in indices) {
        val c = get(i)
        if (c == '1') {
            data[i.shr(3)] = data[i.shr(3)].or(0x80.shr(i.and(0x7)).toByte())
        } else if (c != '0') {
            throw IllegalArgumentException("Invalid char in binary string")
        }
    }
    return data
}

/**
 * Converts a 0/1 string into a char
 */
private fun String.toChar() = Integer.parseInt(this, 2).toChar()

/**
 * Converts a 0/1 string into a string (convert each 8 bytes to a char)
 */
private fun String.asBinary() =
    windowed(8, 8).map { it.toChar() }.joinToString("")

/**
 * Calculates the sha256 of the string
 */
fun String.sha256(): String {
    // just using the java function
    return MessageDigest
        .getInstance("SHA-256")
        .digest(toByteArray())
        // and then formatting as string
        .joinToString("") { it.toUByte().toString(16).padStart(2, '0') }
}