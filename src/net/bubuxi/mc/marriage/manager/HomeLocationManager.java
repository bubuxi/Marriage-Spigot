package net.bubuxi.mc.marriage.manager;

import java.io.File;
import java.io.IOException;

import net.bubuxi.mc.marriage.Logger;
import net.bubuxi.mc.marriage.Marriage;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

public class HomeLocationManager {
	
	private YamlConfiguration yml = new YamlConfiguration();
	
	public HomeLocationManager() {
		loadFromYml();
	}
	
	public Location getHomeLocaiton(String name) {
		String locationString = yml.getString(name);
		String [] locInfos = locationString.split(",");
		if(locInfos.length>=4){
			double x = Double.valueOf(locInfos[1]);
			double y = Double.valueOf(locInfos[2]);
			double z = Double.valueOf(locInfos[3]);
			if(Bukkit.getWorld(locInfos[0])!=null) {
				return new Location(Bukkit.getWorld(locInfos[0]), x, y, z);
			}
			Logger.warning("Home location world name error");
			return null;
		}
		Logger.warning("Home location format error");
		return null;
	}
	
	public void setHomeLocation(String name, Location loc) {
		yml.set(name, loc.getWorld().getName()+","+loc.getX()+","+loc.getY()+","+loc.getZ());
	}

	private void loadFromYml() {
		File dataDir = new File(Marriage.getInstance().getDataFolder(), "data");
		if(!dataDir.isDirectory()) {
			dataDir.mkdir();
		}
		File locationFile = new File(dataDir, "home-locations.yml");
		if(!locationFile.exists()) {
			try {
				locationFile.createNewFile();
			} catch (IOException e) {
				Logger.warning("Location file failed to create");
				return;
			}
		}
		try {
			yml.load(locationFile);
		} catch (Exception e) {
			Logger.warning("Load location file error");
		}
	}
	
	void saveToYml() {
		File locationFile = new File(new File(Marriage.getInstance().getDataFolder(), "data"), 
				"home-locations.yml");
		try {
			yml.save(locationFile);
		} catch (IOException e) {
			Logger.warning("Failed to save home locations");
		}
	}

	public void removeHome(String name) {
		yml.set(name, null);
	}
	
}
