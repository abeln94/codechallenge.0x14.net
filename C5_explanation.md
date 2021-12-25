# Challenge 5 - Invictus
- Contest: https://codechallenge.0x14.net/Challenges?id=5
- Author: Abel Naya

## Explanation
I stumbled on this one for quite some time, but in the end it was far easier than I had though.
The file contains extra UTF8 characters outside the usual range of common english letters (0-128). You can see them with a hex editor.

The solution implemented was by trial and error, manually checking the list of bytes value and trying to find an operation (byte+x or x-byte) that converted them to something with 'MANDELA'. After several tests, the conversion 'byte - 56366' (using only the bytes greater than 128 and not 56128) returned the correct result.