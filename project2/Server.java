package project2;

import java.util.ArrayList;

public class Server {
	private DataBase db;
	private User user;
	private String currentKey;
	private Record record;
	public Server(){
		this.currentKey=null;
		this.user=null;
		this.record=null;
		this.db= DataBase.instance();
		
		if (db.openConnection("db133", "qoq059ex")) {
			System.out.println("Connected to database");
		} else {
			System.out.println("Could not connect to database");
		}
	}
	
	public void close(){
		db.closeConnection();
	}
	public boolean isConnected() {
		return db.isConnected();
	}
	
	public ArrayList<Record> findUsersRecords(String UId, int GId, String division){
		
		return db.getUsersRecords( UId, GId, division);
		
	}
	/** check if UId exists in database
	 * @param passWord */
	public boolean checkUser(String UId, String passWord){
		currentKey = db.checkUser(UId, passWord);
		if(currentKey!=null){
			user = db.getUser(UId);
			return true;
		}
		return false;
	}
	
	public User getUser(String UId) {
		return db.getUser(UId);
		
	}
	public ArrayList<Record> getRecords(User user){
		String UId = user.getUId();
		int GId = user.getGId();
		String division = user.getDivision();
		ArrayList<Record> records=db.getUsersRecords(UId, GId, division);
		return records;
		
	}

	public String getName(String uId) {
		return user.getName();
	}

	public int getGId() {
		return user.getGId();
	}

	public String getDivision() {
		return user.getDivision();
	}

	public void setRecord(Object selectedValue) {
		if(selectedValue==null){
			System.out.println("nope");
		} else {
		String rec = (String) selectedValue;
		String [] r = rec.split(": ");
		this.record = db.getRecord(r[0]);
		}
	}
	public String getRecordData() {
		StringBuilder sb = new StringBuilder();
		sb.append(record.getPatient()).append("\n");
		sb.append(record.getDoctor()).append("\n");
		sb.append(record.getNurse()).append("\n");
		sb.append(record.getDivision()).append("\n");
		sb.append(record.getTitle()).append("\n");
		sb.append(record.getData());
		return sb.toString();
	}

	public boolean createRecord(String information) {
		String[]info = information.split(","); 
		if(db.getUser(info[0])!=null && db.getUser(info[1])!=null && db.getUser(info[2])!=null){
				return db.createRecord(info[0], info[1], info[2],info[3],info[4], info[5]);
		}
		return false;
	}
	
	public boolean editRecord (String information){
		String[]info = information.split(","); 
		if(db.getUser(info[0])!=null && db.getUser(info[1])!=null && db.getUser(info[2])!=null){
				return writeTo (info[5]);
		}
		return false;
	}

	public boolean recordCheck() {
		if(record==null){
			return false;	
		}
		return true;
	}

	public boolean delete() {
		int nbr = record.getNbr();
		return db.delete(nbr);
		
	}

	public boolean writeTo(String data) {
		int nbr = record.getNbr();
		return db.writeTo(nbr, data);	
	}

}
