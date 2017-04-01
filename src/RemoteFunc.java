import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RemoteFunc {
	
	private Connection con = null;
	private Statement st = null;
	private Statement st2 = null;
	
	// Demo Server -- Obviously do not use this for production or actual storage
	String url = "jdbc:mysql://localhost:3306/test?autoReconnect=true&useSSL=false";
	String dbusername = "root";
	String dbpassword = "Terminator10@";
	
	public boolean passUpload(String dbURL, String login, String password){
		
		try {
			con = DriverManager.getConnection(url, dbusername, dbpassword);
			st = con.createStatement();
			
			String query = "INSERT INTO test.credentials(URL, username, password) VALUES(" + "\"" +
					dbURL + "\"" + ", " + "\"" + login + "\"" + ", " + "\"" + password + "\"" + ");";
			
			if(st.executeUpdate(query) == 1){ return true; }
			else return false;
			
		} catch(SQLException e){
			Logger lgr = Logger.getLogger(RemoteFunc.class.getName());
			lgr.log(Level.SEVERE, e.getMessage(), e);
		} finally {
			try {
				if(st != null){ st.close(); }
				if(con != null){ con.close(); }
			} catch (SQLException e){
				Logger lgr = Logger.getLogger(RemoteFunc.class.getName());
				lgr.log(Level.WARNING, e.getMessage(), e);
			}
		}
		
		return false;	
	}
	
	public ArrayList<Credentials> retrievePass(){
		try {
			con = DriverManager.getConnection(url, dbusername, dbpassword);
			st2 = con.createStatement();
			
			String query = "SELECT url, username, password FROM test.credentials;";
			
			ResultSet resultSet = st2.executeQuery(query);
			ArrayList<String> results = new ArrayList<String>();
			
			// Add queried credentials to ArrayList
			while(resultSet.next()){
				for(int z = 1; z < 4; z++){
				results.add(resultSet.getString(z));
				}
			}
			
			String[] resultArray = new String[results.size()];
			for(int i = 0; i < results.size(); i++){
			resultArray[i] = results.get(i);
			}
			
			ArrayList<Credentials> credResults = new ArrayList<Credentials>();
			for(int i = 0; i < resultArray.length; i+=3){
				credResults.add(new Credentials(resultArray[i], resultArray[i+1], resultArray[i+2]));
			}
			
			return credResults;
			
		} catch(SQLException e){
			Logger lgr = Logger.getLogger(RemoteFunc.class.getName());
			lgr.log(Level.SEVERE, e.getMessage(), e);
		} finally {
			try {
				if(st != null){ st.close(); }
				if(con != null){ con.close(); }
			} catch (SQLException e){
				Logger lgr = Logger.getLogger(RemoteFunc.class.getName());
				lgr.log(Level.WARNING, e.getMessage(), e);
			}
		}
		
		return null;
	}
}