# Challenge 19 - Code Red Chaos
- Contest: https://codechallenge.0x14.net/Challenges?id=19
- Author: Abel Naya

## Explanation
The most difficult part of this challenge is to understand what is required.

We need to find the bigger polynomial that allows us to not modify the crc when applying some noise.
This is the equivalent as saying we need to find the bigger polynomial that zeroes (when applying crc) all the noise, because CRC(message^noise^0..0)=CRC(message)^CRC(noise)^CRC(0..0), and since CRC(noise)=0 then CRC(message^noise)=CRC(message).

The CRC operation is just as a 'simple' division, where the result (the CRC output) is equivalent to the remainder of the operation. So we 'simply' need to find the gcd (great common divisor) of all the polynomial noises, which can be calculated sequentially gcd(1,2,3) = gcd(gcd(1,2),3) etc.

To find the gcd, we can use the Euclidean algorithm which just so happens to only require the modulo operation. Exactly what we know how to do! (CRC)

So, to recap:
1) Get all the noise
2) Apply gcd to each pair of noise until only one is left
   1) to apply gcd use the euclidean algorithm using crc itself as the modulo operation (divide the greater into the smaller until the result is 0)
3) The result is the bigger polynomial that solves the problem

Except there is one extra requirement: the polynomial must end in '1'.
So, if the resulting polynomial ends in 0, it means it isn't valid. But in that case we just need to remove the extra leading zeros, because a polynomial still works when adding or subtracting zeros (if the length fits).

so
4) Remove all leading zeros of the output (in binary), and write it as hex.