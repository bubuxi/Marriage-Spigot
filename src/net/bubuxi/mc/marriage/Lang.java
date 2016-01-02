package net.bubuxi.mc.marriage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.bukkit.configuration.file.YamlConfiguration;

public class Lang {
	
	public static Lang lang;
	private YamlConfiguration yml;
	public String proposeSuccess;
	public String otherIsMarried;
	public String alreadyChasingSomeone;
	public String youAreMarried;
	public String mustBePlayer;
	public String acceptProposalSuccess;
	public String notChasingYou;
	public String declineProposalSuccess;
	public String divorceSuccess;
	public String youAreNotMarried;
	public String dropProposalSuccess;
	public String genderConfirm;
	public String genderNotMatch;
	public String maleConfirmed;
	public String femaleConfirmed;
	public String alreadyHaveGender;
	public String marriageBroadcast;
	public String divorceBroadcast;
	public String otherNotEnoughMoney;
	public String youAreProposed;
	public String youAreAccepted;
	public String youAreDeclined;
	public String youAreDivorced;
	public String setHomeSuccess;
	public String tpHomeSuccess;
	public String youDontHaveHome;
	
	public Lang(String language) {
		writeDefaultFileFromJar(new File(new File(Marriage.getInstance().getDataFolder(),"languages"),
				language+".yml"), "languagefiles/" + language + ".yml");
		loadLanguage(language);
		lang = this;
	}

	private void loadStrings() {
		proposeSuccess = yml.getString("proposeSuccess");
		otherIsMarried = yml.getString("otherIsMarried");
		alreadyChasingSomeone = yml.getString("alreadyChasingSomeone");
		youAreMarried = yml.getString("youAreMarried");
		mustBePlayer = yml.getString("mustBePlayer");
		acceptProposalSuccess = yml.getString("acceptProposalSuccess");
		notChasingYou = yml.getString("notChasingYou");
		declineProposalSuccess = yml.getString("declineProposalSuccess");
		divorceSuccess = yml.getString("divorceSuccess");
		youAreNotMarried = yml.getString("youAreNotMarried");
		dropProposalSuccess = yml.getString("dropProposalSuccess");
		genderConfirm = yml.getString("genderConfirm");
		genderNotMatch = yml.getString("genderNotMatch");
		maleConfirmed = yml.getString("maleConfirmed");
		femaleConfirmed = yml.getString("femaleConfirmed");
		alreadyHaveGender = yml.getString("alreadyHaveGender");
		marriageBroadcast = yml.getString("marriageBroadcast");
		divorceBroadcast = yml.getString("divorceBroadcast");
		otherNotEnoughMoney = yml.getString("otherNotEnoughMoney");
		youAreProposed = yml.getString("youAreProposed");
		youAreAccepted = yml.getString("youAreAccepted");
		youAreDeclined = yml.getString("youAreDeclined");
		youAreDivorced = yml.getString("youAreDivorced");
		setHomeSuccess = yml.getString("setHomeSuccess");
		tpHomeSuccess = yml.getString("tpHomeSuccess");
		youDontHaveHome = yml.getString("youDontHaveHome");
		
	}
	
	private void loadLanguage(String language) {
		File languageDir = new File(Marriage.getInstance().getDataFolder(), "languages");
		File languageFile = new File(languageDir, language+".yml");
		yml = new YamlConfiguration();
		try {
			yml.load(languageFile);
		} catch (Exception e) {
			System.err.println("Error reading language file "+language+".yml");
		}
		loadStrings();
	}
	
	private boolean writeDefaultFileFromJar(File writeName, String jarPath) {
        try {
    		File languageDir = new File(Marriage.getInstance().getDataFolder(), "languages");
    		if(!languageDir.exists()) {
    			languageDir.mkdir();
    		}
            File jarloc = new File(getClass().getProtectionDomain().getCodeSource().
            		getLocation().toURI()).getCanonicalFile();
            if (jarloc.isFile()) {
                JarFile jar = new JarFile(jarloc);
                JarEntry entry = jar.getJarEntry(jarPath);
                if (entry != null && !entry.isDirectory()) {
                    InputStream in = jar.getInputStream(entry);
                    InputStreamReader isr = new InputStreamReader(in, "GB2312");
                    if (writeName.isFile()) {
                    	return false;
                    }
                    else {
                    	writeName.createNewFile();
                    }
                    FileOutputStream out = new FileOutputStream(writeName);
                    OutputStreamWriter osw = new OutputStreamWriter(out, "GB2312");
                    char[] tempbytes = new char[512];
                    int readbytes = isr.read(tempbytes, 0, 512);
                    while (readbytes > -1) {
                        osw.write(tempbytes, 0, readbytes);
                        readbytes = isr.read(tempbytes, 0, 512);
                    }
                    osw.close();
                    isr.close();
                    return true;
                }
                jar.close();
            }
            return false;
        } catch (Exception ex) {
            System.out.println("[Marriage] Failed to write file: " + writeName);
            ex.printStackTrace();
            return false;
        }
    }
	
}
