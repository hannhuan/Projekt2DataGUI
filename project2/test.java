package project2;

import gui.BasicPanes;
import gui.CreatePane;
import gui.LoginPane;

public class test {

	public static void main(String[] args) {
		/**String UId = "9302020707";
		Server s1 = new Server();
		System.out.println(s1.getUser(UId).getUId());
		//testa server, db, , get user, getrecords*/
		Server server = new Server();
		LoginPane bp = new LoginPane(server);
	}

}
