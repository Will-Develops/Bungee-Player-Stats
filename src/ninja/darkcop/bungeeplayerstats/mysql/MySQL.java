package ninja.darkcop.bungeeplayerstats.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import ninja.darkcop.bungeeplayerstats.Main;

public class MySQL {
	public static Connection connection;

	public synchronized void openConnection() {
		try {
			connection = DriverManager.getConnection("jdbc:mysql://"
					+ Main.host + ":" + Main.port + "/" + Main.database
					+ "?autoReconnect=true", Main.username, Main.password);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static synchronized void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public synchronized void createTable() {
		try {
			PreparedStatement sql = connection
					.prepareStatement("CREATE TABLE IF NOT EXISTS "
							+ Main.table
							+ " (ID int NOT NULL AUTO_INCREMENT, Player VARCHAR(16), UUID VARCHAR(36), OnlineTime int(11), LastServer VARCHAR(16), CurrentServer VARCHAR(36), FirstJoined VARCHAR(36), LastJoined VARCHAR(36), Online VARCHAR(36), LastJoinedTime VARCHAR(36), ChatCount int(11), CommandCount int(11), PRIMARY KEY (ID));");
			sql.executeUpdate();
			sql.close();
			Main.getInstance().getLogger().info("BungeeStats table exists!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void tableCheck() {
		try {
			PreparedStatement sql = connection
					.prepareStatement("SELECT 1 FROM " + Main.table
							+ " LIMIT 1;");
			ResultSet set = sql.executeQuery();
			set.next();

			sql.close();
			set.close();

			Main.getInstance().getLogger().info("BungeeStats table exists!");
		} catch (Exception e) {
			Main.getInstance().getLogger()
					.info("BungeeStats table doesn't exist, creating one.");
			createTable();
		}
	}
	
	public synchronized void createTableUsers() {
		try {
			PreparedStatement sql = connection
					.prepareStatement("CREATE TABLE IF NOT EXISTS "
							+ Main.tableUsers
							+ " (ID int NOT NULL AUTO_INCREMENT, Player VARCHAR(16), UUID VARCHAR(36), PGroup VARCHAR(255), PRIMARY KEY (ID));");
			sql.executeUpdate();
			sql.close();
			Main.getInstance().getLogger().info("BungeeStats Users table exists!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void tableCheckUsers() {
		try {
			PreparedStatement sql = connection
					.prepareStatement("SELECT 1 FROM " + Main.tableUsers
							+ " LIMIT 1;");
			ResultSet set = sql.executeQuery();
			set.next();

			sql.close();
			set.close();

			Main.getInstance().getLogger().info("BungeeStats Users table exists!");
		} catch (Exception e) {
			Main.getInstance().getLogger()
					.info("BungeeStats Users table doesn't exist, creating one.");
			createTableUsers();
		}
	}

	public synchronized boolean playerDataContainsPlayer(String uuid) {
		try {
			PreparedStatement sql = connection
					.prepareStatement("SELECT * FROM " + Main.table
							+ " WHERE UUID=?;");
			sql.setString(1, uuid);
			ResultSet resultSet = sql.executeQuery();
			boolean containsPlayer = resultSet.next();

			sql.close();
			resultSet.close();

			return containsPlayer;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public synchronized boolean playerDataContainsPlayerUsers(String uuid) {
		try {
			PreparedStatement sql = connection
					.prepareStatement("SELECT * FROM " + Main.tableUsers
							+ " WHERE UUID=?;");
			sql.setString(1, uuid);
			ResultSet resultSet = sql.executeQuery();
			boolean containsPlayer = resultSet.next();

			sql.close();
			resultSet.close();

			return containsPlayer;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public synchronized void emptyTable() {
		try {
			PreparedStatement sql = connection
					.prepareStatement("TRUNCATE TABLE " + Main.table + ";");
			sql.executeUpdate();
			sql.close();
			Main.getInstance().getLogger().info("BungeeStats table emptyed!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void addPlayer(String uuid, String player) {
		try {
			PreparedStatement sql = connection
					.prepareStatement("INSERT INTO "
							+ Main.table
							+ " (Player, UUID, OnlineTime, LastServer, CurrentServer, FirstJoined, LastJoined, Online, LastJoinedTime) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);");
			sql.setString(1, player);
			sql.setString(2, uuid);
			sql.setString(3, "0");
			sql.setString(4, "0");
			sql.setString(5, "0");
			sql.setString(6, Main.Date());
			sql.setString(7, Main.Date());
			sql.setString(8, "True");
			sql.setString(9, Main.Time());
			sql.execute();
			sql.close();
		} catch (Exception e) {
			Main.getInstance().getLogger()
					.log(Level.SEVERE, "Could not log to database!");
			e.printStackTrace();
		}
	}
	
	public synchronized void addPlayerUsers(String uuid, String player, String group) {
		try {
			PreparedStatement sql = connection
					.prepareStatement("INSERT INTO "
							+ Main.tableUsers
							+ " (Player, UUID, PGroup) VALUES (?, ?, ?);");
			sql.setString(1, player);
			sql.setString(2, uuid);
			sql.setString(3, group);
			sql.execute();
			sql.close();
		} catch (Exception e) {
			Main.getInstance().getLogger()
					.log(Level.SEVERE, "Could not log to database!");
			e.printStackTrace();
		}
	}

	public synchronized void clearallOnlinesandCurrentServers(String Online,
			String CurrentServer) {
		try {
			PreparedStatement sql = connection.prepareStatement("UPDATE "
					+ Main.table + " SET Online = ?, CurrentServer = ?;");
			sql.setString(1, Online);
			sql.setString(2, CurrentServer);
			sql.execute();
			sql.close();

			Main.getInstance()
					.getLogger()
					.log(Level.SEVERE,
							"Wipped All Online Status and CurrentServers");
		} catch (Exception e) {
			Main.getInstance().getLogger()
					.log(Level.SEVERE, "Could not log to database!");
		}
	}

	public synchronized void updatePlayer(String uuid, String player,
			String online, String currentserver) {
		try {
			PreparedStatement sql = connection
					.prepareStatement("UPDATE "
							+ Main.table
							+ " SET LastJoined = ?, Online = ?, CurrentServer = ?, Player = ?, LastJoinedTime = ? WHERE UUID = ?;");
			sql.setString(1, Main.Date());
			sql.setString(2, online);
			sql.setString(3, currentserver);
			sql.setString(4, player);
			sql.setString(5, Main.Time());
			sql.setString(6, uuid);
			sql.execute();
			sql.close();
		} catch (Exception e) {
			Main.getInstance().getLogger()
					.log(Level.SEVERE, "Could not log to database!");
			e.printStackTrace();
		}
	}
	
	public synchronized void updatePlayerUsers(String uuid, String player) {
		try {
			PreparedStatement sql = connection
					.prepareStatement("UPDATE "
							+ Main.tableUsers
							+ " SET Player = ? WHERE UUID = ?;");
			sql.setString(1, player);
			sql.setString(2, uuid);
			sql.execute();
			sql.close();
		} catch (Exception e) {
			Main.getInstance().getLogger()
					.log(Level.SEVERE, "Could not log to database!");
			e.printStackTrace();
		}
	}

	public synchronized void serverSwitch(String uuid, String currentserver) {
		try {
			PreparedStatement sql = connection.prepareStatement("UPDATE "
					+ Main.table
					+ " SET LastServer = ?, CurrentServer = ? WHERE UUID = ?;");
			sql.setString(1, currentserver);
			sql.setString(2, currentserver);
			sql.setString(3, uuid);
			sql.execute();
		} catch (Exception e) {
			Main.getInstance().getLogger()
					.log(Level.SEVERE, "Could not log to database!");
			e.printStackTrace();
		}
	}
	
	public synchronized void updateComandCount(String uuid, String total) {
		try {
			PreparedStatement sql = connection.prepareStatement("UPDATE "
					+ Main.table
					+ " SET CommandCount = ? WHERE UUID = ?;");
			sql.setString(1, total);
			sql.setString(2, uuid);
			sql.execute();
		} catch (Exception e) {
			Main.getInstance().getLogger()
					.log(Level.SEVERE, "Could not log to database!");
			e.printStackTrace();
		}
	}
	
	public synchronized void UpdateUserGroup(String uuid, String group) {
		try {
			PreparedStatement sql = connection.prepareStatement("UPDATE "
					+ Main.tableUsers
					+ " SET PGroup = ? WHERE UUID = ?;");
			sql.setString(1, group);
			sql.setString(2, uuid);
			sql.execute();
		} catch (Exception e) {
			Main.getInstance().getLogger()
					.log(Level.SEVERE, "Could not log to database!");
			e.printStackTrace();
		}
	}
	
	public synchronized void updateChatCount(String uuid, String total) {
		try {
			PreparedStatement sql = connection.prepareStatement("UPDATE "
					+ Main.table
					+ " SET ChatCount = ? WHERE UUID = ?;");
			sql.setString(1, total);
			sql.setString(2, uuid);
			sql.execute();
		} catch (Exception e) {
			Main.getInstance().getLogger()
					.log(Level.SEVERE, "Could not log to database!");
			e.printStackTrace();
		}
	}

	public synchronized boolean playerCheck(String player) {
		try {
			PreparedStatement sql = connection
					.prepareStatement("SELECT * FROM " + Main.table
							+ " WHERE Player = ?;");
			sql.setString(1, player);
			ResultSet resultSet = sql.executeQuery();
			boolean containsPlayer = resultSet.next();

			sql.close();
			resultSet.close();

			return containsPlayer;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void sendOnlineTime(ProxiedPlayer p, String h)
			throws SQLException {
		int time = 0;
		PreparedStatement sql = connection
				.prepareStatement("SELECT OnlineTime FROM " + Main.table
						+ " WHERE Player = ?;");
		sql.setString(1, h);
		ResultSet resultSet = sql.executeQuery();
		try {
			while (resultSet.next()) {
				time = Integer.parseInt(resultSet.getString("OnlineTime"));
			}
		} catch (SQLException e) {
			System.out.println("MySQL-Error: " + e.getMessage());
		}
		int hours = time / 60;
		int minutes = time % 60;
		
		p.sendMessage(new TextComponent(Main.getInstance().Prefix + ChatColor.GREEN + h + " has a playtime: " + ChatColor.BLUE + hours + ChatColor.BLUE + " Hours " + ChatColor.BLUE + "and " + ChatColor.BLUE + minutes + ChatColor.BLUE + " Minutes!"));
	}

	public static boolean hasTime(ProxiedPlayer p) throws SQLException {
		PreparedStatement sql = connection.prepareStatement("SELECT * FROM "
				+ Main.table + " WHERE UUID = ?;");
		sql.setString(1, p.getUniqueId().toString());
		ResultSet resultSet = sql.executeQuery();
		try {
			if (resultSet.next()) {
				return true;
			}
		} catch (SQLException e) {
			System.out.println("MySQL-Error: " + e.getMessage());
		}
		return false;
	}
}