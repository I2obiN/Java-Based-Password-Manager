import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.simple.parser.ParseException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
 
public class Crypt32Dec extends Application {
	
    private TableView<Credentials> table = new TableView<Credentials>();
    private TableView<Credentials> table2 = new TableView<Credentials>();
    private TableView<Credentials> table3 = new TableView<Credentials>();
    
    private ObservableList<Credentials> data = FXCollections.observableArrayList();
    private ObservableList<Credentials> data2 = FXCollections.observableArrayList();
    private ObservableList<Credentials> data3 = FXCollections.observableArrayList();
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void start(Stage primaryStage) {
    	
    	JDec dec = new JDec();
    	RemoteFunc rmFunc = new RemoteFunc();
        GridPane grid=new GridPane();
         
        final Label label = new Label("Firefox Passwords");
        final Label label2 = new Label("Google-Chrome Passwords");
        final Label label3 = new Label("Remote Passwords");
        
        label.setFont(new Font("Arial", 20));
        label2.setFont(new Font("Arial", 20));
        label3.setFont(new Font("Arial", 20));
  
        TableColumn URLCol = new TableColumn("URL");
        URLCol.setMinWidth(100);
        URLCol.setCellValueFactory(new PropertyValueFactory<Credentials, String>("URL"));
 
        TableColumn UsernameCol = new TableColumn("Username");
        UsernameCol.setMinWidth(100);
        UsernameCol.setCellValueFactory(new PropertyValueFactory<Credentials, String>("Username"));
 
        TableColumn PasswordCol = new TableColumn("Password");
        PasswordCol.setMinWidth(200);
        PasswordCol.setCellValueFactory(new PropertyValueFactory<Credentials, String>("Password"));
        
        TableColumn URLCol2 = new TableColumn("URL");
        URLCol2.setMinWidth(100);
        URLCol2.setCellValueFactory(new PropertyValueFactory<Credentials, String>("URL"));
 
        TableColumn UsernameCol2 = new TableColumn("Username");
        UsernameCol2.setMinWidth(100);
        UsernameCol2.setCellValueFactory(new PropertyValueFactory<Credentials, String>("Username"));
 
        TableColumn PasswordCol2 = new TableColumn("Password");
        PasswordCol2.setMinWidth(200);
        PasswordCol2.setCellValueFactory(new PropertyValueFactory<Credentials, String>("Password"));
        
        TableColumn URLCol3 = new TableColumn("URL");
        URLCol3.setMinWidth(100);
        URLCol3.setCellValueFactory(new PropertyValueFactory<Credentials, String>("URL"));
 
        TableColumn UsernameCol3 = new TableColumn("Username");
        UsernameCol3.setMinWidth(100);
        UsernameCol3.setCellValueFactory(new PropertyValueFactory<Credentials, String>("Username"));
 
        TableColumn PasswordCol3 = new TableColumn("Password");
        PasswordCol3.setMinWidth(200);
        PasswordCol3.setCellValueFactory(new PropertyValueFactory<Credentials, String>("Password"));
        
        table.setMaxHeight(200);
        table.setItems(data);
        
        table2.setMaxHeight(200);
        table2.setItems(data2);
        
        table3.setMaxHeight(200);
        table3.setItems(data3);
 
        table.getColumns().addAll(URLCol, UsernameCol, PasswordCol);
        table2.getColumns().addAll(URLCol2, UsernameCol2, PasswordCol2);
        table3.getColumns().addAll(URLCol3, UsernameCol3, PasswordCol3);
        GridPane.setConstraints(table, 1, 0);
        GridPane.setConstraints(table2, 2, 0);
        GridPane.setConstraints(table3, 1, 1);
  
        final VBox vbox = new VBox();
        Button btn = new Button();
        Button btn2 = new Button();
        Button btn3 = new Button();
        
        final Label inputText = new Label("URL/Login/Password:");
        
        TextField URLinput = new TextField();
        TextField logininput = new TextField();
        TextField passinput = new TextField();
        
        GridPane.setConstraints(btn, 0, 1);
        GridPane.setConstraints(btn2, 1, 2);
        GridPane.setConstraints(btn3, 0, 3);
        GridPane.setConstraints(logininput, 0, 4);
        GridPane.setConstraints(passinput, 0, 5);
        
        btn.setText("Retrieve Local Passwords");
        btn2.setText("Retrieve External Passwords");
        btn3.setText("Send Password");
        
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table, label2, table2, label3, table3, 
        	btn, btn2, btn3, inputText, URLinput, logininput, passinput);
        grid.getChildren().add(vbox);
        
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
               try {
            	   
				ArrayList<Credentials> ChromeDetails = dec.ChromeDec();
				ArrayList<Credentials> FirefoxDetails = dec.FirefoxDec();
				
				for(Credentials Details : FirefoxDetails){
				data.add(new Credentials(Details.getURL(), Details.getUsername(), Details.getPassword()));
				}
				
				for(Credentials Details2 : ChromeDetails){
				data2.add(new Credentials(Details2.getURL(), Details2.getUsername(), Details2.getPassword()));
				}
				
               	} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
               	} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
               } catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (GeneralSecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
        
        btn2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	
                ArrayList<Credentials> remotePasses = new ArrayList<Credentials>();
                remotePasses = rmFunc.retrievePass();
                
                for(Credentials Details3 : remotePasses){
               	data3.add(new Credentials(Details3.getURL(), Details3.getUsername(), Details3.getPassword()));
                }
            }
        });
        
        btn3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	if(logininput.lengthProperty().get() > 0 && 
            			passinput.lengthProperty().get() > 0 && 
            				URLinput.lengthProperty().get() > 0) {
            		
            		// If query return 1 == SQL success
            		if(rmFunc.passUpload(URLinput.getText(), logininput.getText(), passinput.getText())) {
            		data3.add(new Credentials(URLinput.getText(), logininput.getText(), passinput.getText()));
            		}
            		else { System.out.println("Error: SQL Query Failed"); }
            	}
            	else{
                System.out.println("Error: Empty Field");
            	}
            }
        });
         
        StackPane root = new StackPane();
        root.getChildren().add(grid);
         
        Scene scene = new Scene(root, 500, 1000);
         
        primaryStage.setTitle("Password Management Tool");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}