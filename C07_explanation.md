# Challenge 7 - Escape or Die
- Contest: https://codechallenge.0x14.net/Challenges?id=7
- Author: Abel Naya

## Note
Even though the test and submissions are the same (so if you solve the test you know the submission should be right) I failed this challenge. The reason is that, after completing the challenge and checking it was valid, I refactored, cleaned and commented the code (to improve its quality). In the process I changed the string output converter and forgot a space between the (x, y), which made the output algorithmically correct, but textually invalid. (funny thing, for the latest challenges I specifically made sure the output before and after the refactoring was the same, unfortunately I didn't do it here). The code contains the one-line fix.

## Explanation
This challenge is a labyrinth solver, but using a remote connection instead of local data. The algorithm is a Breadth First Tree Traversal:
1) Initializes a lifo list of states with an initial element as (0, 0, null, []) (x=0, y=0, no direction, no cells visited).
2) While there are states, pop the most recent one (x,y,direction,visited) and:
   1) Calculate the next cell position (except for the first visited node where direction is null).
   2) If the next position is already in the visited list, skip and continue step 2 for next state
   3) send 'go to x,y' and 'direction'. Note: it could be possible that the next position was different that the calculated in step 1 (for example if the grid wasn't a matrix) but it seems it wasn't the case. If it were the command should be sent and the result parsed before the visited check.
   4) Mark the new cell as visited and send 'is exit?'
      1) If exit, print the visited cells and exit (which is the shortest path because of the fifo state list, aka breadth first traversal).
      2) If not exit, send 'look' and push a new state with each direction.