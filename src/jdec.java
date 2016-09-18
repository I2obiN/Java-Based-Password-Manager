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
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;


public class jdec extends Crypt32Util {
	
	public static void main(String[] args) throws SQLException, FileNotFoundException{
		
		// Redirect output to text file
		PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
		System.setOut(out);
		
		Connection conn = null;

		// Use arguments string for db url
		String url = "jdbc:sqlite:C:/your/path/logindata.db";
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
		
		byte bytearray[] = new byte[cint];
		
		ResultSet rsurls = stmt1.executeQuery(sqlurls);
		ResultSet rsusernames = stmt2.executeQuery(sqlusernames);
		ResultSet rspassblobs = stmt3.executeQuery(sqlpassblobs);
		
		ResultSetMetaData rsmeta = rsurls.getMetaData();
		int numberofsets = rsmeta.getColumnCount();
		
		// URLs into String array
		int urint = 1;
		String[] urls = new String[cint];
		while(urint!=cint){
			urls[urint] = rsurls.getString(1);
			urint++;
		}
		
		// Usernames into String array
		int unint = 1;
		String[] usernames = new String[cint];
		while(unint!=cint){
			usernames[unint] = rsusernames.getString(1);
			unint++;
		}
		
		int pint = 1;
		
		// Putting BLOBs into byte array
		while(pint!=cint){
			bytearray[pint] = rspassblobs.getByte(1);
			pint++;
		}
		
		// Decrypt data
		byte unencrypted[] = cryptUnprotectData(bytearray);
		
		// Print results
		for(int i = 0; i < cint; i++){
			System.out.println(urls[i] + usernames[i] + unencrypted[i]);
		}

	}
}
