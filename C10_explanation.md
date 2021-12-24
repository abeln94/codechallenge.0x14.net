# Challenge 10 - Packets delivery
- Contest: https://codechallenge.0x14.net/Challenges?id=10
- Author: Abel Naya

## Explanation
No source code this time, the challenge requires us to analyze a pcap file for an 'experimental' protocol (probably the funniest challenge to read). The steps I made were as follows:

1) Open wireshark, load the pcap file.

- There are multiple packages, all seems to contain a single byte of data, the protocol says that a IP packet is split and sent.

2) Extract all the data bytes, concatenate as bin hex (binary file), load in wireshark again ... nothing (the data doesn't seem to be valid).

- After a bit of testing and documentation, I saw the sequence number of the packages is not in timestamp order. I guess some 'avian carriers' were lazy or slower.

3) Sort the packets by sequence number instead of timestamp.

4) Extract all the data, concatenate as bin hex and load in wireshark again ... nothing. It seems the data is still not a valid IP dataframe.

- But then I notice that the binary file generated in the previous step ... is a qr image!

5) Scan the qr and get the solution. Bingo!