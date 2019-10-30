package ninja.darkcop.bungeeplayerstats.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import ninja.darkcop.bungeeplayerstats.Main;

public class BSClearDatabase extends Command {

	public BSClearDatabase(Main This) {
		super("bscleardatabase", "bungeestats.cleardatabase");
	}

	public void execute(CommandSender sender, String[] args) {

		ProxiedPlayer player = (ProxiedPlayer) sender;

		Main.getInstance().sql.emptyTable();
		player.sendMessage(new TextComponent(Main.getInstance().Prefix
				+ "Database cleared!"));

	}
}