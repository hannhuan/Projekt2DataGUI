package project2;

public abstract class Users {
	protected String UId;
	protected String name;
	protected String division;
	private Certificate certificate;
	
	public Users(String UId, String name, String division){
		this.UId=UId;
		this.name=name;
		this.division=division;
	}
	
	public String getDivision(){
		return division;
		
	}
	public abstract int getGId();
	
	public String getUId(){
		return UId;
		
	}
	public String getName(){
		return name;	
	}
}
