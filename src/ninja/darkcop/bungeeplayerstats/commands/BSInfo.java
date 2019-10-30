package ninja.darkcop.bungeeplayerstats.commands;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import ninja.darkcop.bungeeplayerstats.Main;
import ninja.darkcop.bungeeplayerstats.mysql.MySQL;

import com.google.common.collect.ImmutableSet;

public class BSInfo extends Command implements TabExecutor {

	public BSInfo(Main This) {
		super("bsinfo", "bungeestats.bsinfo");
	}
	
	public void execute(CommandSender sender, String[] args) {
		final ProxiedPlayer player = (ProxiedPlayer) sender;

		if (args.length < 1) {
			player.sendMessage(new TextComponent(Main.getInstance().Prefix
					+ "Player Stats"));
			player.sendMessage(new TextComponent(ChatColor.GREEN + "/bsinfo me"
					+ ChatColor.BLUE + " - Check you're stats"));
			player.sendMessage(new TextComponent(ChatColor.GREEN
					+ "/bsinfo {Player}" + ChatColor.BLUE
					+ " - Check a players stats"));
		}

		if (args.length == 1) {
			String Options = args[0];
			if (Options.equalsIgnoreCase("me")) {
				String PlayerName = null;
				String UUID = null;
				int OnlineTime = 0;
				String LastServer = null;
				String CurrentServer = null;
				String FirstJoined = null;
				String LastJoined = null;
				String Online = null;
				String LastJoinedTime = null;
				try {
					PreparedStatement sql = MySQL.connection
							.prepareStatement("SELECT * FROM " + Main.table
									+ " WHERE Player = ?;");
					sql.setString(1, player.getName());
					ResultSet resultSet = sql.executeQuery();
					resultSet.next();

					PlayerName = resultSet.getString("Player");
					UUID = resultSet.getString("UUID");
					OnlineTime = resultSet.getInt("OnlineTime");
					LastServer = resultSet.getString("LastServer");
					CurrentServer = resultSet.getString("CurrentServer");
					FirstJoined = resultSet.getString("FirstJoined");
					LastJoined = resultSet.getString("LastJoined");
					Online = resultSet.getString("Online");
					LastJoinedTime = resultSet.getString("LastJoinedTime");

					sql.close();
					resultSet.close();

					int hours = OnlineTime / 60;
					int minutes = OnlineTime % 60;

					player.sendMessage(new TextComponent(
							Main.getInstance().Prefix + "Player Stats for "
									+ ChatColor.YELLOW + PlayerName));
					player.sendMessage(new TextComponent(ChatColor.GREEN
							+ "Username: " + ChatColor.BLUE + PlayerName));
					player.sendMessage(new TextComponent(ChatColor.GREEN
							+ "UUID: " + ChatColor.BLUE + UUID));
					player.sendMessage(new TextComponent(ChatColor.GREEN
							+ "Online Time: " + ChatColor.BLUE + hours
							+ " Hours and " + minutes + " Minutes!"));
					player.sendMessage(new TextComponent(ChatColor.GREEN
							+ "Last Server: " + ChatColor.BLUE + LastServer));
					player.sendMessage(new TextComponent(ChatColor.GREEN
							+ "Current Server: " + ChatColor.BLUE
							+ CurrentServer));
					player.sendMessage(new TextComponent(ChatColor.GREEN
							+ "First Joined: " + ChatColor.BLUE + FirstJoined));
					player.sendMessage(new TextComponent(ChatColor.GREEN
							+ "Last Joined Date: " + ChatColor.BLUE
							+ LastJoined));
					player.sendMessage(new TextComponent(ChatColor.GREEN
							+ "Last Joined Time: " + ChatColor.BLUE
							+ LastJoinedTime));
					player.sendMessage(new TextComponent(ChatColor.GREEN
							+ "Online: " + ChatColor.BLUE + Online));

				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				if (Main.getInstance().sql.playerCheck(Options)) {
					String PlayerName = null;
					String UUID = null;
					int OnlineTime = 0;
					String LastServer = null;
					String CurrentServer = null;
					String FirstJoined = null;
					String LastJoined = null;
					String Online = null;
					String LastJoinedTime = null;
					try {
						PreparedStatement sql = MySQL.connection
								.prepareStatement("SELECT * FROM " + Main.table
										+ " WHERE Player = ?;");
						sql.setString(1, Options);
						ResultSet resultSet = sql.executeQuery();
						resultSet.next();

						PlayerName = resultSet.getString("Player");
						UUID = resultSet.getString("UUID");
						OnlineTime = resultSet.getInt("OnlineTime");
						LastServer = resultSet.getString("LastServer");
						CurrentServer = resultSet.getString("CurrentServer");
						FirstJoined = resultSet.getString("FirstJoined");
						LastJoined = resultSet.getString("LastJoined");
						Online = resultSet.getString("Online");
						LastJoinedTime = resultSet.getString("LastJoinedTime");

						sql.close();
						resultSet.close();

						int hours = OnlineTime / 60;
						int minutes = OnlineTime % 60;

						player.sendMessage(new TextComponent(
								Main.getInstance().Prefix + "Player Stats for "
										+ ChatColor.YELLOW + PlayerName));
						player.sendMessage(new TextComponent(ChatColor.GREEN
								+ "Username: " + ChatColor.BLUE + PlayerName));
						player.sendMessage(new TextComponent(ChatColor.GREEN
								+ "UUID: " + ChatColor.BLUE + UUID));
						player.sendMessage(new TextComponent(ChatColor.GREEN
								+ "Online Time: " + ChatColor.BLUE + hours
								+ " Hours and " + minutes + " Minutes!"));
						player.sendMessage(new TextComponent(ChatColor.GREEN
								+ "Last Server: " + ChatColor.BLUE + LastServer));
						player.sendMessage(new TextComponent(ChatColor.GREEN
								+ "Current Server: " + ChatColor.BLUE
								+ CurrentServer));
						player.sendMessage(new TextComponent(ChatColor.GREEN
								+ "First Joined: " + ChatColor.BLUE
								+ FirstJoined));
						player.sendMessage(new TextComponent(ChatColor.GREEN
								+ "Last Joined Date: " + ChatColor.BLUE
								+ LastJoined));
						player.sendMessage(new TextComponent(ChatColor.GREEN
								+ "Last Joined Time: " + ChatColor.BLUE
								+ LastJoinedTime));
						player.sendMessage(new TextComponent(ChatColor.GREEN
								+ "Online: " + ChatColor.BLUE + Online));

					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					player.sendMessage(new TextComponent(
							Main.getInstance().Prefix + "Player Not Found!"));
				}
			}
		}

		if (args.length > 1) {
			player.sendMessage(new TextComponent(Main.getInstance().Prefix
					+ "Player Stats"));
			player.sendMessage(new TextComponent(ChatColor.GREEN + "/bsinfo me"
					+ ChatColor.BLUE + " - Check you're stats"));
			player.sendMessage(new TextComponent(ChatColor.GREEN
					+ "/bsinfo {Player}" + ChatColor.BLUE
					+ " - Check a players stats"));
		}
	}

	public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
		if (args.length > 2 || args.length == 0) {
			return ImmutableSet.of();
		}

		Set<String> matches = new HashSet<>();
		if (args.length == 1) {
			String search = args[0].toLowerCase();
			for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
				if (player.getName().toLowerCase().startsWith(search)) {
					matches.add(player.getName());
				}
			}
		} else {
			String search = args[1].toLowerCase();
			for (String server : ProxyServer.getInstance().getServers()
					.keySet()) {
				if (server.toLowerCase().startsWith(search)) {
					matches.add(server);
				}
			}
		}
		return matches;
	}
}