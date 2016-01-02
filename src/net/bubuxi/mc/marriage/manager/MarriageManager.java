package net.bubuxi.mc.marriage.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import net.bubuxi.mc.marriage.database.SQLUtil;

public class MarriageManager extends SQLDataManager {

	private HashMap<String, String> marriageData = new HashMap<String, String>();
	//latter broke former's marriage
	private HashMap<String, String> messageData = new HashMap<String, String>();
	private HashMap<String, String> toAdd = new HashMap<String, String>();
	private ArrayList<String> toBreak = new ArrayList<String>();
	private ArrayList<String> toRemove = new ArrayList<String>();
	
	
	public MarriageManager() {
		loadFromSql();
	}
	
	@Override
	void loadFromSql(){
		marriageData = SQLUtil.getMarriages();
		messageData = SQLUtil.getMessages();
	}
	
	@Override
	void uploadToSql(){
		//sequence is essential to be this way
		for(Entry<String, String> e: toAdd.entrySet()) {
			SQLUtil.insertMarriage(e.getKey(), e.getValue());
		}
		toAdd.clear();
		for(String name : toBreak) {
			SQLUtil.breakMarriage(name);
		}
		toBreak.clear();
		for(String name : toRemove) {
			SQLUtil.toRemoveMarriage(name);
		}
		toRemove.clear();
	}
	
	void addMarriage(String name1, String name2) {
		marriageData.put(name1, name2);
		marriageData.put(name2, name1);
		toAdd.put(name1, name2);
	}
	
	boolean isMarried(String name1, String name2) {
		return marriageData.get(name1).equals(name2);
	}
	
	boolean isMarried(String name) {
		return marriageData.containsKey(name);
	}
	
	String getMate(String name) {
		if(marriageData.containsKey(name)) {
			return marriageData.get(name);
		}
		return null;
	}
	
	void breakMarriage(String name) {
		toBreak.add(name);
		String name2 = marriageData.get(name);
		marriageData.remove(name2);
		marriageData.remove(name);
		messageData.put(name2, name);
	}
	
	void removeMarriage(String name) {
		messageData.remove(name);
		toRemove.add(name);
	}

	public String needInformDivorce(String name) {
		if(messageData.containsKey(name)) {
			return messageData.get(name);
		}
		return null;
	}

	public void informDivoced(String name) {
		toRemove.add(name);
	}
	
}
