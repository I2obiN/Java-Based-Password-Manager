Java based Password Management Tool I made for a College project, pretty rough but somewhat functional

## 01/04/2017
   Status:
   Going to make this into more of a very basic password management tool. I've updated it significantly since the last update.

## 22/02/2017
   Status:
   Going to drop support for Internet Explorer, Microsoft actually have a built-in tool in Windows for recovering credentials from
   the vault which makes doing password recovery for Windows credentials a little redundant. 
   At this point I'm going to refine existing code further and begin documentation.

## 13/02/2017
   Status:
   Began implementing Internet Explorer/Edge password recovery function. Microsoft credentials are located in an operating system
   vault that uses the DPAPI (Win32 API) based encryption. Should be a relatively similar process to Chrome but I'm having difficulty
   figuring out how to extract the password blobs and decrypt them, although I have found the file location.

## 06/02/2017
   Status:
   Long break for exams and family stuff. Firefox script is working with a slight tweak, Java is simply going to execute
   python.exe and run the script. Going to probably cause some portability issues but nothing that can't be overcome
   ultimately. For our purposes it will do fine. At this point I'm going to move onto Internet Explorer. Once that is
   finished I'll look into OS passwords.

## 12/12/2016
   Status:
   Using https://github.com/Unode/firefox_decrypt to decrypt Firefox passwords. Made executable using py2exe,
   and I'm simply going to execute that and the store the results by redirecting the output to a Java structure.

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
   Status:
   jar file will decrypt Chrome passwords and output to text file in working directory
