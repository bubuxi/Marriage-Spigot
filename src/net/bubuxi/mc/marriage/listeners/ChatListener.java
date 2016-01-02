package net.bubuxi.mc.marriage.listeners;

import net.bubuxi.mc.marriage.Marriage;
import net.bubuxi.mc.marriage.enums.Gender;
import net.bubuxi.mc.marriage.manager.MainManager;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

	
	MainManager mm;
	public ChatListener() {
		mm = Marriage.getInstance().mainM;
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onChat(AsyncPlayerChatEvent e) {
		String format = e.getFormat();
		if(mm.getGender(e.getPlayer().getName())==Gender.MALE) {
			format =  ChatColor.BLUE + "��" + ChatColor.RESET + format;
		}
		else if(mm.getGender(e.getPlayer().getName())==Gender.FEMALE) {
			format = ChatColor.LIGHT_PURPLE + "��" + ChatColor.RESET + format;
		}
		e.setFormat(format);
	}
	
	
}
