package project2;

public class GovernmentAgency implements User{
	private String UId;
	private int GId = 4;
	private String name;
	private Certificate certificate;
	
	public GovernmentAgency(String UId, String name){
		this.UId=UId;
		this.name=name;
	}

	@Override
	public boolean getReadAccess() {
		return false;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getGId() {
		// TODO Auto-generated method stub
		return GId;
	}

	@Override
	public String getUId() {
		// TODO Auto-generated method stub
		return UId;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

}
