package bukkit.gwarband.privatchat;

import org.bukkit.ChatColor;
import org.bukkit.event.player.*;
import org.bukkit.entity.Player;

public class PrivatChatPlayerListener extends PlayerListener
{
	private final PrivatChat plugin;
	
	public PrivatChatPlayerListener(PrivatChat instance)
	{
        plugin = instance;
    }
	
	public void onPlayerJoin(PlayerJoinEvent event)
	{
	  Player player = event.getPlayer();
	  plugin.conservationopen.put(player.getName().toLowerCase(), false);
	  plugin.setchat.put(player.getName().toLowerCase(), false);
	}
	
	public void onPlayerQuit(PlayerQuitEvent event)
	{
	  Player player = event.getPlayer();
	  try {
		  for (int i=0; i<=plugin.laenge; i++) {
			  Player player2 = plugin.conservation.get(player.getName().toLowerCase() + "[" + i + "]");
			  player2.sendMessage(ChatColor.GOLD + "Player left the game! To close the chat please write /chatexit");
		  } 
	  }
	  catch (NullPointerException e) {}
	  plugin.conservationopen.remove(player.getName().toLowerCase());
	}
	
	public void onPlayerChat(PlayerChatEvent event)
	{
		try{
			Player player = event.getPlayer();
			if(plugin.conservationopen.get(player.getName().toLowerCase()) == true)
			{
				String message = event.getMessage();
				event.setCancelled(true);
				for (int i=0; i<=plugin.laenge; i++) {
					Player player2 = plugin.conservation.get(player.getName().toLowerCase() + "[" + i + "]");
					player2.sendMessage(ChatColor.BLUE + "[PrivatChat] "+player.getName()+": "+ChatColor.WHITE+message);
				}
				player.sendMessage(ChatColor.BLUE + "[PrivatChat] "+player.getName()+": "+ChatColor.WHITE+message);
			}
		}catch(NullPointerException e){}
	}
}