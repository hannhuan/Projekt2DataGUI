package project2;

public class Patient implements User{
	private String UId;
	private int GId = 1;
	private String name;
	private Certificate certificate;
	
	public Patient(String UId, String name){
		this.UId=UId;
		this.name=name;
	}

	@Override
	public boolean getReadAccess() {
		return true;
	}

	@Override
	public boolean getCreateAccess() {
		return false;
	}

	@Override
	public boolean getDeleteAcces() {
		return false;
	}

	@Override
	public String getDivision() {
		return null;
	}

	@Override
	public int getGId() {
		return GId;
	}

	@Override
	public String getUId() {
		return UId;
	}

	@Override
	public String getName() {
		return name;
	}

}
