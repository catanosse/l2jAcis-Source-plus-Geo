package net.sf.l2j.gameserver.handler.Voicedcommandhandlers;

import java.util.ArrayList;
import java.util.HashMap;

import net.sf.l2j.commons.data.StatSet;
import net.sf.l2j.commons.logging.CLogger;
import net.sf.l2j.commons.math.MathUtil;
import net.sf.l2j.commons.util.ArraysUtil;

import net.sf.l2j.gameserver.GameServer;
import net.sf.l2j.gameserver.data.manager.GrandBossManager;
import net.sf.l2j.gameserver.data.manager.RaidBossManager;
import net.sf.l2j.gameserver.data.xml.NpcData;
import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

public class RaidInfo implements IVoicedCommandHandler
{
	private static final Integer[] GrandBosses =
	{
		25512,
		29001,
		29006,
		29014,
		29019,
		29020,
		29022,
		29028,
		29045,
		29046,
		29047,
		29065
	};
	
	private static final Integer[] RaidBosses =
	{
		25001,
		25004,
		25007,
		25010,
		25013,
		25016,
		25019,
		25020,
		25023,
		25026,
		25029,
		25032,
		25035,
		25038,
		25041,
		25044,
		25047,
		25050,
		25051,
		25054,
		25057,
		25060,
		25063,
		25064,
		25067,
		25070,
		25073,
		25076,
		25079,
		25082,
		25085,
		25088,
		25089,
		25092,
		25095,
		25098,
		25099,
		25102,
		25103,
		25106,
		25109,
		25112,
		25115,
		25118,
		25119,
		25122,
		25125,
		25126,
		25127,
		25128,
		25131,
		25134,
		25137,
		25140,
		25143,
		25146,
		25149,
		25152,
		25155,
		25158,
		25159,
		25162,
		25163,
		25166,
		25169,
		25170,
		25173,
		25176,
		25179,
		25182,
		25185,
		25188,
		25189,
		25192,
		25198,
		25199,
		25202,
		25205,
		25208,
		25211,
		25214,
		25217,
		25220,
		25223,
		25226,
		25229,
		25230,
		25233,
		25234,
		25235,
		25238,
		25241,
		25244,
		25245,
		25248,
		25249,
		25252,
		25255,
		25256,
		25259,
		25260,
		25263,
		25266,
		25269,
		25272,
		25276,
		25277,
		25280,
		25281,
		25282,
		25290,
		25293,
		25296,
		25299,
		25302,
		25305,
		25306,
		25309,
		25312,
		25315,
		25316,
		25319,
		25322,
		25325,
		25328,
		25352,
		25354,
		25357,
		25360,
		25362,
		25365,
		25366,
		25369,
		25372,
		25373,
		25375,
		25378,
		25380,
		25383,
		25385,
		25388,
		25391,
		25392,
		25394,
		25395,
		25398,
		25401,
		25404,
		25407,
		25410,
		25412,
		25415,
		25418,
		25420,
		25423,
		25426,
		25429,
		25431,
		25434,
		25437,
		25438,
		25441,
		25444,
		25447,
		25450,
		25453,
		25456,
		25460,
		25463,
		25467,
		25470,
		25473,
		25475,
		25478,
		25481,
		25484,
		25487,
		25490,
		25493,
		25496,
		25498,
		25501,
		25504,
		25506,
		25509,
		25514,
		25523,
		25524,
		25527,
		29030,
		29033,
		29036,
		29037,
		29040,
		29095
	};
	private static final CLogger LOGGER = new CLogger(GameServer.class.getName());
	
	private static final String[] _voicedCommands =
	{
		"raidinfo"
	};
	
	@Override
	public boolean useVoicedCommand(String command, Player activeChar, String target)
	{
		if (command.equals("raidinfo"))
		{
			activeChar.sendMessage("====[Online Players]====");
			activeChar.sendMessage("Player(s): " + World.getInstance().getPlayers().size() + " Online.");
			activeChar.sendMessage("======[L2 Foda]======");
			showMainpage(activeChar);
		}
		return true;
	}
	
	public void showMainpage(Player player)
	{
		
		StringBuilder sb = new StringBuilder();
		StringBuilder sbGrandBoss = new StringBuilder();
		StringBuilder sbRaidBoss = new StringBuilder();
		
		sb.append("<html><title>Boss Spawn</title><body><center>");
		sb.append("<img src=\"L2UI_CH3.herotower_deco\" width=256 height=32><br>");
		sb.append("Epic's Boss respawn time<br>");
		sb.append("<img src=\"sek.cbui32\" width=210 height=1><br>");
		Integer[] allBosses = ArraysUtil.concat(GrandBosses, RaidBosses);
		boolean isGrandBoss;
		final int max = MathUtil.countPagesNumber(allBosses.length, 13);
		int page = 1;
		
		if (page > max)
			page = max;
		NpcTemplate raidBoss = null;
		HashMap<NpcTemplate, StringBuilder> raidList = null;
		for (int boss : allBosses)
		{
			String name = "";
			NpcTemplate template = null;
			raidBoss = NpcData.getInstance().getTemplate(boss);

			if ((raidBoss = NpcData.getInstance().getTemplate(boss)) != null)
			{
				name = raidBoss.getName();
			}
			else
			{
				LOGGER.warn("[RaidInfoHandler][sendInfo] Raid Boss with ID " + boss + " is not defined into NpcData");
				continue;
			}
			
			StatSet actual_boss_stat = null;
			long delay = 0;
			if (NpcData.getInstance().getTemplate(boss).getType().equals("GrandBoss"))
			{
				actual_boss_stat = GrandBossManager.getInstance().getStatSet(boss);
				if (actual_boss_stat != null)
					delay = actual_boss_stat.getLong("respawn_time");
				isGrandBoss = true;
			}
			else if (NpcData.getInstance().getTemplate(boss).getType().equals("RaidBoss"))
			{
				delay = RaidBossManager.getInstance().getBossSpawn(boss).getRespawnTime();
				isGrandBoss = false;
			}
			else
				continue;
			
			if (delay <= System.currentTimeMillis())
			{
				if (isGrandBoss)
				{
					sbGrandBoss.append("<font color=\"00C3FF\">" + name + "</font>: " + "<font color=\"9CC300\">Is Alive</font>" + "<br1>");
				}
				else
				{
					sbRaidBoss.append("<font color=\"00C3FF\">" + name + "</font>: " + "<font color=\"9CC300\">Is Alive</font>" + "<br1>");
					//raidList.put(raidBoss, sbRaidBoss);
				}
			}
			else
			{
				int hours = (int) ((delay - System.currentTimeMillis()) / 1000 / 60 / 60);
				int mins = (int) (((delay - (hours * 60 * 60 * 1000)) - System.currentTimeMillis()) / 1000 / 60);
				int seconts = (int) (((delay - ((hours * 60 * 60 * 1000) + (mins * 60 * 1000))) - System.currentTimeMillis()) / 1000);
				if (isGrandBoss)
					sbGrandBoss.append("<font color=\"00C3FF\">" + name + "</font>" + "<font color=\"FFFFFF\">" + " " + "Respawn in :</font>" + " " + " <font color=\"32C332\">" + hours + " : " + mins + " : " + seconts + "</font><br1>");
				else
				{
					sbRaidBoss.append("<font color=\"00C3FF\">" + name + "</font>" + "<font color=\"FFFFFF\">" + " " + "Respawn in :</font>" + " " + " <font color=\"32C332\">" + hours + " : " + mins + " : " + seconts + "</font><br1>");
				}
			}
		}
		sb.append(sbGrandBoss);
		
		//sb.append("Raid Boss");
		//sb.append(sbRaidBoss);
		sb.append("<img src=\"sek.cbui32\" width=210 height=1><br>");
		sb.append("<img src=\"L2UI_CH3.herotower_deco\" width=256 height=32>");
		sb.append("</center></body></html>");
		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setHtml(sb.toString());
		player.sendPacket(html);
	}
	
	@Override
	public String[] getVoicedCommandList()
	{
		return _voicedCommands;
	}
}
