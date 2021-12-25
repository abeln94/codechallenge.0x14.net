# Challenge 4 - Let’s build musical scales
- Contest: https://codechallenge.0x14.net/Challenges?id=4
- Author: Abel Naya

## Explanation
Note: the submitted solution is wrong, it fails with some specific inputs, an explanation and fix is detailed below.

Constant: notes dictionary: `[["A"],["A#", "Bb"],["B", "Cb"],["C", "B#"],["C#", "Db"],["D"],["D#", "Eb"],["E", "Fb"],["F", "E#"],["F#", "Gb"],["G"],["G#", "Ab"]]`

The algorithm is as follows:
1) Read the data, the initial note and the steps.
2) Create an array of 'indexes' from the notes dictionary. For example if the first letter is `g` (index 10) and the steps are sT (+1,+2) build the array `[10,11,1]`
3) Recursively test each note to find a solution which doesn't repeat any letter for different notes. In the example above it will search for "GG#" [error] then "GAbA#" [error] and finally "GAbBb" [success]. Return that solution.

This algorithm fails if the initial note is one of the second elements in the notes dictionary (Bb, Cb, etc) because the algorithm will search using the first note first, and the returned scale will be the same one musically, but different textually. For example G#A#B#C#D#E#F#G# instead of AbBbCDbEbFGbAb. The solution requires to keep the first note without modifications. The code contains the one-line fix.