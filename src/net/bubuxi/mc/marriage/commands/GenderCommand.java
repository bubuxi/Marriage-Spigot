package net.bubuxi.mc.marriage.commands;

import java.util.HashSet;

import net.bubuxi.mc.marriage.Lang;
import net.bubuxi.mc.marriage.Logger;
import net.bubuxi.mc.marriage.Marriage;
import net.bubuxi.mc.marriage.enums.Gender;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GenderCommand implements CommandExecutor {
	
	HashSet<String> maleConfirm = new HashSet<String>();
	HashSet<String> femaleConfirm = new HashSet<String>();
	
	private Lang lang;
	public GenderCommand() {
		lang = Marriage.getInstance().language;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lable,
			String[] args) {
		if(cmd.getName().equalsIgnoreCase("gender")) {
			if(args.length==2&&args[0].equalsIgnoreCase("choose")) {
				if(sender instanceof Player) {
					if(Marriage.getInstance().mainM.getGender(sender.getName())==Gender.NONE){
						if (args[1].equals("male")) {
							if (femaleConfirm.contains(sender.getName())) {
								femaleConfirm.remove(sender.getName());
							}
							maleConfirm.add(sender.getName());
							Logger.message(sender, lang.genderConfirm);
						}
						else if(args[1].equals("female")) {
							if (maleConfirm.contains(sender.getName())) {
								maleConfirm.remove(sender.getName());
							}
							femaleConfirm.add(sender.getName());
							Logger.message(sender, lang.genderConfirm);
						}
						return true;
					}
					Logger.message(sender, lang.alreadyHaveGender);
					return false;
				}
				Logger.message(sender, lang.mustBePlayer);
			}
			else if(args.length==1&&args[0].equalsIgnoreCase("confirm")) {
				if(maleConfirm.contains(sender.getName())&&!femaleConfirm.contains(sender.getName())) {
					Marriage.getInstance().mainM.setGender(sender.getName(), Gender.MALE);
					Logger.message(sender, lang.maleConfirmed);
					return true;
				}
				else if(!maleConfirm.contains(sender.getName())&&femaleConfirm.contains(sender.getName())) {
					Marriage.getInstance().mainM.setGender(sender.getName(), Gender.FEMALE);
					Logger.message(sender, lang.femaleConfirmed);
					return true;
				}
				else if(maleConfirm.contains(sender.getName())&&femaleConfirm.contains(sender.getName())) {
					maleConfirm.remove(sender.getName());
					femaleConfirm.remove(sender.getName());
					return true;
				}
				else {
					
				}
			}
			else if(args.length==2&&args[0].equalsIgnoreCase("check")) {
				sender.sendMessage(args[1]+" :"+Marriage.getInstance().mainM.getGender(args[1]).toString());
				return true;
			}
			else {
				sender.sendMessage("/gender choose male/female (����Ϊmale,����Ϊfemale)");
				return true;
			}
		}
		return false;
	}

}
