# PasswordRecoveryTool
Java Password Recovery Tool for Chrome/Firefox/IE.
Over time I might expand this to light OS password cracking. Need to do more research on Firefox and IE cracking first.

I'm not responsible for how you use this tool.

## 05/10/2016
   Status:
   Progress with Firefox is slow, NSS is a complete nightmare to use in Java. Struggling to initialize it
   and get it up and running. Ended up using nss3.dll which was in the 64-bit Firefox install thankfully.
   Still struggling to set the config file for the Java NSS library, may try JSS again but ..
   At this point it would just be easier to simply use a Python script and simply execute it in Java, but that
   kind of defeats the purpose of the project.
   
   Goal: Get NSS initialized

## 19/09/2016

 -- v0.1
   Status:
   jar file will decrypt Chrome passwords and output to text file in working directory

   Goal:
   Firefox next, get the hard one out of the way first I guess

## Steps:

  1. Download as zip, unzip to Desktop or wherever you'd like.
   
  2. Run jdec.jar
   
  3. Open output.txt, enjoy your decrypted password chain(s)

