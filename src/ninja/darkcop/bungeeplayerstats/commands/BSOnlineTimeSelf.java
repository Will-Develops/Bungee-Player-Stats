package ninja.darkcop.bungeeplayerstats.commands;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import ninja.darkcop.bungeeplayerstats.Main;
import ninja.darkcop.bungeeplayerstats.mysql.MySQL;

public class BSOnlineTimeSelf extends Command implements TabExecutor {

	public BSOnlineTimeSelf(Main This) {
		super("onlinetime");
	}

	public void execute(CommandSender sender, String[] args) {
		ProxiedPlayer p = (ProxiedPlayer) sender;

		if (args.length < 1) {
			p.sendMessage(new TextComponent(Main.getInstance().Prefix
					+ "Player Online Time"));
			p.sendMessage(new TextComponent(ChatColor.GREEN + "/onlinetime me"
					+ ChatColor.BLUE + " - Check you're online time"));
			p.sendMessage(new TextComponent(ChatColor.GREEN
					+ "/onlinetime {Player}" + ChatColor.BLUE
					+ " - Check a players online time"));
		}

		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("me")) {
				try {
					MySQL.sendOnlineTime(p, p.getName());
				} catch (SQLException e) {
				}
			} else {
				try {
					MySQL.sendOnlineTime(p, args[0]);
				} catch (SQLException e) {
				}
			}
		}

		if (args.length > 1) {
			p.sendMessage(new TextComponent(Main.getInstance().Prefix
					+ "Player Online Time"));
			p.sendMessage(new TextComponent(ChatColor.GREEN + "/onlinetime me"
					+ ChatColor.BLUE + " - Check you're online time"));
			p.sendMessage(new TextComponent(ChatColor.GREEN
					+ "/onlinetime {Player}" + ChatColor.BLUE
					+ " - Check a players online time"));
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