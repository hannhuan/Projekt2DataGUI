package KrustyKookies;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Random;
import java.util.Set;

import javax.swing.JOptionPane;

import KrustyKookies.DataBase;

public class DataBase extends Observable {
	private Connection conn;
	private Randomizer randomizer;

	public DataBase() {
		conn = null;
		randomizer = new Randomizer();
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

	/** returnerar username till användare eller */

	public void checkUser(char[] input) {
		PreparedStatement ps = null;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < input.length; i++) {
			sb.append(Character.toString(input[i]));
		}
		boolean password = false;
		CurrentUser.instance().loginAs(sb.toString());

		try {
			ps = conn
					.prepareStatement("select * from krustykookies where psword=?");
			ps.setString(1, sb.toString());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
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
		String sql = "select cookieName from cookies where cookieName=?";
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
			ps = conn.prepareStatement("start transaction");
			ps.execute();

			sql = "select * from cookies, recipes lock in share mode";
			ps = conn.prepareStatement(sql);
			ps.execute();

			sql = "insert into cookies values (?)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, cookie);
			ps.execute();

			// lägger in recipe i db
			StringBuilder ingredient = new StringBuilder();
			for (String s : ingredients) {
				String[] ing = s.split(" ");
				for (int i = 0; i < ing.length - 2; i++) {
					ingredient.append(ing[i]);
				}
				sql = "select count(*) from rawmaterials where ingredient=?";
				ps = conn.prepareStatement(sql);
				ps.setString(1, ingredient.toString());
				rs = ps.executeQuery();
				boolean exist = false;
				while (rs.next()) {
					int count = rs.getInt("count(*)");
					if (count != 0) {
						exist = true;

					}
				}
				if (exist == false) {
					// add into rawmaterials
					addRawMaterial(ingredient.toString(), "0",
							ing[ing.length - 1]);
				}
				ps = conn
						.prepareStatement("insert into recipes values (?, ?, ?, ?)");
				ps.setString(1, cookie);
				ps.setString(2, ingredient.toString());
				ps.setString(3, ing[ing.length - 2]);
				ps.setString(4, ing[ing.length - 1]);
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
					.prepareStatement("select ingredient from recipes where cookieName=?");
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			List<String> ingredients = new ArrayList<String>();
			while (rs.next()) {
				ingredients.add(rs.getString("ingredient"));
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
			System.out.println("hej");
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

	/**
	 * adds raw material to our raw material storage
	 * 
	 * @param unit
	 */

	public void addRawMaterial(String ingredient, String amount, String unit) {
		PreparedStatement ps = null;
		String info = null;
		String sql = "start transaction";
		if (CheckInput.checkNbr(amount).equals("valid")) {

			try {

				ps = conn.prepareStatement(sql);
				ps.execute();

				sql = "select * from rawmaterials lock in share mode";
				ps = conn.prepareStatement(sql);
				// lägger in ingredient och amount i db
				ps.execute();
				if (!compareCaseInsensitive(ingredient)) {
					sql = "insert into rawmaterials (ingredient, totalAmount, unit) values (?, ?, ?)";
					ps = conn.prepareStatement(sql);
					ps.setString(1, ingredient);
					ps.setInt(2, Integer.parseInt(amount));
					ps.setString(3, unit);
					ps.execute();
				} else {
					sql = "update rawmaterials set totalAmount=totalAmount+? where ingredient=?";
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

	}

	public boolean compareCaseInsensitive(String ingredient) {
		PreparedStatement ps = null;

		String sql = "select ingredient from rawmaterials where ingredient=? ";
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, ingredient);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				return true;
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

		return false;
	}

	/** Customer methods */
	public List<String> getCustomers() {
		List<String> customers = new ArrayList<String>();
		PreparedStatement ps = null;

		String sql = "select * from customers";
		try {
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				customers.add(rs.getString("customerName"));
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

		return customers;
	}

	public List<String> getCookies() {
		List<String> cookies = new ArrayList<String>();
		PreparedStatement ps = null;

		String sql = "select * from cookies";
		try {
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				cookies.add(rs.getString("cookieName"));
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

		return cookies;
	}

	public void addOrder(String customer, List<String> orders, String date) {
		PreparedStatement ps = null;
		int orderId = randomizer.ID(this);
		String info = "failed";
		String sql;
		try {
			int amount = 0;
			int count = 0;
			Map<Integer, Integer> amounts = new HashMap<Integer, Integer>();
			for (String o : orders) {
				String[] ord = o.split(" ");
				StringBuilder c = new StringBuilder();
				for (int j = 0; j < ord.length - 1; j++) {
					c.append(ord[j]).append(" ");
				}
				if (CheckInput.checkNbr(ord[ord.length - 1]).equals("invalid")) {
					setChanged();
					notifyObservers("invalidFormat");
					return;
				}
				amount = Integer.valueOf(ord[ord.length - 1]);
				sql = "select count(*) from pallets natural left outer join blockedpallets where blockedDate is NULL and cookieName=? and status=1";
				ps = conn.prepareStatement(sql);
				ps.setString(1, c.toString());
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					count = rs.getInt("count(*)");
				}
				amounts.put(amount, count);
			}
			boolean sufficient = true;
			Set<Entry<Integer, Integer>> keys = amounts.entrySet();
			Iterator it = amounts.entrySet().iterator();
			while (it.hasNext()) {
				Entry pair = (Entry) it.next();
				amount = (int) pair.getKey();
				count = (int) pair.getValue();
				if (count < amount || count == 0 || amount == 0) {
					sufficient = false;
				}
			}
			if (sufficient == true) {

				sql = "start transaction";
				ps = conn.prepareStatement(sql);
				ps.execute();

				sql = "select * from orders, pallets, orderquantity lock in share mode";
				ps = conn.prepareStatement(sql);
				ps.execute();

				// lägger in orders i db
				sql = "insert into orders (orderID, customerName, deliveryDate) values (?, ?, ?)";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, orderId);
				ps.setString(2, customer);
				ps.setString(3, date);
				ps.execute();
				for (String s : orders) {
					String[] ing = s.split(" ");
					StringBuilder cookie = new StringBuilder();
					for (int i = 0; i < ing.length - 1; i++) {
						cookie.append(ing[i]).append(" ");
					}
					int palletId = getPalletID(cookie.toString().trim());
					sql = " insert into orderQuantity values ( ?, ?, ?)";
					ps = conn.prepareStatement(sql);
					ps.setInt(1, orderId);
					ps.setInt(2, palletId);
					ps.setInt(3, Integer.valueOf(ing[ing.length - 1]));
					ps.execute();
					// Ändrar status
					sql = "update pallets set status=3 where palletID=?";
					ps = conn.prepareStatement(sql);
					ps.setInt(1, palletId);
					ps.execute();
				}
				// unlock lock and stop transaction
				ps = conn.prepareStatement("commit");
				ps.execute();
				info = String.valueOf(orderId);
			} else if (amount == 0) {
				JOptionPane.showMessageDialog(null,
						"An invalid order amount inserted");
			} else
				JOptionPane
						.showMessageDialog(null,
								"There are insufficient amounts of pallets for this order");

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

	public List<Integer> getOrderIds() {
		List<Integer> orderIds = new ArrayList<Integer>();
		PreparedStatement ps = null;

		String sql = "select orderID from orders";
		try {
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				orderIds.add(rs.getInt("orderID"));
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

		return orderIds;
	}

	public int getPalletID(String cookie) {
		PreparedStatement ps = null;
		int palletId = 0;
		String sql = "select palletID from pallets where cookieName=? and status!=3 and status!=4";
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, cookie);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				palletId = rs.getInt("palletID");
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
		return palletId;
	}

	public Map<String, List<String>> searchTime(String startTime,
			String endTime, String cookie) {
		Map<String, List<String>> pallets = new HashMap<String, List<String>>();
		if (CheckInput.checkSearchDate(startTime) == false
				|| CheckInput.checkSearchDate(endTime) == false) {
			JOptionPane.showMessageDialog(null,
					"Check length and format of the inserted time and date");
		} else {
			String start = startTime + ":00";
			String end = endTime + ":00";
			PreparedStatement ps = null;
			try {

				if (start.substring(0, 10).compareTo(end.substring(0, 10)) < 0) {

					String sql = " select * from pallets natural left outer join blockedpallets where prodTime>=? and prodTime<=? and cookieName=?;";
					ps = conn.prepareStatement(sql);
					ps.setString(1, start);
					ps.setString(2, end);
					ps.setString(3, cookie);
					ResultSet rs = ps.executeQuery();
					String id = null;
					while (rs.next()) {
						List<String> pallet = new ArrayList<String>();
						id = String.valueOf(rs.getInt("palletID"));

						pallet.add(id);
						pallet.add(rs.getString("cookieName"));
						pallet.add(rs.getString("prodTime"));
						switch (rs.getInt("status")) {
						case 1:
							pallet.add("Freezer");
							break;
						case 2:
							pallet.add("Transport");
							break;
						case 3:
							pallet.add("Delivered");
							break;
						default:
							pallet.add("Just Produced");
							break;
						}
						if (rs.getString("blockedDate") != null) {
							pallet.add(rs.getString("blockedDate"));

						} else {
							pallet.add("Not Blocked");
						}
						if (id != null) {
							pallets.put(id, pallet);
						} else
							JOptionPane.showMessageDialog(null,
									"Pallet cannot be found in databse");

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
		return pallets;

	}

	public List<String> searchPallet(String text) {

		boolean pall = CheckInput.checkPalletID(text);
		List<String> palletInfo = new ArrayList<String>();
		if (pall == false) {
			JOptionPane.showMessageDialog(null,
					"Check length and format of the inserted palletID");
		} else {
			PreparedStatement ps = null;
			String sql = "select * from pallets natural left outer join blockedpallets where palletID=?";
			try {
				ps = conn.prepareStatement(sql);
				ps.setString(1, text);
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					palletInfo.add(text);
					palletInfo.add(rs.getString("cookieName"));
					palletInfo.add(rs.getString("prodTime"));
					switch (rs.getInt("status")) {
					case 1:
						palletInfo.add("Freezer");
						break;
					case 2:
						palletInfo.add("Transport");
						break;
					case 3:
						palletInfo.add("Delivered");
						break;
					default:
						palletInfo.add("Just Produced");
						break;
					}
					if (rs.getString("blockedDate") != null) {
						palletInfo.add(rs.getString("blockedDate"));

					} else
						palletInfo.add("Not Blocked");
				} else
					JOptionPane.showMessageDialog(null,
							"Pallet cannot be found in databse");
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
		return palletInfo;

	}

	public String block(String palletId) {
		int palletID = Integer.parseInt(palletId);
		String date = null;

		PreparedStatement ps = null;
		String sql = "start transaction";
		try {
			ps = conn.prepareStatement(sql);
			ps.execute();
			sql = "select * from blockedPallets, pallets lock in share mode";
			ps = conn.prepareStatement(sql);
			ps.execute();

			sql = "insert into blockedpallets values (?, now())";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, palletID);
			ps.execute();

			sql = "select blockedDate from blockedpallets where palletID=?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, palletID);
			ps.execute();
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				date = rs.getString("blockedDate");
			}
			sql = "commit";
			ps = conn.prepareStatement(sql);
			ps.execute();

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
		return date;

	}

	public void unblock(String palletId) {
		int palletID = Integer.parseInt(palletId);

		PreparedStatement ps = null;
		String sql = "start transaction";
		try {
			ps = conn.prepareStatement(sql);
			ps.execute();
			sql = "select * from blockedPallets, pallets lock in share mode";
			ps = conn.prepareStatement(sql);
			ps.execute();

			sql = "delete from blockedpallets where palletID=?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, palletID);
			ps.execute();

			sql = "commit";
			ps = conn.prepareStatement(sql);
			ps.execute();

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

	public void status(String palletId) {
		int palletID = Integer.parseInt(palletId);

		PreparedStatement ps = null;
		String sql = "start transaction";
		try {
			ps = conn.prepareStatement(sql);
			ps.execute();
			sql = "select * from blockedPallets, pallets lock in share mode";
			ps = conn.prepareStatement(sql);
			ps.execute();

			sql = "update pallets set status=status+1 where palletID=?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, palletID);
			ps.execute();

			sql = "commit";
			ps = conn.prepareStatement(sql);
			ps.execute();

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

	public void statusAll(Map<String, List<String>> searchMap) {
		for (List<String> values : searchMap.values()) {
			if (!values.get(3).equals("Delivered")) {
				status(values.get(0));
				if(values.get(3).equals("Transport")){
					setDelivered(values.get(0));
				}
			} 
		}
	}

	public List<String> blockAll(Map<String, List<String>> pallets) {
		List<String> dates = new ArrayList<String>();
		String date = null;
		for (List<String> values : pallets.values()) {
			if (values.get(4).equals("Not Blocked")
					&& values.get(3).equals("Freezer")) {
				date = block(values.get(0));
				dates.add(date);
			}
		}
		return dates;
	}

	public void unBlockAll(Map<String, List<String>> pallets) {
		for (List<String> values : pallets.values()) {
			if (values.get(4) != null) {
				unblock(values.get(0));
			}
		}
	}

	public String getUnit(String cookie) {
		String unit = "no";
		String sql;
		PreparedStatement ps = null;
		try {

			sql = "select unit from rawmaterials where ingredient=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, cookie);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				unit = rs.getString("unit");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			unit = null;
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (NullPointerException n) {
				n.printStackTrace();

			}
		}
		return unit;
	}

	public void createPallets(String cookie, String inputAmount) {
		if ((CheckInput.checkNbr(inputAmount)).equals("valid")) {
			int amount = Integer.parseInt(inputAmount);
			String sql;
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				sql = "drop view if exists measurements";
				ps = conn.prepareStatement(sql);
				ps.execute();

				sql = "create view measurements as select ingredient, totalAmount, recipeAmount*540*? as amount from recipes natural join rawmaterials where cookieName=?";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, amount);
				ps.setString(2, cookie);
				ps.execute();
				int i = 0;
				int k = 0;
				sql = "select count(*) from measurements";
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				while (rs.next()) {
					i = rs.getInt(1);
				}
				sql = "select count(*) from measurements where totalAmount>amount";
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				while (rs.next()) {
					k = rs.getInt(1);
				}
				if (k == i && k != 0 && i != 0) {
					sql = "select * from rawmaterials, pallets lock in share mode";
					ps = conn.prepareStatement(sql);
					ps.execute();

					sql = "insert into pallets (cookieName, palletID) values (?, ?)";
					for (int j = 0; j < amount; j++) {
						int palletId = randomizer.ID(this);
						ps = conn.prepareStatement(sql);
						ps.setString(1, cookie);
						ps.setInt(2, palletId);
						ps.execute();
					}
					sql = "select totalAmount-amount as rest, ingredient from measurements";
					ps = conn.prepareStatement(sql);
					rs = ps.executeQuery();
					while (rs.next()) {
						k = rs.getInt(1);
						String ingredient = rs.getString("ingredient");
						sql = "update rawmaterials set totalAmount=? where ingredient=?";
						ps = conn.prepareStatement(sql);
						ps.setInt(1, k);
						ps.setString(2, ingredient);
						ps.execute();
					}
					sql = "commit";
					ps = conn.prepareStatement(sql);
					ps.execute();
					setChanged();
					notifyObservers("created");

				} else
					JOptionPane.showMessageDialog(null,
							"Not Sufficient Raw Materials");

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
		} else
			JOptionPane.showMessageDialog(null, "Check input for amount");
	}

	public void setDelivered(String palletID) {
		if (CheckInput.checkPalletID(palletID) == false) {
			JOptionPane.showMessageDialog(null, "Check input for amount");
		} else {
			int palletId = Integer.parseInt(palletID);
			PreparedStatement ps = null;
			String sql = "start transaction";
			try {
				ps = conn.prepareStatement(sql);
				ps.execute();
				sql = "select * from pallets lock in share mode";
				ps = conn.prepareStatement(sql);
				ps.execute();

				sql = "update orders set ifDelivered='n' where palletID=?";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, palletId);
				ps.execute();

				sql = "commit";
				ps = conn.prepareStatement(sql);
				ps.execute();

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
	}

}
