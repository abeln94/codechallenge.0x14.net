# Challenge 11 - ALOT Another library of tools
- Contest: https://codechallenge.0x14.net/Challenges?id=11
- Author: Abel Naya

## Explanation
For this challenge we are required to split a list of strings into groups of K elements, where the length of the common prefix in each group is maximum. A bruteforce solution (check every grouping, aka each combination) is infeasible, so instead the algorithm solves the problem directly as follows:

1) Initialize all strings into a list.
2) From the list of strings, take the K with the longer prefix (if multiple take just one, later we will take the others). This is done recursively starting with T=0:
   1) First group each line based on the T (T=0 means first) character (g->[get,group,...], s->[set, split,...], ...). If a string is shorter than T consider the character is a space.
   2) Take the group with the bigger size greater or equal to K (or any of them if multiple). If there are no groups with K or more strings, return any K element of the list, which have prefix length T, and stop.
   3) If all the strings are T characters length, return any K strings, which have prefix length T.
   4) Otherwise, repeat this process for T+1.
3) Remove those K strings from the list of strings, and mark its length (the algorithm already gives us the length), repeat step 2 if there are still strings in the list.
4) Once there are no more strings, the sum of the marked lengths is the solution.

Example of 1 iteration with K=2:
- T=0 `[getA,gotB,git,setC]` --> `g->[getA,gotB,git], s->[setC]` => take 'g' 
- T=1 `[getA,gotB,git]` --> `e->[getA], o->[gotB], i->[git]` --> no group with 2 or more strings, return any 2 `[getA,gotB]` with common prefix length T=1