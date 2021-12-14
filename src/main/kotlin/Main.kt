import java.io.File
import kotlin.streams.toList

fun main() {
    // write output file
    File("output.txt").writeBytes(
        // read input file
        File("Invictus.txt").readText(Charsets.UTF_8)
            // get extra chars
            .chars().filter { it > 128 }
            // convert chars
            // these settings were found by trial and error, unfortunately
            .filter { it != 56128 }.map { it - 56366 }
            // back to byte, and write
            .toList().map { it.toByte() }.toByteArray()
    )
}
