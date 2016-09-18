# PasswordScrape
Java Password Scraper for Chrome built on JNA and Win32Crypt functions

Steps:

  1. Download as zip, unzip to Desktop or wherever you'd like.

  2. Double-click winscrape.bat or run from cmd line
   
  3. Run jdec.jar
   
  4. Open output.txt, enjoy your decrypted Google-Chrome password chain
  
NOTES: Will probably only work on the machine of the logged in user. You probably can't take a db from another PC, and run jdec.jar on another system to decrypt it although I haven't tested this yet.

Chances are if the output.txt doesn't have the password but others are there, then that BLOB was encrypted on another machine. I don't know tbh, the native Win32Crypt is sketchy af on Java.

Required libraries;
https://maven.java.net/content/repositories/releases/net/java/dev/jna/jna/4.2.2/jna-4.2.2.jar
https://maven.java.net/content/repositories/releases/net/java/dev/jna/jna-platform/4.2.2/jna-platform-4.2.2.jar
https://bitbucket.org/xerial/sqlite-jdbc/downloads
