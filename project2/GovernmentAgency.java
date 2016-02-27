package project2;

public class GovernmentAgency extends Users{
	private String UId;
	private int GId = 4;
	private String name;
	private Certificate certificate;
	
	public GovernmentAgency(String UId, String name, String division){
		super(UId, name, division);
	}

	

	@Override
	public int getGId() {
		// TODO Auto-generated method stub
		return GId;
	}

}
