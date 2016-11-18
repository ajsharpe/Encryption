This is a simple client/server that exchanges encrypted messages from (hardcoded for now) users. 
Written in Java, it uses native function calls written in C using the TEA Algorithm to encrypt and decrypt messages.

To run Client:
`java Client name password`

To run Server:
`java Server`

Passwords must be >=8 characters, as the first 8 characters are used as the encryption key. 
