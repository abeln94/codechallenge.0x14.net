import java.io.File

fun main() {
    File("output.txt").printWriter().use { out ->
        var index = 0
        File("input.txt").forEachLine { line ->
            if (index != 0) out.println(convert(line, index))
            index++
        }
    }
}

fun convert(line: String, index: Int): String {
    return "Case #$index: " + line
        .split(':')
        .sumOf { it.toInt() }
        .let { if (it < 12) (it + 1).toString() else "-" }
}