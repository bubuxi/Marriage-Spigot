package net.bubuxi.mc.marriage.listeners;

import java.util.Timer;
import java.util.TimerTask;

import net.bubuxi.mc.marriage.Lang;
import net.bubuxi.mc.marriage.Logger;
import net.bubuxi.mc.marriage.Marriage;
import net.bubuxi.mc.marriage.datatype.Couple;
import net.bubuxi.mc.marriage.manager.MainManager;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class LoginListener implements Listener {

	MainManager mm;
	Marriage m;
	
	public LoginListener() {
		m = Marriage.getInstance();
		mm = m.mainM;
		
	}
	
	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent e) {
		String name = e.getPlayer().getName();
		Timer timer = new Timer();
		timer.schedule(new CheckTask(name), 5000L);
		if(mm.isMarried(name)&&Bukkit.getOfflinePlayer(mm.getMate(name)).isOnline()) {
			m.em.coupleList.add(new Couple(e.getPlayer(), Bukkit.getPlayer(mm.getMate(name))));
			Logger.debug("new couple login");
		}
	}
	
	@EventHandler
	public void onPlayerLogout(PlayerQuitEvent e) {
		m.em.removePlayerFromCoupleList(e.getPlayer());
	}
	
	private class CheckTask extends TimerTask {

		private Lang lang;
		private MainManager mm;
		String n;
		
		public CheckTask(String name) {
			n = name;
			lang = Marriage.getInstance().language;
			mm = Marriage.getInstance().mainM;
		}

		@Override
		public void run() {
			
			if(Bukkit.getOfflinePlayer(n).isOnline()) {
				Bukkit.getPlayer(n).chat("/marriage check "+n);
			}
			
			if(mm.needInformDeclined(n)!=null) {
				Logger.message(n, lang.youAreDeclined.replaceFirst("%player%", mm.needInformDeclined(n)));
				mm.informDeclined(n);
			}
			
			if(mm.needInformDivorce(n)!=null) {
				Logger.message(n, lang.youAreDivorced.replaceFirst("%player%", mm.needInformDivorce(n)));
				mm.informDivorced(n);
			}
			
		}

	}
}
