internal object Graph {
    var time = 0
    fun addEdge(adj: ArrayList<ArrayList<Int>>, u: Int, v: Int) {
        adj[u].add(v)
        adj[v].add(u)
    }

    fun APUtil(
        adj: ArrayList<ArrayList<Int>>, u: Int,
        visited: BooleanArray, disc: IntArray, low: IntArray,
        parent: Int, isAP: BooleanArray
    ) {
        // Count of children in DFS Tree
        var children = 0

        // Mark the current node as visited
        visited[u] = true

        // Initialize discovery time and low value
        low[u] = ++time
        disc[u] = low[u]

        // Go through all vertices aadjacent to this
        for (v in adj[u]) {
            // If v is not visited yet, then make it a child of u
            // in DFS tree and recur for it
            if (!visited[v]) {
                children++
                APUtil(adj, v, visited, disc, low, u, isAP)

                // Check if the subtree rooted with v has
                // a connection to one of the ancestors of u
                low[u] = Math.min(low[u], low[v])

                // If u is not root and low value of one of
                // its child is more than discovery value of u.
                if (parent != -1 && low[v] >= disc[u]) isAP[u] = true
            } else if (v != parent) low[u] = Math.min(low[u], disc[v])
        }

        // If u is root of DFS tree and has two or more children.
        if (parent == -1 && children > 1) isAP[u] = true
    }

    fun AP(adj: ArrayList<ArrayList<Int>>, V: Int): ArrayList<Int> {
        val visited = BooleanArray(V)
        val disc = IntArray(V)
        val low = IntArray(V)
        val isAP = BooleanArray(V)
        val time = 0
        val par = -1

        // Adding this loop so that the
        // code works even if we are given
        // disconnected graph
        for (u in 0 until V) if (visited[u] == false) APUtil(adj, u, visited, disc, low, par, isAP)
        val result = ArrayList<Int>()
        for (u in 0 until V) if (isAP[u] == true) result.add(u)
        return result
    }

    @JvmStatic
    fun main(args: Array<String>) {

        // Creating first example graph
        var V = 5
        val adj1 = ArrayList<ArrayList<Int>>(V)
        for (i in 0 until V) adj1.add(ArrayList())
        addEdge(adj1, 1, 0)
        addEdge(adj1, 0, 2)
        addEdge(adj1, 2, 1)
        addEdge(adj1, 0, 3)
        addEdge(adj1, 3, 4)
        println("Articulation points in first graph")
        AP(adj1, V)

        // Creating second example graph
        V = 4
        val adj2 = ArrayList<ArrayList<Int>>(V)
        for (i in 0 until V) adj2.add(ArrayList())
        addEdge(adj2, 0, 1)
        addEdge(adj2, 1, 2)
        addEdge(adj2, 2, 3)
        println("Articulation points in second graph")
        AP(adj2, V)

        // Creating third example graph
        V = 7
        val adj3 = ArrayList<ArrayList<Int>>(V)
        for (i in 0 until V) adj3.add(ArrayList())
        addEdge(adj3, 0, 1)
        addEdge(adj3, 1, 2)
        addEdge(adj3, 2, 0)
        addEdge(adj3, 1, 3)
        addEdge(adj3, 1, 4)
        addEdge(adj3, 1, 6)
        addEdge(adj3, 3, 5)
        addEdge(adj3, 4, 5)
        println("Articulation points in third graph")
        AP(adj3, V)
    }
}