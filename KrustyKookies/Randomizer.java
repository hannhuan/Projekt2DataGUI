package KrustyKookies;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Randomizer {
	private static Random rand = new Random();
	public Randomizer(){}
	
	public int ID(DataBase db){
		List<Integer> orderIds = db.getOrderIds();
		StringBuilder build = new StringBuilder();
		int orderId = 0;
		boolean id = false;
 		while(id==false){
 			orderId = rand.nextInt(1000000000);
 			orderId+=1000000000;
 			if(!orderIds.contains(orderId)){
 				id = true;
 				orderIds.add(orderId);	
 			}
		}
		System.out.println(orderId);  
		return orderId;
	}

}
