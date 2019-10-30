package ninja.darkcop.bungeeplayerstats.listeners;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import ninja.darkcop.bungeeplayerstats.Main;

public class ServerSwitch implements Listener {

	public ServerSwitch(Main This) {
		super();
	}

	@EventHandler
	public void ServerSwitchEvent(ServerSwitchEvent event) {
		ProxiedPlayer player = event.getPlayer();
		String CurrentServer = player.getServer().getInfo().getName();

		if (Main.getInstance().sql.playerDataContainsPlayer(player
				.getUniqueId().toString())) {
			Main.getInstance().sql.serverSwitch(
					player.getUniqueId().toString(), CurrentServer);
		}

	}
}
