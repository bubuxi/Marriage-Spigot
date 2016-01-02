package net.bubuxi.mc.marriage.database;

import net.bubuxi.mc.marriage.Marriage;
import net.bubuxi.mc.marriage.Logger;
import net.bubuxi.mc.marriage.enums.Gender;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class SQLUtil {
	/*
	 * MARRIAGES  MarriageId  Player1  Player2  Status
	 * Status: 0-default 1-broken 2-invalid(to be deleted)
	 * 
	 * PROPOSALS  ProposalId  Player1  Player2  Status
	 * Status: 0-default 1-declined by latter 2-invalid(to be deleted)
	 * 
	 * Gender Player Gender
	 * Gender: 0-male, 1-female
	 */
	private static String createMarriageTableSql = "CREATE TABLE MARRIAGES(MarriageId int(11) NOT NULL "
			+ "AUTO_INCREMENT, Player1 varchar(20) NOT NULL, Player2 varchar(20) NOT NULL, Status tinyint(2) "
			+ "NOT NULL DEFAULT 0, Breaker varchar(20), PRIMARY KEY (MarriageId))";

	private static String insertMarriageSql = "INSERT INTO MARRIAGES(Player1, Player2) VALUES(?, ?)";
	private static String getMarriagesSql = "SELECT Player1, Player2 FROM MARRIAGES WHERE Status=0";
	private static String getMessagesSql = "SELECT Player1, Player2, Breaker FROM MARRIAGES WHERE Status=1";
	private static String breakMarriageSql = "UPDATE MARRIAGES SET Status=1,Breaker=? WHERE Status=0 AND "
			+ "(Player1=? OR Player2=?)";
	//? stands for the one being broken 
	private static String toRemoveMarriageSql = "UPDATE MARRIAGES SET Status=2 WHERE Status=1 AND "
			+ "(Player1=? OR Player2=?)";
	private static String removeInvalidMarriageSql = "DELETE FROM MARRIAGES WHERE Status=2";
	
	private static String createProposalTableSql = "CREATE TABLE PROPOSALS(ProposalId int(11) NOT NULL "
			+ "AUTO_INCREMENT, Player1 varchar(20) NOT NULL, Player2 varchar(20) NOT NULL, Status tinyint(2) "
			+ "NOT NULL DEFAULT 0, PRIMARY KEY (ProposalId))";
	private static String insertProposalSql = "INSERT INTO PROPOSALS (Player1, Player2) VALUES (?, ?)";
	private static String getProposalsSql = "SELECT Player1, Player2 FROM PROPOSALS WHERE Status=0";
	private static String getInformSql = "SELECT Player1, Player2 FROM PROPOSALS WHERE Status=1";
	private static String declineProposalSql = "UPDATE PROPOSALS SET Status=1 WHERE Status=0 AND "
			+ "Player1=? AND Player2=?";
	private static String toRemoveProposalSql = "UPDATE PROPOSALS SET Status=2 WHERE (Status=0 OR Status=1) AND "
			+ "Player1=? AND Player2=?";
	private static String removeInvalidProposalSql = "DELETE FROM PROPOSALS WHERE Status=2";
	
	private static String createGenderTableSql = "CREATE TABLE GENDER(Player varchar(20) NOT NULL, Gender "
			+ "tinyint(1) NOT NULL)";
	private static String insertGenderSql = "INSERT INTO GENDER (Player, Gender) VALUES (?, ?)";
	private static String getGenderSql = "SELECT Player, Gender FROM GENDER WHERE Gender=0 OR Gender=1";

	/*
	 * REMOVE methods
	 */
	public static void removeInvalidProposal() {
		PreparedStatement ps = null;
		try {
			ps = Marriage.conn.prepareStatement(removeInvalidProposalSql);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void removeInvalidMarriage() {
		PreparedStatement ps = null;
		try {
			ps = Marriage.conn.prepareStatement(removeInvalidMarriageSql);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * UPDATE methods
	 */	
	public static void toRemoveProposal(String name1, String name2) {
		PreparedStatement ps = null;
		try {
			ps = Marriage.conn.prepareStatement(toRemoveProposalSql);
			ps.setString(1, name1);
			ps.setString(2, name2);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void declineProposal(String name1, String name2) {
		PreparedStatement ps = null;
		try {
			ps = Marriage.conn.prepareStatement(declineProposalSql);
			ps.setString(1, name1);
			ps.setString(2, name2);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void toRemoveMarriage(String name) {
		PreparedStatement ps = null;
		try {
			ps = Marriage.conn.prepareStatement(toRemoveMarriageSql);
			ps.setString(1, name);
			ps.setString(2, name);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void breakMarriage(String name) {
		PreparedStatement ps = null;
		try {
			ps = Marriage.conn.prepareStatement(breakMarriageSql);
			ps.setString(1, name);
			ps.setString(2, name);
			ps.setString(3, name);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * GET methods
	 */
	public static HashMap<String, Gender> getGender() {
		Statement st = null;
		try {
			st=Marriage.conn.createStatement();
			ResultSet rs = st.executeQuery(getGenderSql);
			HashMap<String, Gender> map = new HashMap<String, Gender>();
			while (rs.next()) {
				switch (rs.getInt(2)) {
				case 0:
					map.put(rs.getString(1), Gender.MALE);
					break;
				case 1:
					map.put(rs.getString(1), Gender.FEMALE);
					break;
				default:
					break;
				}
			}
			return map;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static HashMap<String, String> getInform() {
		Statement st = null;
		try {
			st=Marriage.conn.createStatement();
			ResultSet rs = st.executeQuery(getInformSql);
			HashMap<String, String> map = new HashMap<String, String>();
			while (rs.next()) {
				map.put(rs.getString(1), rs.getString(2));
			}
			return map;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static HashMap<String, String> getProposals() {
		Statement st = null;
		try {
			st=Marriage.conn.createStatement();
			ResultSet rs = st.executeQuery(getProposalsSql);
			HashMap<String, String> map = new HashMap<String, String>();
			while (rs.next()) {
				map.put(rs.getString(1), rs.getString(2));
			}
			return map;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static HashMap<String, String> getMessages() {
		Statement st = null;
		try {
			st=Marriage.conn.createStatement();
			ResultSet rs = st.executeQuery(getMessagesSql);
			HashMap<String, String> map = new HashMap<String, String>();
			while (rs.next()) {
				String s1 = rs.getString(1),s2=rs.getString(2),s3=rs.getString(3);
				if(s1.equals(s3)){
					map.put(s2, s1);
				}
				else{
					map.put(s1, s2);
				}
			}
			return map;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static HashMap<String, String> getMarriages() {
		Statement st = null;
		try {
			st=Marriage.conn.createStatement();
			ResultSet rs = st.executeQuery(getMarriagesSql);
			HashMap<String, String> map = new HashMap<String, String>();
			while (rs.next()) {
				String s1 = rs.getString(1),s2=rs.getString(2);
				map.put(s1, s2);
				map.put(s2, s1);
			}
			return map;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/*
	 * INSERT methods
	 */
	public static void insertGender(String name, Gender gender) {
		switch (gender) {
		case MALE:
			insertWithStringAndInt(insertGenderSql, name, 0);
			break;
		case FEMALE:
			insertWithStringAndInt(insertGenderSql, name, 1);
			break;
		default:
			break;
		}
	}
	
	public static void insertProposal(String name1, String name2) {
		insertWithTwoStrings(insertProposalSql, name1, name2);
	}
	
	public static void insertMarriage(String name1, String name2) {
		insertWithTwoStrings(insertMarriageSql, name1, name2);
	}
	
	/*
	 * CREATE TABLE methods
	 */
	
	public static void createMarriageTable() throws SQLException {
		createTable(createMarriageTableSql);
	}
	
	public static void createProposalTable() throws SQLException {
		createTable(createProposalTableSql);
	}
	
	public static void createGenderTable() throws SQLException {
		createTable(createGenderTableSql);
	}
	
	/*
	 * PRIVATE methods
	 */
	private static void insertWithStringAndInt(String sql, String str, int i) {
		PreparedStatement ps = null;
		try {
			ps = Marriage.conn.prepareStatement(sql);
			ps.setString(1, str);
			ps.setInt(2, i);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void insertWithTwoStrings(String sql, String str1, String str2) {
		PreparedStatement ps = null;
		try {
			ps = Marriage.conn.prepareStatement(sql);
			ps.setString(1, str1);
			ps.setString(2, str2);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void createTable(String createTableSql) 
		throws SQLException {
			Statement st=null;
			try{
				Logger.debug("creating table: "+createTableSql);
				st=Marriage.conn.createStatement();
				if(st.execute(createTableSql)){
					Logger.debug("table created");
				}
				else{
					Logger.debug("Failed to creat table");
				}
			}catch(SQLException e){
				throw e;
			}finally{
				try {
					st.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
	}
}
