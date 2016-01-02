package net.bubuxi.mc.marriage;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Logger {

	private static String prefix = "[Marriage] ";
	private static String debug = "[debug]";
	private static String info = "[info]";
	private static String warning = "[warning]";
	
	public static void debug(String msg) {
		if(Marriage.getInstance().getConfig().getBoolean("debug")) {
			System.out.println(prefix+debug+msg);
		}
	}
	
	public static void info(String msg) {
		System.out.println(prefix+info+msg);
	}
	
	public static void warning(String msg) {
		System.out.println(prefix+warning+msg);
	}
	
	public static void message(CommandSender sender, String msg) {
		sender.sendMessage(translate(msg));
	}
	
	public static void broadcast(String msg, String name1, String name2) {
		Bukkit.broadcastMessage(translate(msg.replaceFirst("%player%", name1).replaceFirst("%player%", name2)));
	}
	
	private static String translate(String str) {
		return ChatColor.translateAlternateColorCodes('&', str);
	}

	public static void message(String name, String msg) {
		if(Bukkit.getOfflinePlayer(name).isOnline()) {
			Bukkit.getPlayer(name).sendMessage(translate(msg));
		}
	}
}
