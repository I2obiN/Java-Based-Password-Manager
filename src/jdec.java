import java.util.*;
import java.lang.Object;

import com.sun.jna.*;
import com.sun.jna.platform.win32.*;
import com.sun.jna.platform.win32.WinCrypt.DATA_BLOB;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class jdec extends Crypt32Util {
	
	public static void main(String[] args) throws SQLException{
		
		Connection conn = null;
		
		// URL for password database
		String url = "jdbc:sqlite:./logindata.db";
		conn = DriverManager.getConnection(url);
		
	}
}
