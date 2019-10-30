package ninja.darkcop.bungeeplayerstats.listeners;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import ninja.darkcop.bungeeplayerstats.Main;

public class Quit implements Listener {

	public Quit(Main This) {
		super();
	}

	@EventHandler
	public void PlayerQuitEvent(PlayerDisconnectEvent event) {
		ProxiedPlayer player = event.getPlayer();

		if (Main.getInstance().sql.playerDataContainsPlayer(player
				.getUniqueId().toString())) {
			Main.getInstance().sql.updatePlayer(
					player.getUniqueId().toString(), player.getName()
							.toString(), "False", "Offline");
		}

	}
}
