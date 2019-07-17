package org.warn.utils.swing;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JTextField;

public class UICommons {
	
	public static final String OS_PROP_NAME = "os.name";
	public static final String MAC_OS_NAME = "mac";
	
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
	
	public static int getControlCommandMask() {
		int controlCommandMask = ActionEvent.CTRL_MASK;
		String osType = System.getProperty( OS_PROP_NAME ).toLowerCase();
		if( osType.startsWith( MAC_OS_NAME ) ) {
			controlCommandMask = ActionEvent.META_MASK;
		}
		return controlCommandMask;
	}

}
