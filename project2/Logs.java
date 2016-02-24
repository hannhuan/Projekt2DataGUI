package project2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logs {
	private static Logs instance;
	private static File file;
	private static StringBuilder sb; 
	
	private Logs() {
		file=null;
	}
	public static Logs instance(){
		 if (instance == null){
			 	instance = new Logs();
			 	sb = new StringBuilder(); 
		        file = new File("log1.txt");
		        try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
		 }
				return instance;
		    
	}
	public File getFile(){
		return file;
	}
	public String log(String logAction){
		sb.append(logAction).append("\n");
		FileWriter writer;
		try {
			writer = new FileWriter(file);
			writer.write(sb.toString());
			writer.flush();
			writer.close(); 
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	

}
