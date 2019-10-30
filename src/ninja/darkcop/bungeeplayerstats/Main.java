package ninja.darkcop.bungeeplayerstats;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import ninja.darkcop.bungeeplayerstats.commands.BSClearDatabase;
import ninja.darkcop.bungeeplayerstats.commands.BSGroup;
import ninja.darkcop.bungeeplayerstats.commands.BSInfo;
import ninja.darkcop.bungeeplayerstats.commands.BSOnlineTimeSelf;
import ninja.darkcop.bungeeplayerstats.commands.BSReload;
import ninja.darkcop.bungeeplayerstats.listeners.Join;
import ninja.darkcop.bungeeplayerstats.listeners.Quit;
import ninja.darkcop.bungeeplayerstats.listeners.ServerSwitch;
import ninja.darkcop.bungeeplayerstats.mysql.MySQL;

public class Main extends Plugin {

	private static Main instance;

	public static ConfigurationProvider cp = ConfigurationProvider
			.getProvider(YamlConfiguration.class);
	public static ConfigurationProvider cpUsers = ConfigurationProvider
			.getProvider(YamlConfiguration.class);
	public static String host;
	public static String port;
	public static String database;
	public static String table;
	public static String tableUsers;
	public static String username;
	public static String password;
	public String Prefix;

	public MySQL sql = new MySQL();

	public void onEnable() {
		ProxyServer.getInstance().getPluginManager()
				.registerListener(this, new Join(this));
		ProxyServer.getInstance().getPluginManager()
				.registerListener(this, new Quit(this));
		ProxyServer.getInstance().getPluginManager()
				.registerListener(this, new ServerSwitch(this));
		/*ProxyServer.getInstance().getPluginManager()
				.registerListener(this, new Chat(this));*/
		ProxyServer.getInstance().getPluginManager()
				.registerCommand(this, new BSOnlineTimeSelf(this));
		ProxyServer.getInstance().getPluginManager()
				.registerCommand(this, new BSClearDatabase(this));
		ProxyServer.getInstance().getPluginManager()
				.registerCommand(this, new BSInfo(this));
		ProxyServer.getInstance().getPluginManager()
				.registerCommand(this, new BSReload(this));
		ProxyServer.getInstance().getPluginManager()
				.registerCommand(this, new BSGroup(this));
		instance = this;

		if (!Main.getInstance().getDataFolder().exists()) {
			Main.getInstance().getDataFolder().mkdir();
			Main.getInstance().getDataFolder().setWritable(true);
			Main.getInstance().getDataFolder().setReadable(true);
		}

		try {
			loadConfigFile();
		} catch (IOException e) {
			System.out.println("Config Error: " + e.getMessage());
			Prefix = "Default Prefix";
		}

		sql.openConnection();
		try {
			sql.tableCheck();
			sql.tableCheckUsers();
			sql.clearallOnlinesandCurrentServers("False", "Offline");

		} catch (Exception e) {
			e.printStackTrace();
			MySQL.closeConnection();
			Main.getInstance().getLogger()
					.log(Level.SEVERE, "CONNECTION CLOSED!");
		}

		start();
	}

	public void onDisable() {
		MySQL.closeConnection();
		Main.getInstance().getLogger().log(Level.SEVERE, "CONNECTION CLOSED!");

	}

	public static Main getInstance() {
		return instance;
	}

	public static String Date() {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MMMM/yyyy");
		String date = formatter.format(calendar.getTime());
		return date;
	}

	public static String Time() {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		String date = formatter.format(calendar.getTime());
		return date;
	}

	public void loadConfigFile() throws IOException {
		File file = new File(getDataFolder(), "config.yml");
		if (!file.exists()) {
			file.createNewFile();
			Configuration cfg = cp.load(file);
			cfg.set("Strings.prefix", "Default Prefix");
			cfg.set("MySQL.host", "localhost");
			cfg.set("MySQL.port", "3306");
			cfg.set("MySQL.database", "minecraft");
			cfg.set("MySQL.table", "BungeeStats");
			cfg.set("MySQL.tableUsers", "Users");
			cfg.set("MySQL.username", "root");
			cfg.set("MySQL.password", "password");
			cp.save(cfg, file);
		}

		Configuration cfg = cp.load(file);
		host = cfg.getString("MySQL.host");
		port = cfg.getString("MySQL.port");
		database = cfg.getString("MySQL.database");
		table = cfg.getString("MySQL.table");
		tableUsers = cfg.getString("MySQL.tableUsers");
		username = cfg.getString("MySQL.username");
		password = cfg.getString("MySQL.password");

		Prefix = ChatColor.LIGHT_PURPLE + "[" + ChatColor.YELLOW
				+ cfg.getString("Strings.prefix") + ChatColor.LIGHT_PURPLE
				+ "]" + ChatColor.RESET + " ";

	}

	public static void saveTime(ProxiedPlayer all) {
		int time = 0;
		try {
			PreparedStatement sql = MySQL.connection
					.prepareStatement("SELECT OnlineTime FROM " + Main.table
							+ " WHERE UUID = ?;");
			sql.setString(1, all.getUniqueId().toString());
			ResultSet resultSet = sql.executeQuery();

			try {
				while (resultSet.next()) {
					time = Integer.parseInt(resultSet.getString("OnlineTime"));
				}
			} catch (SQLException e) {
				System.out.println("MySQL-Error: " + e.getMessage());
			}
		} catch (SQLException e) {
			System.out.println("MySQL-Error: " + e.getMessage());
		}
		time++;

		try {
			PreparedStatement sql1 = MySQL.connection
					.prepareStatement("UPDATE " + Main.table
							+ " SET OnlineTime = ? WHERE UUID = ?;");
			sql1.setInt(1, time);
			sql1.setString(2, all.getUniqueId().toString());
			sql1.executeUpdate();
		} catch (SQLException e) {
			System.out.println("MySQL-Error: " + e.getMessage());
		}
	}

	public void start() {
		getProxy().getScheduler().schedule(this, new Runnable() {
			public void run() {
				for (ProxiedPlayer all : Main.getInstance().getProxy()
						.getPlayers()) {
					saveTime(all);
				}
			}
		}, 1L, 1L, TimeUnit.MINUTES);
	}

}
