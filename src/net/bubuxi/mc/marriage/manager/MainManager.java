package net.bubuxi.mc.marriage.manager;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Location;

import net.bubuxi.mc.marriage.enums.Gender;

public class MainManager {

	MarriageManager marriageM;
	ProposalManager proposalM;
	GenderManager genderM;
	HomeLocationManager homeLocationM;
	
	public MainManager() {
		marriageM = new MarriageManager();
		proposalM = new ProposalManager();
		genderM = new GenderManager();
		homeLocationM = new HomeLocationManager();
		Timer timer = new Timer();
		//15 mins each renew
		//timer.schedule(new UpdateRunnable(), 900000L, 900000L);
		//1 min
		timer.schedule(new UpdateRunnable(), 60000L, 60000L);
	}
	
	private class UpdateRunnable extends TimerTask {

		@Override
		public void run() {
			marriageM.uploadToSql();
			proposalM.uploadToSql();
			genderM.uploadToSql();
			marriageM.loadFromSql();
			proposalM.loadFromSql();
			genderM.loadFromSql();
		}
	}
	
	public void save() {
		marriageM.uploadToSql();
		proposalM.uploadToSql();
		genderM.uploadToSql();
		homeLocationM.saveToYml();
	}
	
	public void propose(String name1, String name2) {
		if(matchGender(name1, name2)) {
			if(proposalM.getChasing(name1)==null&&!marriageM.isMarried(name1)&&!marriageM.isMarried(name2)){
				proposalM.propose(name1, name2);
			}
		}	
	}
	
	public void accept(String name1, String name2) {
		if(matchGender(name1, name2)) {
			if(!marriageM.isMarried(name1)&&!marriageM.isMarried(name2)){
				marriageM.addMarriage(name1, name2);
				proposalM.delete(name1, name2);
			}
		}
	}
	
	public ArrayList<String> getChasers(String name) {
		return proposalM.getChasers(name);
	}
	
	public void decline(String name1, String name2) {
		if(proposalM.getChasing(name1)!=null&&proposalM.getChasers(name1).equals(name2)) {
			proposalM.decline(name1, name2);
		}
	}

	public void divorce(String name) {
		if(marriageM.getMate(name)!=null){
			homeLocationM.removeHome(name);
			homeLocationM.removeHome(getMate(name));
			marriageM.breakMarriage(name);
		}
	}

	public boolean isMarried(String name) {
		return marriageM.isMarried(name);
	}

	public String getMate(String name) {
		return marriageM.getMate(name);
	}

	public String getChasing(String name) {
		return proposalM.getChasing(name);
	}

	public Gender getGender(String name) {
		return genderM.getGender(name);
	}
	
	public void setGender(String name, Gender gender) {
		if(genderM.getGender(name)==Gender.NONE) {
			genderM.setGender(name, gender);
		}
	}
	
	public boolean matchGender(String name1, String name2) {
		return (genderM.getGender(name1)==Gender.FEMALE&&genderM.getGender(name2)==Gender.MALE)||
				(genderM.getGender(name1)==Gender.MALE&&genderM.getGender(name2)==Gender.FEMALE);
	}

	public void dropPropose(String name) {
		if(proposalM.getChasing(name)!=null) {
			proposalM.delete(name, proposalM.getChasing(name));
		}
	}

	public void informDeclined(String name) {
		proposalM.informDeclined(name);
	}
	
	public String needInformDeclined(String name) {
		return proposalM.needInformDeclined(name);
	}

	public String needInformDivorce(String name) {
		return marriageM.needInformDivorce(name);
	}

	public void informDivorced(String n) {
		marriageM.informDivoced(n);		
	}
	
	public void setHome(String name, Location loc) {
		if(isMarried(name)) {
			homeLocationM.setHomeLocation(name, loc);
			homeLocationM.setHomeLocation(getMate(name), loc);
		}
	}
	
	public Location getHome(String name) {
		if(isMarried(name)) {
			return homeLocationM.getHomeLocaiton(name);
		}
		return null;
	}
}
