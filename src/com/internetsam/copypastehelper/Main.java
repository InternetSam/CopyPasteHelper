package com.internetsam.copypastehelper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class Main implements NativeKeyListener {
	
	public String clipBoardData = "";
	public static DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
	public static Date currentDate = new Date();
	public static String formatedDate = dateFormat.format(currentDate);
	public boolean ctrlHeld = false;

	public static void main(String[] args) {
		
		
		
		String operatingSystem = System.getProperty("os.name");
		
		File directory = new File("CopyPasteHelper_Logs");
		File writeFile = new File("CopyPasteHelper_Logs/"+formatedDate+".txt");
		
		System.out.println("OS: " + operatingSystem);
		System.out.println("Date: "+dateFormat.format(currentDate));
		if(directory.exists() && directory.isDirectory()) {
			
			System.out.println("Skipping folder creation - folder already exists"); 
		}else{
			
			System.out.println("Log folder didn't exist - created");
			directory.mkdir();
		}
		
		if(writeFile.exists() && writeFile.isDirectory() != true) {
			
			System.out.println("Skipping log file creation - a log file already exists for the current day");
		}else{
			
			try {
				writeFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
				
		System.out.println("Ready to log!");
		
		try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException n) {

        	System.err.println("JNativeHook could not be registered");
            System.err.println(n.getMessage());
            System.exit(1);
        }

        GlobalScreen.getInstance().addNativeKeyListener(new Main());
		
		
	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent e) {
		
		if(e.getKeyCode() == NativeKeyEvent.VC_CONTROL_L) {
			ctrlHeld = true;
		}
		
		if(e.getKeyCode() == NativeKeyEvent.VC_C && ctrlHeld == true || e.getKeyCode() == NativeKeyEvent.VC_X && ctrlHeld == true) {

			try {
					clipBoardData = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
					try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("CopyPasteHelper_Logs/"+formatedDate + ".txt", true)))) {
					    out.println(clipBoardData+"\n---------------------------------------\n");
					}catch (IOException e1) {

					}
				
			} catch (HeadlessException e2) {
				e2.printStackTrace();
			} catch (UnsupportedFlavorException e2) {
				e2.printStackTrace();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			
			System.out.println(clipBoardData);

		}
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent e) {
		
		if(e.getKeyCode() == NativeKeyEvent.VC_CONTROL_L){
			
			ctrlHeld = false;
		}
		
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent e) {
		
	}
}


/**

	os.name will return

		Linux
		Windows *Version Num*
		Mac OS X
**/