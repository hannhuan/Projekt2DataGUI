package project2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class LogAccess {
	public LogAccess() {
	}

	public void log(String logAction) {
		File originFile = Logs.instance().getFile();		
		Scanner scan = null;
		StringBuilder sb = null;
		try {
			scan = new Scanner(originFile);
			while (scan.hasNextLine()) {
				sb = new StringBuilder();
				sb.append(scan.nextLine()).append("\n");
			}
			Logs.instance().log(logAction);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if(sb==null){
			sb = new StringBuilder();
		}
		scan.close();
		sb.append(logAction);
		File file = new File("log3.txt");	
		try {
			file.createNewFile();
			FileWriter writer = new FileWriter(file);
			writer.write(sb.toString());
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
