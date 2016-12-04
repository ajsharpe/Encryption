This is a simple client/server that exchanges encrypted messages from (hardcoded for now) users. 
Written in Java, it uses native function calls written in C using the TEA Algorithm to encrypt and decrypt messages.

To run Client:
`java Client name password`

To run Server:
`java Server`

Passwords must be >=8 characters, as the first 8 characters are used as the encryption key. 


Known Bugs:
- Single 'm' from client messes everything up (every other character on the keyboard seems fine, including arrow keys?)
- Decryption not 100% accurate. weird characters appear sometimes (usually 2 messed up chars in the middle of a string)
- Users are hardcoded for now, should be read in from encrypted file using a key stored in an env variable
