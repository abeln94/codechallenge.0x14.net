# Challenge 20 - Hidden toy story
- Contest: https://codechallenge.0x14.net/Challenges?id=20
- Author: Abel Naya

## Explanation
Even though it took me the longest to solve, it was because I misread a hint and solved a challenge which wasn't needed. Here are the steps I took:

- The input is a picture without alpha, so no alpha hidden.
- The PNG has valid IEND at the end, so no hidden data
- I tested some color modifications, nothing obvious but the line seem to be a specific greyscale
- The line is a separate colorspace, and without aliasing (the rest of the image is). Definitely something hidden here.
1) I made a program to traverse the line and report the pixels in order.
- I then tried to convert the pixels to something in binary that when decoded produced something. Take the colors, reverse them, invert, reverse and invert...
2) After lots (LOTS) of tests, the git first commit was printed on the screen. You need to take the last bit of each pixel (rgb) in order. I was socked, but then I found the message that explains there is another tablet. There was also a link to a YouTube video, which I checked and found it was about learning how to decode an unknown algorithm.
3) I downloaded the new tablet, now in color, and with just a bit of tweaking of the traversal algorithm (follow pixels similar in color instead of grayscale) and then the same conversion, it printed the new decoded output.
4) Same git output, but now the payload is like a random bytecode. Seems binary, and the header was 1f 8b 08, the header of a gzip file, so I just wrote it as binary.
- After unzipping, the inside file explains there is just one more tablet.
6) Another tweak of the line traversal algorithm (allowing 'jumps' of two pixels) and again valid output.
7) It seems like a binary file again, with strange ascii output, but this time they are not outside the 256 byte range.

- I stumbled at this binary file for a while, and then I remembered the hint video in the first tablet output. It had the same similar ff tokens, and so I thought it was an invented compression technique which I needed to decode (as in the video).

And that's what I did. I took the binary file and with the only 'hints' that it was a linear-feed algorithm where ff probably meant '8 literals next' and if not there were 'holes' that required you to either look a dictionary, or use a sliding window or pointers, I started to decode it. The text started with a toys in the attic transcription, so I had almost the original and encoded text. By hand, I found that the codes meant that '1' is literal and '0' is a two bytes sliding window, where the first 12 bits (but with the last 4 at the beginning) was the position offset 18, and the last 4 the length offset 3. 

I was able to decode the whole string, but there were a single sliding window with a negative offset, which was impossible to deduce (the transcription didn't have that part). I needed to calculate the sha256 of the file, as it said, but there were 5 unknown characters so I couldn't.

I can't describe how I felt, I knew the end of the challenge was just 5 bytes away, but at the same time out of reach.

After a bit of rest I suddenly remembered the hint video from the first tablet. I went to the wiki file they mention ... and [it was there](https://moddingwiki.shikadi.net/wiki/Softdisk_Library_Format#LZW). The code format, the 18 and 3 offset...and what to do with a negative offset: write a space.

8) I implemented the missing negative=space into my algorithm...and the correct output was generated.

So, in the end I made the already hard latest challenge even harder, but that was all my fault, and even though it took a while, I'm surprised I was able to do it.