package project2;

import java.util.ArrayList;

public class Record {
	private String patientUId;
	private String doctorUId;
	private String nurseUId;
	private String division;
	private String title;
	private StringBuilder data = new StringBuilder();
	private int recordNbr;
	private String datetime;
	
	public Record( String patientUId, String doctorUId, String nurseUId, String division, String title, String input, int recordNbr,String datetime){
		this.patientUId=patientUId;
		this.doctorUId=doctorUId;
		this.nurseUId=nurseUId;
		this.division=division;
		this.title=title;
		this.data.append(input).append("\n");
		this.recordNbr=recordNbr;
		this.datetime=datetime;
	}
	
	public void writeTo(int recordNbr, String data){
		this.data.append(data).append("\n");
		DataBase.instance().writeTo( this.recordNbr ,this.data.toString());
	}

	public String getTitle() {
		return title;
	}

	public int getNbr() {
		return recordNbr;
	}

	public String getPatient() {
		return patientUId;
	}

	public String getDoctor() {
		return doctorUId;
	}

	public String getNurse() {
		
		return nurseUId;
	}

	public String getDivision() {
		return division;
	}

	public String getData() {
		return this.data.toString();
	}
	public String toString(){
		return patientUId + " "  + doctorUId + " " + nurseUId + " " + division + " " ;
		
	}
}
