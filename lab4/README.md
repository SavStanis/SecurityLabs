# Lab 4 Human-like password generator

### Part 2
I took 3 files with hashes from [bog-dan-git](https://github.com/bog-dan-git) and guessed that one of them contained just md5 hashes, 
second one md5 hashes with salt and the last one with bcrypt hashes. To restore passwords from hashes I installed [hashcat](https://hashcat.net/hashcat/). 
First, I tried to use a brute force attack on md5 hashes, but after seeing ending processing time predictions, I decided to move to more effective attacks.
So I downloaded a file with the top 10kk most popular passwords and tried to use a dictionary attack. For md5 without salt I restored passwords from `55188 / 65121 (84.75%)` of hashes.
Then I tried to use the same attack but on the bcrypt hashes. Fortunately, hashcat writes founded passwords to a separate file, because according to hashcat predictions 
it would have taken several days to go through all possible options in the dictionary. I stopped the attack after an hour. By that time I had restored `15/1000 (1.5%)` passwords from hashes.
Obviously, they were the simplest ones. 