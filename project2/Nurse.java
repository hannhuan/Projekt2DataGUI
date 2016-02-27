package project2;

public class Nurse extends Users {
	private int GId = 2;

	public Nurse(String UId, String name, String division) {
		super(UId, name, division);
	}

	@Override
	public int getGId() {
		// TODO Auto-generated method stub
		return GId;
	}
	

}
