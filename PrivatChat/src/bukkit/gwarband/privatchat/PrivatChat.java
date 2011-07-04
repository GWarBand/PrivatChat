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

public class PrivatChat extends JavaPlugin
{
	public static Logger log = Logger.getLogger("Minecraft");
	private final PrivatChatWorker worker = new PrivatChatWorker(this);
	public HashMap<String, Boolean> conservationopen = new HashMap<String, Boolean>();
	public HashMap<String, Boolean> setchat = new HashMap<String, Boolean>();
	public HashMap<String, Player> conservation = new HashMap<String, Player>();
	int laenge = 0;
	private final PrivatChatPlayerListener playerListener = new PrivatChatPlayerListener(this);
	public static PermissionHandler permissionHandler;
	public static boolean permFound;

	
	public void onDisable() 
	{
		log.info("[PrivatChat] Plugin disable");
	}

	public void onEnable()
	{	  
		setupPermissions();
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_CHAT, playerListener, Priority.Monitor, this);
		log.info("[PrivatChat] Plugin enable");
	}
	
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) 
	{
		String commandName = command.getName().toLowerCase();
		Player player = (Player) sender;
		try {
			if(this.setchat.get(player.getName().toLowerCase()) == false) {
				if(args.length >= 1 && commandName.equals("pchat"))
				{
					if (args[0].equalsIgnoreCase("add")) {
						if (!PrivatChat.perm(player, "privatchat.add")) {
							player.sendMessage(ChatColor.RED + "You don't have Permissions to use this Command!");
							return true;
						}
						this.laenge+=1;
						Player player2 = this.getServer().getPlayer(args[1]);
						this.conservation.put(player.getName().toLowerCase() + "[" + laenge + "]", player2);
						player.sendMessage(ChatColor.GOLD + "You added " + player2.getDisplayName() + " to the chat!");
					} else if(args[0].equalsIgnoreCase("remove")) {
						if (!PrivatChat.perm(player, "privatchat.remove")) {
							player.sendMessage(ChatColor.RED + "You don't have Permissions to use this Command!");
							return true;
						}
						Player player2 = this.getServer().getPlayer(args[1]);
						for (int i=0; i<=this.laenge; i++) {
							if (this.conservation.get(player.getName().toLowerCase() + "[" + i + "]").equals(player2))
								this.conservation.remove(player.getName().toLowerCase() + "[" + i + "]");
						}
						this.laenge-=1;
						player.sendMessage(ChatColor.GOLD + "You removed " + player2.getDisplayName() + " from the chat!");
					} else if(args[0].equalsIgnoreCase("list")){
						if (!PrivatChat.perm(player, "privatchat.list")) {
							player.sendMessage(ChatColor.RED + "You don't have Permissions to use this Command!");
							return true;
						}
						String list = "";
						for (int i=0; i<=this.laenge; i++) {
							list = " " + this.conservation.get(player.getName().toLowerCase() + "[" + i + "]").getDisplayName();
						}
						player.sendMessage(ChatColor.BLUE + "The following people are chatting with you:" + list);					
					} else if(args[0].equalsIgnoreCase("set")) {
						return worker.setchat(player, args);
					} else if(args[0].equalsIgnoreCase("unset")) {
						return worker.unsetchat(player, args);
					} else if(commandName.equals("pchat")) {
						this.laenge = args.length-1;
						return worker.chatopen(player, args);
					}
				}else if(commandName.equals("pchat")){
					player.sendMessage(ChatColor.RED + "/pchat <player player ...>");
				}
			
				if(commandName.equals("chatexit"))
				{
					for (int i=0; i<=this.laenge; i++) {
						this.conservation.get(player.getName().toLowerCase() + "[" + i + "]").sendMessage(ChatColor.GOLD + player.getDisplayName() + " closed the chat!");
					}
					return worker.chatclose(player);
				}
	  
				if(args.length > 0 && commandName.equals("bm"))
				{
					if(commandName.equals("bm"))
					{
						return worker.chatmsg(player, args);
					}
				}else if(commandName.equals("bm")){
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