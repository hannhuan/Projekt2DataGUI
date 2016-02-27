package project2;

public class Doctor extends Users {
	private int GId = 3;

	public Doctor(String UId, String name, String division) {
		super(UId, name, division);
	}
	
	
	@Override
	public int getGId() {
		// TODO Auto-generated method stub
		return GId;
	}

}
