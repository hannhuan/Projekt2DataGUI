package project2;

public abstract class DocOrNurse implements User {
	protected String UId;
	protected String name;
	protected String division;
	private Certificate certificate;
	
	public DocOrNurse(String UId, String name, String division){
		this.UId=UId;
		this.name=name;
		this.division=division;
	}
	public String getName(){
		return name;
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
		return division;
	}

	@Override
	public abstract int getGId();

	@Override
	public String getUId() {
		// TODO Auto-generated method stub
		return null;
	}

}
