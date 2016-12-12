# PasswordRecoveryTool
Java Password Recovery Tool for Chrome/Firefox/IE.

## 21/11/2016
   Status:
   Switching to Jython to execute Python script to decrypt Firefox username and passwords. 
   As far as I can tell Mozilla do not have NSS dlls readily available for my platform so I'm
   just going to cut my losses and use one of the many Python scripts that are out there. Going
   to begin work on it tomorrow. Briefly attempted to migrate to C++ with little success, typical of
   C++ the code has become bloated already and takes probably 20 lines to do what Java or Python can do in 3.

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
