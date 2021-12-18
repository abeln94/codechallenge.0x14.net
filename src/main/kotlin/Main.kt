import java.io.File

fun convertToMatlab() {

    // prepare output file
    File("matlab.txt").printWriter().use { out ->

        val cases = readInput()

        var matlab = "T = {${cases.size}};"

        cases.forEach { (_, count) ->

            matlab += if (count.size != 1) """
                    
                    symbols = (1:${count.size});
                    prob = [${count.values.joinToString(" ")}]/${count.values.sum()};
                    dict = huffmandict(symbols,prob, 2, 'min');
                    T = [T ; {length(dict)} ; dict(:,2)];
                """.trimIndent()
            else "\n" +
                    "T = [T ; {1} ; {1}];"


        }
        matlab += """
                
                T = cell2table(T)
                writetable(T,'myDataFile.csv')
            """.trimIndent()
        out.println(matlab)
    }
}

fun readInput(): MutableList<Pair<List<String>, Map<String, Int>>> {
    val cases = mutableListOf<Pair<List<String>, Map<String, Int>>>()

    // prepare input file
    File("input.txt").bufferedReader().use { inp ->

        val n = inp.readLine().toInt()


        var matlab = "T = {$n};"

        // for each case
        (1..n).forEach { case ->

            // read instructions
            val instructions = (1..inp.readLine().toInt()).map {
                inp.readLine().substringBefore(" ")
            }
            val count = instructions.groupBy { it }.mapValues { it.value.size }

            cases += instructions to count
        }
    }

    return cases
}


fun main() {
//    convertToMatlab()
    convertFromMatlab()
}

fun convertFromMatlab() {

    // prepare output file
    File("output.txt").printWriter().use { out ->

        val cases = readInput()

        // prepare input file
        File("matlab.txt").bufferedReader().use { inp ->

            inp.readLine()

            // for each case
            (1..inp.readLine().substringBefore(",").toInt()).forEach { case ->

                val sizes = (1..inp.readLine().substringBefore(",").toInt()).map {
                    inp.readLine().split(",").filter { it.isNotBlank() }.size
                }

                val (instructions, count) = cases[case-1]

                val size = instructions.sumOf { 2 + sizes[count.keys.indexOf(it)] }
                val diff = (sizes.maxOrNull() ?: return) - (sizes.minOrNull() ?: return)
                // report output
                out.println("Case #$case: $size, $diff")
            }

        }
    }
}
