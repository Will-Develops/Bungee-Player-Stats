package ninja.darkcop.bungeeplayerstats.commands;

import java.util.HashSet;
import java.util.Set;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import ninja.darkcop.bungeeplayerstats.Main;

import com.google.common.collect.ImmutableSet;

public class BSGroup extends Command implements TabExecutor{

	public BSGroup(Main This) {
		//super("BSGroup", "bungeestats.group");
		super("BSGroup");
	}

	public void execute(CommandSender sender, String[] args) {

		ProxiedPlayer player = (ProxiedPlayer) sender;
		
		
		if(args.length < 1 || args.length == 1 || args.length > 2){
			player.sendMessage(new TextComponent(Main.getInstance().Prefix + "/bsgroup {Name} {Group}"));
		}
		
		if(args.length == 2){
			ProxiedPlayer target = Main.getInstance().getProxy().getPlayer(args[0]);
			String group = args[1];
			if(target != null){
				if (Main.getInstance().sql.playerDataContainsPlayerUsers(target.getUniqueId().toString())) {
					Main.getInstance().sql.UpdateUserGroup(target.getUniqueId().toString(), group);
					player.sendMessage(new TextComponent(Main.getInstance().Prefix + "Set " + target.getName() + " to group " + group));
				} else {
					player.sendMessage(new TextComponent(Main.getInstance().Prefix + "Player Not Found In Database!"));
				}
			} else {
				player.sendMessage(new TextComponent(Main.getInstance().Prefix + "Player Not Online!"));
			}
			
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