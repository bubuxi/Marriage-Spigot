package net.bubuxi.mc.marriage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.bubuxi.mc.marriage.commands.GenderCommand;
import net.bubuxi.mc.marriage.commands.MarriageCommand;
import net.bubuxi.mc.marriage.database.DataBase;
import net.bubuxi.mc.marriage.database.SQLUtil;
import net.bubuxi.mc.marriage.effects.EffectManager;
import net.bubuxi.mc.marriage.listeners.ChatListener;
import net.bubuxi.mc.marriage.listeners.LoginListener;
import net.bubuxi.mc.marriage.manager.MainManager;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Marriage extends JavaPlugin{

	private static Marriage instance;
	public static Connection conn;
	public MainManager mainM;
	public Lang language;
	public static Economy econ = null;
	public LoginListener loginListener;
	public EffectManager em;
	
	public void onEnable(){
		getLogger().info("���ڼ���");
		saveDefaultConfig();
		instance=this;
		conn=DataBase.getConnection();
		setupEconomy();
		checkAndCreateTables();
		mainM = new MainManager();
		language = new Lang(getConfig().getString("lang"));
		registerListeners();
		this.getCommand("marriage").setExecutor(new MarriageCommand());
		this.getCommand("gender").setExecutor(new GenderCommand());
		Logger.info("�������");
	}
	
	private void registerListeners() {
		loginListener = new LoginListener();
		Bukkit.getPluginManager().registerEvents(loginListener, this);
		Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
		em = new EffectManager();
	}
	
	public void onDisable() {
		mainM.save();
	}
	
	private void checkAndCreateTables() {
		try{
			ResultSet rs=conn.getMetaData().getTables(null, null, "MARRIAGES", null);
			if(!rs.next()){
				SQLUtil.createMarriageTable();
			}
			rs=conn.getMetaData().getTables(null, null, "PROPOSALS", null);
			if(!rs.next()){
				SQLUtil.createProposalTable();
			}
			rs=conn.getMetaData().getTables(null, null, "GENDER", null);
			if(!rs.next()){
				SQLUtil.createGenderTable();
			}
		}catch(SQLException e){
			Logger.warning("������");
			e.printStackTrace();
		}
	}

	public static Marriage getInstance() {
		return instance;
	}
	
	private boolean setupEconomy() {
	    if (getServer().getPluginManager().getPlugin("Vault") == null) {
	    	return false;
	    }
	    RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
	    if (rsp == null) {
	      return false;
	    }
	    econ = rsp.getProvider();
	    return econ != null;
	}
}
