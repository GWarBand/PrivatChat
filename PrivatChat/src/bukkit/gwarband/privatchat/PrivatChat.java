package bukkit.gwarband.privatchat;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.event.Event;

import java.util.HashMap;
import java.util.logging.Logger;

import com.nijikokun.bukkit.Permissions.Permissions;
import com.nijiko.permissions.PermissionHandler;

public class PrivatChat extends JavaPlugin {
	
	public static Logger log = Logger.getLogger("Minecraft");
	private final PrivatChatWorker worker = new PrivatChatWorker(this);
	public HashMap<String, Boolean> conservationopen = new HashMap<String, Boolean>();
	public HashMap<String, Boolean> setchat = new HashMap<String, Boolean>();
	public HashMap<String, Player> conservation = new HashMap<String, Player>();
	public HashMap<String, Integer> laenge = new HashMap<String, Integer>();
	private final PrivatChatPlayerListener playerListener = new PrivatChatPlayerListener(this);
	public static PermissionHandler permissionHandler;
	public static boolean permFound;

	
	public void onDisable() {
		log.info("[PrivatChat] Plugin disable");
	}

	public void onEnable() {	  
		setupPermissions();
		worker.reload();
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_CHAT, playerListener, Priority.Lowest, this);
		log.info("[PrivatChat] Plugin enable");
	}
	
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		String commandName = command.getName().toLowerCase();
		Player player = (Player) sender;
		try {
			if(this.setchat.get(player.getName().toLowerCase()) == false) {
				if(args.length >= 1 && commandName.equals("pchat")) {
					if (args[0].equalsIgnoreCase("add")) {
						
						return worker.addchat(player, args);
						
					} else if(args[0].equalsIgnoreCase("remove")) {
						
						return worker.removechat(player, args);
						
					} else if(args[0].equalsIgnoreCase("list")) {
						
						return worker.listchat(player);
						
					} else if(args[0].equalsIgnoreCase("set")) {
						
						return worker.setchat(player, args);
						
					} else if(args[0].equalsIgnoreCase("unset")) {
						
						return worker.unsetchat(player, args);
						
					} else if(commandName.equals("pchat")) {
						
						return worker.chatopen(player, args);
					}
					
				} else if(commandName.equals("pchat")) {
					player.sendMessage(ChatColor.RED + "/pchat <player player ...>");
				}
			
				if(commandName.equals("chatexit")) {
					
					return worker.chatclose(player);
				}
	  
				if(args.length > 0 && commandName.equals("bm")) {
					if(commandName.equals("bm")) {
						return worker.chatmsg(player, args);
					}
				} else if(commandName.equals("bm")) {
					player.sendMessage(ChatColor.RED + "/bm <msg>");
				}
			} else {
				player.sendMessage(ChatColor.DARK_RED + "You are in a conservation with " + this.conservation.get(player.getName().toLowerCase() + "[0]").getDisplayName());
			}
		}
		catch (NullPointerException e) {
			player.sendMessage(ChatColor.DARK_RED + "You do not chat");
		}
		return true;
	}
	
	/**
	 * load Permissions plugin
	 */
    private void setupPermissions() {
        Plugin permissionsPlugin = this.getServer().getPluginManager().getPlugin("Permissions");        

        if (permissionHandler == null) {
            if (permissionsPlugin != null) {
                permissionHandler = ((Permissions)permissionsPlugin).getHandler();
                log.info("[PrivatChat] Permission enabled");
                permFound = true;
            } else {
                log.info("[PrivatChat] Permission system not detected");
                permFound = false;
            }
        }
    }
    
    /**
	 * checks Permissions
	 */
    public static boolean perm(Player player, String perm){
    	if (permFound) {
    		return PrivatChat.permissionHandler.has((Player)player, perm);
        } else {
            return true;
        }
    }
	
}