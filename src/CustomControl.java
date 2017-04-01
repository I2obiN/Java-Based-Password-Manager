import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;

import javafx.scene.control.Control;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Translate;


public class CustomControl extends Control {
	
	//similar to previous custom controls but must handle more
	//complex mouse interactions and key interactions

	private Crypt32Dec gui;
	private Translate pos;
	
	public CustomControl(){
		pos = new Translate();
		setSkin(new CustomControlSkin(this));
		gui = new Crypt32Dec();
	}
}