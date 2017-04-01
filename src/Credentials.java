import javafx.beans.property.SimpleStringProperty;

// Simple class for holding credentials

public class Credentials {
	
	private final SimpleStringProperty Username;
	private final SimpleStringProperty URL;
	private final SimpleStringProperty Password;
	
	public Credentials(String x, String y, String z) {
		this.URL = new SimpleStringProperty(x); 
		this.Username = new SimpleStringProperty(y);
		this.Password = new SimpleStringProperty(z);
	}

	public String getUsername(){ return Username.get(); }
	
	public void setUsername(String username){ Username.set(username); }
	
	public String getURL(){ return URL.get(); }
	
	public void setURL(String url){ URL.set(url); }
	
	public String getPassword(){ return Password.get(); }
	
	public void setPassword(String password){ Password.set(password); }
}