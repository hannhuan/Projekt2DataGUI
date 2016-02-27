package project2;

public class Patient extends Users{
	private int GId = 1;
	private Certificate certificate;
	
	public Patient(String UId, String name, String division){
		super(UId, name, division);
	}

	public int getGId() {
		return GId;
	}
}


