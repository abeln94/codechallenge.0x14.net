# Challenge 8 - Awesome Sales Inc.!
- Contest: https://codechallenge.0x14.net/Challenges?id=8
- Author: Abel Naya

## Explanation
The problem asks to find which cities, when removed, will make some paths impossible. This is the same as finding the nodes of an undirected graph which will split it into two separated non-connected subgraphs. Those special nodes are called articulation points (or cut vertices or separation vertices). This problem is common and you can find some solutions already available online. After some searching and reviewing I found [this solution](https://www.geeksforgeeks.org/articulation-points-or-cut-vertices-in-a-graph/) which solves correctly the problem in Java.

The implemented solution parses the data and converts it to an adjacency matrix, which is then feed into the algorithm, and the result is then parsed and printed as specified.

Note: I could have implemented it myself, or probably could have found a library and use it instead, but this way the file can be executed without external dependencies which was faster for a time-limited challenge.