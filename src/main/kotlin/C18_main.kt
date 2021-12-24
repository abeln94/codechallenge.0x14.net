import java.io.File

// The problem ask to generate a minimal huffman dictionary with minimal variance
// good news? there is a function that does exactly that
// bad new? it's in matlab https://es.mathworks.com/help/comm/ref/huffmandict.html
//
// matlab is horrible, ok maybe not, but last time I used it was a long time ago and never wanted to touch it again
// so I made what you should never make (except if you are making a contest and the deadline is ALMOST HEREEEE)
// use kotlin to read the problem and generate ugly matlab code that will solve it and dump as a csv
// then use kotlin again to read and parse the csv
// a good solution should be all in matlab, but as I said, matlab is horrible when you are under pressure

/**
 * Reads and parses the input
 */
fun readInput(data: String): MutableList<Pair<List<String>, Map<String, Int>>> {
    // list of instructions and grouping for each case
    val cases = mutableListOf<Pair<List<String>, Map<String, Int>>>()

    // prepare input file
    File("C18_${data}_input0").bufferedReader().use { inp ->

        // number of cases
        val n = inp.readLine().toInt()

        // for each case
        (1..n).forEach { case ->

            // read instructions
            val instructions = (1..inp.readLine().toInt()).map {
                inp.readLine().substringBefore(" ")
            }
            // generate groupings (count how many instructions of each)
            val count = instructions.groupBy { it }.mapValues { it.value.size }

            // and save
            cases += instructions to count
        }
    }

    // return to sender
    return cases
}

/**
 * Use the problem info to generate 'valid' matlab code
 */
fun convertToMatlab(data: String) {

    // prepare output file
    File("C18_${data}_matlab_in.m").printWriter().use { out ->

        // read input
        val cases = readInput(data)

        // prepare code
        var matlab = "T = {${cases.size}};"

        // for each case
        cases.forEach { (_, count) ->

            // generate the code for each case
            // the huffmandict requires the 'min' parameter to minimize distance between codes (even if the wiki says it isn't necessary...it is!!!)
            // basically calls the function with the correct parameters, and appends it to the csv (T)
            matlab += if (count.size != 1) """
                    
                    symbols = (1:${count.size});
                    prob = [${count.values.joinToString(" ")}]/${count.values.sum()};
                    dict = huffmandict(symbols,prob, 2, 'min');
                    T = [T ; {length(dict)} ; dict(:,2)];
                """.trimIndent()
            // the function requires at least 2 elements, so if there is only one the solution is obvious
            else "\n" +
                    "T = [T ; {1} ; {1}];"

        }

        // last code to dump the result to a csv
        matlab += """
                
                T = cell2table(T)
                writetable(T,'C18_${data}_matlab_out.csv')
            """.trimIndent()
        out.println(matlab)
    }
}

/**
 * Parse the matlab ugly csv into the solution
 */
fun convertFromMatlab(data: String) {

    // prepare output file
    File("C18_${data}_out.txt").printWriter().use { out ->

        // read cases again, we need them
        val cases = readInput(data)

        // prepare input file
        File("C18_${data}_matlab_out.csv").bufferedReader().use { inp ->

            // ignore header
            inp.readLine()

            // for each case
            (1..inp.readLine().substringBefore(",").toInt()).forEach { case ->

                // read all huffman codes of each instruction, calculate its length
                val sizes = (1..inp.readLine().substringBefore(",").toInt()).map {
                    // an entry of "1,0,,,,," means [1,0] aka length 2
                    inp.readLine().split(",").filter { it.isNotBlank() }.size
                }

                // get the original data of this case
                val (instructions, count) = cases[case - 1]

                // the size is the huffman code length of that instructions plus 2 for the arguments
                val size = instructions.sumOf { 2 + sizes[count.keys.indexOf(it)] }
                // the diff is the max minus the min lengths
                val diff = (sizes.maxOrNull() ?: return) - (sizes.minOrNull() ?: return)
                // report output
                out.println("Case #$case: $size, $diff")
            }

        }
    }
}

/**
 * How to solve:
 * Run this once
 * Run the generated C18_{}_matlab_in.m files
 * Run this again
 */
fun main() {

    convertToMatlab("sample")
    convertToMatlab("test")
    convertToMatlab("submit")

    runCatching { convertFromMatlab("sample") }
    runCatching { convertFromMatlab("test") }
    runCatching { convertFromMatlab("submit") }

}