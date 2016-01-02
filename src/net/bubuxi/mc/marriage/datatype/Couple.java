package net.bubuxi.mc.marriage.datatype;

import org.bukkit.entity.Player;

public class Couple {
	
	public Player player1, player2;
	
	public Couple(Player p1, Player p2) {
		player1 = p1;
		player2 = p2;
	}
	
	public String toString() {
		return player1.getName()+" "+player2.getName();
	}
}
