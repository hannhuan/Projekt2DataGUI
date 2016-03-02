package KrustyKookies;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import KrustyKookies.DataBase;

public class DataBase extends Observable {
	private Connection conn;
	private ArrayList<String> ingredients;

	public DataBase() {
		conn = null;
		ingredients = new ArrayList<String>();
	}

	public boolean openConnection(String userName, String password) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(
					"jdbc:mysql://puccini.cs.lth.se/" + userName, userName,
					password);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Close the connection to the database.
	 */
	public void closeConnection() {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
		}
		conn = null;
	}

	/**
	 * Check if the connection to the database has been established
	 * 
	 * @return true if the connection has been established
	 */
	public boolean isConnected() {
		return conn != null;
	}

	/** returnerar username till användare eller null */

	public void checkUser(char[] input) {
		PreparedStatement ps = null;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < input.length; i++) {
			sb.append(Character.toString(input[i]));
		}
		boolean password = false;
		try {
			ps = conn
					.prepareStatement("select psword from krustykookies where psword=?");
			ps.setString(1, sb.toString());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				CurrentUser.instance().loginAs(sb.toString());
				password = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (NullPointerException n) {
				n.printStackTrace();

			}
		}
		setChanged();
		notifyObservers(password);
	}

	public void addCustomer(String name, String address) {
		PreparedStatement ps = null;
		String success = "success";
		try {
			// bara en får lägga till customer om gången
			ps = conn.prepareStatement("start transaction");
			ps.execute();
			// checkar så att customer rinte redan finns
			ps = conn
					.prepareStatement("select * from Customers lock in share mode");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				if (name.equals(rs.getString("customerName"))) {
					ps = conn.prepareStatement("rollback");
					ps.execute();
					ps.close();
					success = "nope";
					setChanged();
					notifyObservers(success);
					return;
				}
			}

			// lägger in customer i db
			ps = conn.prepareStatement("insert into customers values (?, ?)");
			ps.setString(1, name);
			ps.setString(2, address);
			ps.execute();

			// unlock lock and stop transaction
			ps = conn.prepareStatement("commit");
			ps.execute();
		} catch (SQLException e) {
			success = "nope";
			e.printStackTrace();
		} finally {

			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (NullPointerException n) {
				n.printStackTrace();

			}
		}
		setChanged();
		notifyObservers(success);

	}

	public void addRecipe(String cookie, List<String> ingredients) {
		PreparedStatement ps = null;
		String info = null;
		String sql = "select cookieName from recipes where cookieName=?";
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, cookie);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				info = "ExistsAlready";
				setChanged();
				notifyObservers(info);
				return;
			}
			sql = "select * from recipes lock in share mode";
			ps = conn.prepareStatement(sql);
			ps.execute();
			// lägger in recipe i db
			for(String s : ingredients){
				String[] ing = s.split(" ");
				StringBuilder ingredient = new StringBuilder();
				for(int i=0; i <ing.length-2; i++){
					ingredient.append(ing[i]);
				}
				ps = conn.prepareStatement("insert into recipes values (?, ?, ?, ?)");
				ps.setString(1, cookie);
				ps.setString(2, ingredient.toString());
				ps.setString(3, ing[ing.length-2]);
				ps.setString(4, ing[ing.length-1]);
				ps.execute();
				
			}

			// unlock lock and stop transaction
			ps = conn.prepareStatement("commit");
			ps.execute();
			info = "recipeAdded";

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (NullPointerException n) {
				n.printStackTrace();

			}
		}
		setChanged();
		notifyObservers(info);

	}

	public String getRecipe(String name) {
		PreparedStatement ps = null;
		String text = null;
		try {
			ps = conn
					.prepareStatement("select ingredients from recipes where cookieName=?");
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				text = rs.getString("ingredients");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (NullPointerException n) {
				n.printStackTrace();

			}
		}
		return text;
	}

	public void removeCustomer(String customerN) {
		PreparedStatement ps = null;
		String info = null;
		String sql = "select customerName from customers where customerName=?";
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, customerN);
			ResultSet rs = ps.executeQuery();
			if (!rs.next()) {
				info = "DoesNotExist";
				setChanged();
				notifyObservers(info);
				return;
			}
			sql = "select * from customers lock in share mode";
			ps = conn.prepareStatement(sql);
			ps.execute();

			// lägger in customer i db
			ps = conn
					.prepareStatement("delete from customers where customerName=?");
			ps.setString(1, customerN);
			ps.execute();

			// unlock lock and stop transaction
			ps = conn.prepareStatement("commit");
			ps.execute();
			info = "costumersRemoved";

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (NullPointerException n) {
				n.printStackTrace();

			}
		}
		setChanged();
		notifyObservers(info);

	}

	public void removeRecipe(String cookieN) {
		PreparedStatement ps = null;
		String info = null;
		String sql = "select cookieName from recipes where cookieName=?";
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, cookieN);
			ResultSet rs = ps.executeQuery();
			if (!rs.next()) {
				info = "DoesNotExist";
				setChanged();
				notifyObservers(info);
				return;
			}
			sql = "select * from recipes lock in share mode";
			ps = conn.prepareStatement(sql);
			ps.execute();

			// lägger in customer i db
			ps = conn
					.prepareStatement("delete from recipes where cookieName=?");
			ps.setString(1, cookieN);
			ps.execute();

			// unlock lock and stop transaction
			ps = conn.prepareStatement("commit");
			ps.execute();
			info = "recipeRemoved";

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (NullPointerException n) {
				n.printStackTrace();

			}
		}
		setChanged();
		notifyObservers(info);
	}

	/** adds raw material to our raw material storage */

	public void addRawMaterial(String ingredient, String amount) {
		PreparedStatement ps = null;
		String info = null;
		String sql = "start transaction";
		try {
			ps = conn.prepareStatement(sql);
			ps.execute();

			sql = "select * from rawmaterials lock in share mode";
			ps = conn.prepareStatement(sql);
			// lägger in ingredient och amount i db
			ps.execute();
			if (!compareCaseInsensitive(ingredient)) {
				sql = "insert into rawmaterials (ingredients, totalAmount) values (?, ?)";
				ps = conn.prepareStatement(sql);
				ps.setString(1, ingredient);
				ps.setString(2, amount);
				ps.execute();
				addIngredients();
			} else {
				sql = "update rawmaterials set totalAmount=totalAmount+? where ingredients=?";
				ps = conn.prepareStatement(sql);
				ps.setString(1, amount);
				ps.setString(2, ingredient);
				ps.execute();
			}

			// unlock lock and stop transaction
			ps = conn.prepareStatement("commit");
			ps.execute();
			info = "IngredientAdded";
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (NullPointerException n) {
				n.printStackTrace();

			}
		}
		setChanged();
		notifyObservers(info);
	}

	public void addIngredients() {

		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("select ingredients from recipes");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String line = rs.getString("ingredients");
				String[] ing = line.split(";");
				for (int i = 0; i < ing.length; i++) {
					String s = ing[i];
					if(ing[i].startsWith(" ")){
						s = ing[i].substring(1);
					} 
					String [] t = s.split(" ");
					StringBuilder build = new StringBuilder();
					for(int j = 0; j < t.length-2; j++){
						build.append(t[j] + " ");
					}
						
					if (!compareCaseInsensitive(build.toString())) {
						ingredients.add(build.toString());
					}
					}
					
				}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (NullPointerException n) {
				n.printStackTrace();

			}
		}
	}
	public boolean compareCaseInsensitive(String s){
		for(String t : ingredients){
			int i = t.compareToIgnoreCase(s);
			if(i==1){
				return true;
			}
		}
		return false;
	}

	public void addOrder(String customer, List<String> orders, String date) {
		PreparedStatement ps = null;
		String info = "failed";
		String sql = "start transaction";
		System.out.println(customer + " " + date);
		try {
			ps = conn.prepareStatement(sql);
			ps.execute();
			
			sql = "select * from orders lock in share mode";
			ps = conn.prepareStatement(sql);
			ps.execute();

			// lägger in orders i db
			sql = "insert into orders (customerName, deliveryDate) values (?, ?)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, customer);
			ps.setString(2, date);
			ps.execute();
			System.out.println("inserted order");
			for(String s : orders){
				String[] ing = s.split(" ");
				StringBuilder cookie = new StringBuilder();
				for(int i=0; i <ing.length-1; i++){
					cookie.append(ing[i]);
				}
				sql =" insert into orderQuantity values ( ?, ?, ?, ?)";
				ps = conn.prepareStatement(sql);
				ps.setString(1, customer);
				ps.setString(2, date);
				ps.setString(3, cookie.toString());
				ps.setInt(4, Integer.valueOf(ing[ing.length-1]));
				ps.execute();
				
			}
			System.out.println("inserted orderQuantity");
			// unlock lock and stop transaction
			ps = conn.prepareStatement("commit");
			ps.execute();
			info = "succeded";

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (NullPointerException n) {
				n.printStackTrace();

			}
		}
		setChanged();
		notifyObservers(info);

		
	}
}
