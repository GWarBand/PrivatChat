package bukkit.gwarband.privatchat;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PrivatChatWorker {
	
	public PrivatChat plugin;

	public PrivatChatWorker(PrivatChat tplugin) {
		plugin = tplugin;
	}

	public boolean chatopen(Player player, String[] args) {
		if (!PrivatChat.perm(player, "privatchat.chat")) {
			player.sendMessage(ChatColor.RED + "You don't have Permissions to use this Command!");
			return true;
		}
		for (int i=0; i<=plugin.laenge; i++) {
			Player player2 = plugin.getServer().getPlayer(args[i]);
			plugin.conservation.put(player.getName().toLowerCase() + "[" + i + "]", player2);
		}
		plugin.conservationopen.put(player.getName().toLowerCase(), true);
		player.sendMessage(ChatColor.GOLD + "You have a new privat chat");
		player.sendMessage(ChatColor.GOLD + "To close the privat chat please write /chatexit");
		return true;
	}

	public boolean chatclose(Player player) {
		plugin.conservation.remove(player.getName().toLowerCase());
		player.sendMessage(ChatColor.GOLD + "Chat close");
		plugin.conservationopen.put(player.getName().toLowerCase(), false);
		return true;
	}
	
	public boolean setchat(Player player, String[] args) {
		if (!PrivatChat.perm(player, "privatchat.set")) {
			player.sendMessage(ChatColor.RED + "You don't have Permissions to use this Command!");
			return true;
		}
		Player player2 = plugin.getServer().getPlayer(args[1]);
		plugin.setchat.put(player2.getName().toLowerCase(), true);
		plugin.conservation.put(player.getName().toLowerCase() + "[0]", player2);
		plugin.conservation.put(player2.getName().toLowerCase() + "[0]", player);
		plugin.conservationopen.put(player.getName().toLowerCase(), true);
		plugin.conservationopen.put(player2.getName().toLowerCase(), true);
		player2.sendMessage(ChatColor.DARK_RED + "You are now in a conservation with " + player.getDisplayName());
		player.sendMessage(ChatColor.GOLD + "You are now in a conservation with " + player2.getDisplayName());
		return true;
	}
	
	public boolean unsetchat(Player player, String[] args) {
		if (!PrivatChat.perm(player, "privatchat.unset")) {
			player.sendMessage(ChatColor.RED + "You don't have Permissions to use this Command!");
			return true;
		}
		Player player2 = plugin.getServer().getPlayer(args[1]);
		plugin.setchat.put(player2.getName().toLowerCase(), false);
		plugin.conservation.remove(player.getName().toLowerCase() + "[0]");
		plugin.conservation.remove(player2.getName().toLowerCase() + "[0]");
		plugin.conservationopen.put(player.getName().toLowerCase(), false);
		plugin.conservationopen.put(player2.getName().toLowerCase(), false);
		player2.sendMessage(ChatColor.DARK_RED + "Chat closed with " + player.getDisplayName());
		player.sendMessage(ChatColor.GOLD + "Chat closed with " + player2.getDisplayName());
		return true;
	}

	public boolean chatmsg(Player player, String args[]) {
		String msg = "";
		for (int i = 0; args.length > i; i++) {
			msg += args[i] + " ";
			System.out.println(args[i]);
		}
		plugin.getServer().broadcastMessage(getPrefix(player) + "[" + getGroup(player) + "] " + ChatColor.WHITE + player.getName() + ": " + msg);
		return true;
	}

	@SuppressWarnings("deprecation")
	public String getGroup(Player player) {
		if (PrivatChat.permissionHandler != null) {
			String group = PrivatChat.permissionHandler.getGroup(player.getWorld().getName(), player.getName());
			return group;
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public ChatColor getPrefix(Player player) {
		if (PrivatChat.permissionHandler != null) {
			// Check for user prefix first
			String userPrefix = (PrivatChat.permissionHandler).getUserPermissionString(player.getWorld().getName(),player.getName(), "prefix");
			if (userPrefix != null && !userPrefix.isEmpty()) {
				if (userPrefix.equalsIgnoreCase("&0"))
					return ChatColor.BLACK;
				else if (userPrefix.equalsIgnoreCase("&1"))
					return ChatColor.DARK_BLUE;
				else if (userPrefix.equalsIgnoreCase("&2"))
					return ChatColor.DARK_GREEN;
				else if (userPrefix.equalsIgnoreCase("&3"))
					return ChatColor.DARK_AQUA;
				else if (userPrefix.equalsIgnoreCase("&4"))
					return ChatColor.DARK_RED;
				else if (userPrefix.equalsIgnoreCase("&5"))
					return ChatColor.DARK_PURPLE;
				else if (userPrefix.equalsIgnoreCase("&6"))
					return ChatColor.GOLD;
				else if (userPrefix.equalsIgnoreCase("&7"))
					return ChatColor.GRAY;
				else if (userPrefix.equalsIgnoreCase("&8"))
					return ChatColor.DARK_GRAY;
				else if (userPrefix.equalsIgnoreCase("&9"))
					return ChatColor.BLUE;
				else if (userPrefix.equalsIgnoreCase("&a"))
					return ChatColor.GREEN;
				else if (userPrefix.equalsIgnoreCase("&b"))
					return ChatColor.AQUA;
				else if (userPrefix.equalsIgnoreCase("&c"))
					return ChatColor.RED;
				else if (userPrefix.equalsIgnoreCase("&d"))
					return ChatColor.LIGHT_PURPLE;
				else if (userPrefix.equalsIgnoreCase("&e"))
					return ChatColor.YELLOW;
				else
					return ChatColor.WHITE;
			}
			// Check if the group has a prefix.
			String group = PrivatChat.permissionHandler.getGroup(player.getWorld().getName(), player.getName());
			if (group == null)
				return null;
			String groupPrefix = PrivatChat.permissionHandler.getGroupPrefix(player.getWorld().getName(), group);
			if (groupPrefix.equalsIgnoreCase("&0"))
				return ChatColor.BLACK;
			else if (groupPrefix.equalsIgnoreCase("&1"))
				return ChatColor.DARK_BLUE;
			else if (groupPrefix.equalsIgnoreCase("&2"))
				return ChatColor.DARK_GREEN;
			else if (groupPrefix.equalsIgnoreCase("&3"))
				return ChatColor.DARK_AQUA;
			else if (groupPrefix.equalsIgnoreCase("&4"))
				return ChatColor.DARK_RED;
			else if (groupPrefix.equalsIgnoreCase("&5"))
				return ChatColor.DARK_PURPLE;
			else if (groupPrefix.equalsIgnoreCase("&6"))
				return ChatColor.GOLD;
			else if (groupPrefix.equalsIgnoreCase("&7"))
				return ChatColor.GRAY;
			else if (groupPrefix.equalsIgnoreCase("&8"))
				return ChatColor.DARK_GRAY;
			else if (groupPrefix.equalsIgnoreCase("&9"))
				return ChatColor.BLUE;
			else if (groupPrefix.equalsIgnoreCase("&a"))
				return ChatColor.GREEN;
			else if (groupPrefix.equalsIgnoreCase("&b"))
				return ChatColor.AQUA;
			else if (groupPrefix.equalsIgnoreCase("&c"))
				return ChatColor.RED;
			else if (groupPrefix.equalsIgnoreCase("&d"))
				return ChatColor.LIGHT_PURPLE;
			else if (groupPrefix.equalsIgnoreCase("&e"))
				return ChatColor.YELLOW;
			else
				return ChatColor.WHITE;
		}
		return null;
	}
}