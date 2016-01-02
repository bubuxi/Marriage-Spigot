package net.bubuxi.mc.marriage.manager;

import java.util.HashMap;
import java.util.Map.Entry;

import net.bubuxi.mc.marriage.database.SQLUtil;
import net.bubuxi.mc.marriage.enums.Gender;

public class GenderManager extends SQLDataManager {

	private HashMap<String, Gender> genderData = new HashMap<String, Gender>();
	private HashMap<String, Gender> toAdd = new HashMap<String, Gender>();
	
	public GenderManager() {
		loadFromSql();
	}
	
	Gender getGender(String name) {
		if(genderData.get(name)!=null) {
			return genderData.get(name);
		}
		return Gender.NONE;
	}

	public void setGender(String name, Gender gender) {
		genderData.put(name, gender);
		toAdd.put(name, gender);
	}
	
	@Override
	void loadFromSql() {
		genderData=SQLUtil.getGender();
	}

	@Override
	void uploadToSql() {
		for(Entry<String, Gender> e : toAdd.entrySet()) {
			SQLUtil.insertGender(e.getKey(), e.getValue());
		}
		toAdd.clear();
	}


}
