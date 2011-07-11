package bukkit.gwarband.privatchat;

import org.bukkit.ChatColor;
import org.bukkit.event.player.*;
import org.bukkit.entity.Player;

public class PrivatChatPlayerListener extends PlayerListener {
	private final PrivatChat plugin;
	
	public PrivatChatPlayerListener(PrivatChat instance) {
        plugin = instance;
    }
	
	public void onPlayerJoin(PlayerJoinEvent event) {
	  Player player = event.getPlayer();
	  plugin.conservationopen.put(player.getName().toLowerCase(), false);
	  plugin.setchat.put(player.getName().toLowerCase(), false);
	  plugin.laenge.put(player.getName().toLowerCase(), 0);
	}
	
	public void onPlayerQuit(PlayerQuitEvent event) {
	  Player player = event.getPlayer();
	  try {
		  for (int i=0; i<=plugin.laenge.get(player.getName().toLowerCase()); i++) {
			  Player player2 = plugin.conservation.get(player.getName().toLowerCase() + "[" + i + "]");
			  player2.sendMessage(ChatColor.GOLD + "Player left the game! To close the chat please write /pchat remove " + player.getDisplayName().toLowerCase());
		  } 
	  }
	  catch (NullPointerException e) {}
	  plugin.conservationopen.remove(player.getName().toLowerCase());
	}
	
	public void onPlayerChat(PlayerChatEvent event) {
		try{
			String message = event.getMessage();
			Player player = event.getPlayer();
			if(plugin.conservationopen.get(player.getName().toLowerCase())) {
	        	event.setCancelled(true);
	        	player.sendMessage(ChatColor.BLUE + "[PrivatChat] "+player.getName()+": "+ChatColor.WHITE+message);
	        	for (int i=0; i<=plugin.laenge.get(player.getName().toLowerCase()); i++) {
	        		try {
	        			Player player2 = plugin.conservation.get(player.getName().toLowerCase() + "[" + i + "]");
	        			player2.sendMessage(ChatColor.BLUE + "[PrivatChat] "+player.getName()+": "+ChatColor.WHITE+message);
	        		}
	        		catch (NullPointerException e) {}
	        	}	
	        }
		}
		catch(NullPointerException e) {}
	}
}