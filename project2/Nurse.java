package project2;

public class Nurse extends DocOrNurse {
	private int GId = 2;

	public Nurse(String UId, String name, String division) {
		super(UId, name, division);
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
	public int getGId() {
		// TODO Auto-generated method stub
		return GId;
	}
	

}
