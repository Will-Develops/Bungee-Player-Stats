package ninja.darkcop.bungeeplayerstats.listeners;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import ninja.darkcop.bungeeplayerstats.Main;

public class Join implements Listener {

	public Join(Main This) {
		super();
	}

	@EventHandler
	public void PlayerJoinEvent(PostLoginEvent event) {
		ProxiedPlayer player = event.getPlayer();

		if (!(Main.getInstance().sql.playerDataContainsPlayer(player
				.getUniqueId().toString()))) {
			Main.getInstance().sql.addPlayer(player.getUniqueId().toString(),
					player.getName().toString());
		} else {
			Main.getInstance().sql.updatePlayer(
					player.getUniqueId().toString(), player.getName()
							.toString(), "True", " ");
		}
		
		if (!(Main.getInstance().sql.playerDataContainsPlayerUsers(player
				.getUniqueId().toString()))) {
			Main.getInstance().sql.addPlayerUsers(player.getUniqueId().toString(),
					player.getName().toString(), "Player");
		} else {
			Main.getInstance().sql.updatePlayerUsers(player.getUniqueId().toString(), player.getName().toString());
		}
	}
}
