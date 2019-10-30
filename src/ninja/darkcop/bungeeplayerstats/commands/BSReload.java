package ninja.darkcop.bungeeplayerstats.commands;

import java.io.IOException;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import ninja.darkcop.bungeeplayerstats.Main;

public class BSReload extends Command {

	public BSReload(Main This) {
		super("bsreload", "bungeestats.reload");
	}

	public void execute(CommandSender sender, String[] args) {
		ProxiedPlayer player = (ProxiedPlayer) sender;
		try {
			Main.getInstance().loadConfigFile();
			player.sendMessage(new TextComponent(Main.getInstance().Prefix
					+ ChatColor.AQUA + "Configuration Reloaded"));
		} catch (IOException e) {
			player.sendMessage(new TextComponent(Main.getInstance().Prefix
					+ ChatColor.AQUA + "Configuration Reload Failed"));
			e.printStackTrace();
		}

	}
}