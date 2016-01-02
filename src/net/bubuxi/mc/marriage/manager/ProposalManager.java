package net.bubuxi.mc.marriage.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import net.bubuxi.mc.marriage.database.SQLUtil;

public class ProposalManager extends SQLDataManager {

	private HashMap<String, String> proposalData = new HashMap<String, String>();
	private HashMap<String, String> informData = new HashMap<String, String>();
	private HashMap<String, String> toAdd = new HashMap<String, String>();
	private HashMap<String, String> toDecline = new HashMap<String, String>();
	private HashMap<String, String> toRemove = new HashMap<String, String>();
	
	@Override
	void loadFromSql() {
		proposalData = SQLUtil.getProposals();
		informData = SQLUtil.getInform();
	}

	@Override
	void uploadToSql() {
		for(Entry<String, String> e : toAdd.entrySet()) {
			SQLUtil.insertProposal(e.getKey(), e.getValue());
		}
		toAdd.clear();
		for(Entry<String, String> e : toDecline.entrySet()) {
			SQLUtil.declineProposal(e.getKey(), e.getValue());
		}
		toDecline.clear();
		for(Entry<String, String> e : toRemove.entrySet()) {
			SQLUtil.toRemoveProposal(e.getKey(), e.getValue());
		}
		toRemove.clear();
	}
	
	void propose(String name1, String name2) {
		proposalData.put(name1, name2);
		toAdd.put(name1, name2);
	}
	
	void decline(String name1, String name2) {
		proposalData.remove(name1);
		informData.put(name1, name2);
		toDecline.put(name1, name2);
	}
	
	void delete(String name1, String name2) {
		proposalData.remove(name1);
		toRemove.put(name1,name2);
	}
	
	ArrayList<String> getChasers(String name) {
		ArrayList<String> list = new ArrayList<String>();
		for(Entry<String,String> e : proposalData.entrySet()) {
			if(e.getValue().equals(name)) {
				list.add(e.getKey());
			}
		}
		return list;
	}
	String getProposed(String name) {
		if(proposalData.containsKey(name)) {
			return proposalData.get(name);
		}
		return null;
	}

	String getChasing(String name) {
		if(proposalData.containsKey(name)) {
			return proposalData.get(name);
		}
		return null;
	}

	public String needInformDeclined(String name) {
		if(informData.containsKey(name)) {
			return informData.get(name);
		}
		return null;
	}
	
	public void informDeclined(String name) {
		toRemove.put(name, informData.get(name));
		informData.remove(name);
	}
}
