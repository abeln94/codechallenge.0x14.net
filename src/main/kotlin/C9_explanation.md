# Challenge 9 - Collisions
- Contest: https://codechallenge.0x14.net/Challenges?id=9
- Author: Abel Naya

## Explanation
This is a collision checker between several sprites on a grid. The implemented algorithm is as follows:
1) Parse the data into a list of 'sprites' which contains the sprite data (matrix of booleans) and the bounding box of the sprite (left, right, top and bottom sides).
2) For each unique pair of sprites (ignoring order, aka combinations) check if they collide:
   1) Calculate the collision bounding box, by taking the maximum of the left sides, the max of the tops, the min of the right and the min of the bottom.
   2) For each pixel in the collision bounding box, check if both sprites collide (both have a pixel there). Note that the collision bounding box may be empty, in that case there is no collision.
   3) Count all collisions and print the result.

Note: this algorithm is theoretically slow, compares all pairs of sprites even if they are far away (although in that case the collision bounding box is empty so no further checks are made). In order to improve the speed I could have implemented a spacial hashing division or other methods, however both inputs take a few seconds to compute and the optimization will probably take quite a few minutes to implement, so for a time-limited challenge I decided to just leave it like this.