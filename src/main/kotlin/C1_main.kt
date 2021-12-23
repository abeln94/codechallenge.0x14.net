import java.io.File

fun main() {
    run("sample")
    run("test")
    run("submit")
}

private fun run(data: String) {
    // prepare output file
    File("C1_${data}_out.txt").printWriter().use { out ->
        var index = 0
        // prepare input file
        File("C1_${data}_in.txt").forEachLine { line ->
            // convert each
            if (index != 0) out.println(convert(line, index))
            index++
        }
    }
}

/**
 * Convert each line
 */
private fun convert(line: String, index: Int): String {
    return "Case #$index: " + line
        // split the input by ':'
        .split(':')
        // add both sides
        .sumOf { it.toInt() }
        // and output the sum + 1 unless already 12
        .let { if (it < 12) (it + 1).toString() else "-" }
}