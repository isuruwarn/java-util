package org.warn.utils.swing;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JTextField;

public class UICommons {
	
	
	public static void chooseDirectory( JTextField pathTxt ) {
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
		int returnVal = fc.showOpenDialog(null);
		if( returnVal == JFileChooser.APPROVE_OPTION ) {
			File file = fc.getSelectedFile();
			pathTxt.setText( file.getPath() );
		}
	}
	
	public static void chooseFile( JTextField pathTxt ) {
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode( JFileChooser.FILES_ONLY );
		int returnVal = fc.showOpenDialog(null);
		if( returnVal == JFileChooser.APPROVE_OPTION ) {
			File file = fc.getSelectedFile();
			pathTxt.setText( file.getPath() );
		}
	}


}
