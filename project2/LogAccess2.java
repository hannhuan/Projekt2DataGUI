package project2;

public class LogAccess2 {
	public LogAccess2(){
		
	}
	public void log(String action){
		String t = Logs.instance().log(action);
		System.out.println(t);
	}
}
