package project2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DataBase {
	
private static DataBase instance; 	
private Connection conn;
	
	private DataBase(){
		conn = null;
	}
	
	public static DataBase instance(){
		 if (instance == null){
		        instance = new DataBase();
		 }       
		 return instance;
	}
	
	public boolean openConnection(String userName, String password) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://puccini.cs.lth.se/" + userName,
					userName, password);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 * Close the connection to the database.
	 */
	public void closeConnection() {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
		}
		conn = null;
	}

	/**
	 * Check if the connection to the database has been established
	 * 
	 * @return true if the connection has been established
	 */
	public boolean isConnected() {
		return conn != null;
	}
	
	public Users getUser(String UId){
		Users user = null;
		PreparedStatement ps = null;
		String currentName = null;
		String division = null;
		int gid = 0;
		String sql = "select * from users where UId=?";
			try {
				ps = conn.prepareStatement(sql);
				ps.setString(1, UId);
				ResultSet rs = ps.executeQuery();
				while(rs.next()){
					currentName = rs.getString("name");
					gid = rs.getInt("GId");
					if(gid==1 || gid==4){
						if(gid==1){
							user = new Patient(UId, currentName, null);
						} else 
							user = new GovernmentAgency(UId, currentName, null);
					} else {
						division = rs.getString("division");
						if(gid==2){
							user = new Nurse(UId, currentName, division);
						} else
							user = new Doctor(UId, currentName, division);
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		return user;
	}
	

	// tänk på om det inte finns ngra records på denna user
	public ArrayList <Record> getUsersRecords(String UId, int GId, String division){
		ArrayList<Record> reqRecords=new ArrayList<Record>();
		PreparedStatement ps=null;
		String sql1=null;
		if(GId==1){
			sql1 = "select * from records where patientUId=?";
		} else if (GId==2){
			sql1 = "select * from records where nurseUId=?";
		} else if (GId==3){
			sql1 = "select * from records where doctorUId=?";
		} else {
			System.out.println("bhklö");
			sql1 = "select * from records";
		}	
		try {
			ps = conn
					.prepareStatement(sql1);
			if(GId!=4){
				ps.setString(1, UId);
			}
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String pUId = rs.getString("patientUId");
				String dUId = rs.getString("doctorUId");
				String nUId = rs.getString("nurseUId");
				String div = rs.getString("division");
				String title = rs.getString("title");
				String data = rs.getString("data");
				int recordNbr = rs.getInt("recordNbr");
				String datetime = rs.getString("datetime");
				reqRecords.add(new Record(pUId, dUId, nUId, div, title, data, recordNbr, datetime));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(GId==2 || GId==3){
			if(GId==2){
				sql1 = "select * from records where division=? and nurseUId!=?";
			} else {
				sql1 = "select * from records where division=? and doctorUId!=?";
			}
			try {
				ps = conn
						.prepareStatement(sql1);
				ps.setString(1, division);
				ps.setString(2, UId);
				ResultSet rs2 = ps.executeQuery();
				while (rs2.next()) {
					String pUId = rs2.getString("patientUId");
					String dUId = rs2.getString("doctorUId");
					String nUId = rs2.getString("nurseUId");
					String div = rs2.getString("division");
					String title = rs2.getString("title");
					String data = rs2.getString("data");
					int recordNbr = rs2.getInt("recordNbr");
					String datetime = rs2.getString("datetime");
					reqRecords.add(new Record(pUId, dUId, nUId, div, title, data, recordNbr, datetime));
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			finally {
				
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (NullPointerException n){
					n.printStackTrace();	
				}
			}
		}
		return reqRecords;
	}
	
	public boolean createRecord(String patientUId, String doctorUId, String nurseUId,String division, String title, String data){
		PreparedStatement ps=null;
		String sql = "start transaction";
		try {
			ps = conn
					.prepareStatement(sql);
			ps.execute();
			sql = "select * from records lock in share mode";
			ps = conn
					.prepareStatement(sql);
			ps.execute();
			// Assumed that doctor, nurse and patient already exists in the database
			sql = "insert into records (patientUId, doctorUId, nurseUId, division, title, data) values (?, ?, ?, ?, ?, ?)";
			ps = conn
					.prepareStatement(sql);
			ps.setString(1, patientUId);
			ps.setString(2, doctorUId);
			ps.setString(3, nurseUId);
			ps.setString(4, division);
			ps.setString(5, title);
			ps.setString(6, data);
			ps.execute();
			sql ="commit";
			// unlock lock and stop transaction
			ps = conn.prepareStatement(sql);
			ps.execute();
			

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		finally {
			
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (NullPointerException n){
				n.printStackTrace();
				
			}
		}
		return true;
	}	
	
	public boolean writeTo(int recordNbr, String data){
		PreparedStatement ps=null;
		String sql = "start transaction";
		try {
			ps = conn
					.prepareStatement(sql);
			ps.execute();
			sql = "select * from records lock in share mode";
			ps = conn
					.prepareStatement(sql);
			ps.execute();
			sql = "update records set data=? where recordNbr=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, data);
			ps.setInt(2, recordNbr);
			ps.execute();
			// unlock lock and stop transaction
			sql ="commit";
			ps = conn.prepareStatement(sql);
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
		
	}
	public boolean delete(int recordNbr){
		PreparedStatement ps=null;
		String sql = "start transaction";
		try {
			ps = conn
					.prepareStatement(sql);
			ps.execute();
			sql = "select * from records lock in share mode";
			ps = conn
					.prepareStatement(sql);
			ps.execute();
			sql = "delete from records where recordNbr=?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, recordNbr);
			ps.execute();
			// unlock lock and stop transaction
			sql ="commit";
			ps = conn.prepareStatement(sql);
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public String checkUser(String uId, String passWord) {
		PreparedStatement ps=null;
		String key = null;
		String sql = "select * from usercheck where UId=? and userKey=?";
		try {
			ps = conn
					.prepareStatement(sql);
			ps.setString(1, uId);
			ps.setString(2, passWord);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				key = rs.getString("userKey");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return key;

		
	}

	public Record getRecord(String recordNbr) {
		Record rec = null;
		PreparedStatement ps=null;
		String sql = "select * from records where recordNbr=?";
		try {
			ps = conn
					.prepareStatement(sql);
			ps.setString(1, recordNbr);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				String pUId = rs.getString("patientUId");
				String dUId = rs.getString("doctorUId");
				String nUId = rs.getString("nurseUId");
				String div = rs.getString("division");
				String title = rs.getString("title");
				String data = rs.getString("data");
				String datetime = rs.getString("datetime");
				rec = new Record(pUId, dUId, nUId, div, title, data, Integer.parseInt(recordNbr),datetime);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return rec;
	}
	
	

}
