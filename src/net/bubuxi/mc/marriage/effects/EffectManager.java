package net.bubuxi.mc.marriage.effects;


import java.util.HashSet;

import net.bubuxi.mc.marriage.Logger;
import net.bubuxi.mc.marriage.Marriage;
import net.bubuxi.mc.marriage.datatype.Couple;
import net.bubuxi.mc.marriage.manager.MainManager;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class EffectManager {
	
	public HashSet<Couple> coupleList = new HashSet<Couple>();
	private HashSet<Player> effectList = new HashSet<Player>();
	private MainManager mm;
	
	public EffectManager() {
		HashSet<String> usedName = new HashSet<String>();
		mm = Marriage.getInstance().mainM;
		for(Player player: Bukkit.getOnlinePlayers()) {
			String name = player.getName();
			if(usedName.contains(name)) {
				continue;
			}
			if(mm.isMarried(name)) {
				String mateName = mm.getMate(name);
				if(Bukkit.getOfflinePlayer(mateName).isOnline()) {
					coupleList.add(new Couple(player, Bukkit.getPlayer(mateName)));
					usedName.add(mateName);
				}
			}
		}
		
		Bukkit.getScheduler().runTaskTimer(Marriage.getInstance(), new Runnable() {

			@Override	
			public void run() {
				effectList.clear();
				for(Couple c : coupleList) {
					Logger.debug(c.toString());
					if(c.player1.getWorld().equals(c.player2.getWorld())) {
						if(c.player1.getLocation().distance(c.player2.getLocation())<15) {
							effectList.add(c.player1);
							effectList.add(c.player2);
						}
					}
				}
			}
		}, 10, 100);
		
		Bukkit.getScheduler().runTaskTimer(Marriage.getInstance(), new Runnable() {

			@Override
			public void run() {
				for(Player p : effectList) {
					p.getWorld().playEffect(p.getLocation().add(new Location(p.getWorld(),0,2,0)), Effect.HEART, 10, 100);
				}
			}
		}, 10, 10);
	}

	public void removePlayerFromCoupleList(Player player) {
		for(Couple c : coupleList) {
			if(c.player1.equals(player)) {
				coupleList.remove(c);
			}
			else if(c.player2.equals(player)) {
				coupleList.remove(c);
			}
		}
	}
	
	
	
}
