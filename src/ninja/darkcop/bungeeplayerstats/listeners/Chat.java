package ninja.darkcop.bungeeplayerstats.listeners;

import java.util.logging.Level;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import ninja.darkcop.bungeeplayerstats.Main;
import ninja.darkcop.bungeeplayerstats.BSPlayer;

public class Chat implements Listener {

	public Chat(Main This) {
		super();
	}
	
	BSPlayer player = new BSPlayer();

	@EventHandler
	public void PlayerChatEvent(ChatEvent event) {
		
		String msg = event.getMessage();
        ProxiedPlayer p = (ProxiedPlayer) event.getSender();
        player.name = p.getName();
        
        if (msg.startsWith("/")) {
        	player.commandCount++;
        } else {
        	player.chatCount++;
        }
        
        Main.getInstance().getLogger().log(Level.INFO, player.name + " > " + player.chatCount + " < chat | " + player.commandCount + " < command");
		
        
	}
}
