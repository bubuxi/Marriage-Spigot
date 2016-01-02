package net.bubuxi.mc.marriage.commands;

import java.util.ArrayList;

import net.bubuxi.mc.marriage.Lang;
import net.bubuxi.mc.marriage.Logger;
import net.bubuxi.mc.marriage.Marriage;
import net.bubuxi.mc.marriage.datatype.Couple;
import net.bubuxi.mc.marriage.manager.MainManager;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MarriageCommand implements CommandExecutor {

	MainManager mm=null;
	Lang lang = null;
	private double marriageCost = 0;
	static Economy econ = null;
	public MarriageCommand() {
		mm=Marriage.getInstance().mainM;
		lang = Marriage.getInstance().language;
		marriageCost = Marriage.getInstance().getConfig().getDouble("marriageCost");
		Marriage.getInstance();
		econ = Marriage.econ;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lable,
			String[] args) {
		if(cmd.getName().equalsIgnoreCase("marriage")) {
			if(args.length==2&&args[0].equalsIgnoreCase("propose")) {
				if(sender instanceof Player) {
					if(!mm.isMarried(sender.getName())) {
						if(mm.getChasing(sender.getName())==null) {
							if(!mm.isMarried(args[1])) {
								if(mm.matchGender(sender.getName(), args[1])) {
									mm.propose(sender.getName(), args[1]);
									Logger.message(sender, lang.proposeSuccess);
									Logger.message(args[1], lang.youAreProposed);
									return true;
								}
								Logger.message(sender, lang.genderNotMatch);
								return false;
							}
							Logger.message(sender, lang.otherIsMarried);
							return false;
						}
						Logger.message(sender, lang.alreadyChasingSomeone);
						return false;
					}
					Logger.message(sender, lang.youAreMarried);
					return false;
				}
				Logger.message(sender, lang.mustBePlayer);
				return false;
			}
			else if(args.length==2&&args[0].equalsIgnoreCase("accept")) {
				if(sender instanceof Player) {
					if(mm.getChasing(args[1])!=null&&mm.getChasing(args[1]).equalsIgnoreCase(sender.getName())) {
						if(!mm.isMarried(sender.getName())) {
							if(!mm.isMarried(args[1])) {
								if(econ.getBalance(args[1])>=marriageCost) {
									econ.withdrawPlayer(args[1], marriageCost);
									mm.accept(args[1], sender.getName());
									Logger.message(sender, lang.acceptProposalSuccess);
									Logger.broadcast(lang.marriageBroadcast, args[1], sender.getName());
									if(Bukkit.getOfflinePlayer(args[1]).isOnline()) {
										Logger.message(args[1], lang.youAreAccepted);
										//add to EffectManager
										Marriage.getInstance().em.coupleList.add(new Couple((Player)sender, 
												Bukkit.getPlayer(args[1])));
									}
									
									return true;
								}
								Logger.message(sender, lang.otherNotEnoughMoney);
								return false;
							}
							Logger.message(sender, lang.otherIsMarried);
							return false;
						}
						Logger.message(sender, lang.youAreMarried);
						return false;
					}
					Logger.message(sender, lang.notChasingYou);
					return false;
				}
				Logger.message(sender, lang.mustBePlayer);
				return false;
			}
			else if(args.length==1&&args[0].equalsIgnoreCase("chasers")) {
				if(sender instanceof Player) {
					ArrayList<String> chasers = mm.getChasers(sender.getName());
					sender.sendMessage(chasers.toString());
					return true;
				}
				Logger.message(sender, lang.mustBePlayer);
				return false;
			}
			else if(args.length==2&&args[0].equalsIgnoreCase("decline")) {
				if(sender instanceof Player) {
					if(mm.getChasing(args[1])!=null&&mm.getChasing(args[1]).equalsIgnoreCase(sender.getName())){
						mm.decline(args[1], sender.getName());
						Logger.message(sender, lang.declineProposalSuccess);
						if(Bukkit.getOfflinePlayer(args[1]).isOnline()) {
							Logger.message(args[1], lang.youAreDeclined.replaceFirst("%player%", 
									sender.getName()));
							mm.informDeclined(args[1]);
						}
						return true;
					}
					Logger.message(sender, lang.notChasingYou);
					return true;
				}
				Logger.message(sender, lang.mustBePlayer);
				return false;
			}
			else if(args.length==1&&args[0].equalsIgnoreCase("divorce")) {
				if(sender instanceof Player) {
					if(mm.isMarried(sender.getName())) {
						String mateName = mm.getMate(sender.getName());
						mm.divorce(sender.getName());
						Logger.message(sender, lang.divorceSuccess);
						Logger.broadcast(lang.divorceBroadcast, sender.getName(), mateName);
						if(Bukkit.getOfflinePlayer(mateName).isOnline()) {
							Logger.message(mateName, lang.youAreDivorced.replaceFirst("%player%", 
									sender.getName()));
							mm.informDivorced(mateName);
							//check Effect Manager
							Marriage.getInstance().em.removePlayerFromCoupleList(Bukkit.getPlayer(mateName));
						}
						return true;
					}
					Logger.message(sender, lang.youAreNotMarried);
					return false;
				}
				Logger.message(sender, lang.mustBePlayer);
				return false;
			}
			else if(args.length>=1&&args[0].equalsIgnoreCase("check")) {
				String name;
				name = (args.length==1)?sender.getName(): args[1]; 
				boolean isMarried = mm.isMarried(name);
				String s1 = isMarried ? "�ѻ�": "δ��";
				String s2 = mm.getMate(name);
				String s3 = mm.getChasing(name);
				String s4 = mm.getChasers(name).toString();
				String s5 = null;
				s2 = (s2==null)?"��":s2;
				s3 = (s3==null)?"��":s3;
				switch(mm.getGender(name)) {
				case FEMALE:
					s5="����";
					break;
				case MALE:
					s5="����";
					break;
				case NONE:
					s5= "��";
					break;
				default:
					s5= "��";
					break;
				}
				sender.sendMessage("========����=========");
				sender.sendMessage("����״���� "+ s1);
				sender.sendMessage("���£� "+s2);
				sender.sendMessage("��׷�� "+s3);
				sender.sendMessage("׷���ߣ� "+s4);
				sender.sendMessage("�Ա�: "+s5);
				return true;
			}
			else if(args.length==1&&args[0].equalsIgnoreCase("drop")) {
				if(sender instanceof Player) {
					if(mm.getChasing(sender.getName())!=null) {
						mm.dropPropose(sender.getName());
						Logger.message(sender, lang.dropProposalSuccess);
					}
					Logger.message(sender, lang.notChasingYou);
					return false;
				}
				Logger.message(sender, lang.mustBePlayer);
				return false;
			}
			else if(args.length==1&&args[0].equalsIgnoreCase("sethome")) {
				if(sender instanceof Player) {
					Player p = (Player)sender;
					if(mm.isMarried(sender.getName())) {
						mm.setHome(sender.getName(), p.getLocation());
						Logger.message(sender, lang.setHomeSuccess);
						return true;
					}
					Logger.message(sender, lang.youAreNotMarried);
					return false;
				}
				Logger.message(sender, lang.mustBePlayer);
				return false;
			}
			else if(args.length==1&&args[0].equalsIgnoreCase("home")) {
				if(sender instanceof Player) {
					if(mm.isMarried(sender.getName())) {
						Location loc = mm.getHome(sender.getName());
						if(loc!=null) {
							((Player)sender).teleport(loc);
							Logger.message(sender, lang.tpHomeSuccess);
							return true;
						}
						Logger.message(sender, lang.youDontHaveHome);
						return false;
					}
					Logger.message(sender, lang.youAreNotMarried);
					return false;
				}
				Logger.message(sender, lang.mustBePlayer);
				return false;
			}
			else {
				String [] marriageHelp = new String [11];
				marriageHelp[0]="��c��n=============�����==============";
				marriageHelp[1]="��e/marriage propose [���] ����ұ��";
				marriageHelp[2]="��e       ��׳ɹ�����֧��"+marriageCost+"���";
				marriageHelp[3]="��e/marriage accept [���]  ������ұ�ײ����";
				marriageHelp[4]="��e/marriage chasers        ��ѯ�Լ���׷Ѱ��";
				marriageHelp[5]="��e/marriage decline [���] �ܾ���ұ��";
				marriageHelp[6]="��e/marriage divorce        ���";
				marriageHelp[7]="��e/marriage check [���]   ��ѯ��һ���״̬";
				marriageHelp[8]="��e/marriage sethome        ����˫���ļ�";
				marriageHelp[9]="��e/marriage home           ǰ��˫���ļ�";
				marriageHelp[10]="��e/gender                 ѡ���Ա�";
				sender.sendMessage(marriageHelp);
			}
		}
		return false;
	}



}
