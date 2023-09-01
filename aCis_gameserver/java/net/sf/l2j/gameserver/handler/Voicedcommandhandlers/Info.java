package net.sf.l2j.gameserver.handler.Voicedcommandhandlers;

import java.util.List;

import net.sf.l2j.commons.lang.StringUtil;
import net.sf.l2j.commons.math.MathUtil;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.data.xml.ItemData;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.DropData;
import net.sf.l2j.gameserver.model.item.kind.Item;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

public class Info implements IVoicedCommandHandler, IAdminCommandHandler
{
    private static final String[] _voicedCommands = {"drop","spoil"};
	private static final int PAGE_LIMIT = 7;

    @Override
    public boolean useVoicedCommand(String command, Player player, String target)
	{
		
		final WorldObject targetWorldObject = getTarget(WorldObject.class, player, true);
		
		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile("data/html/admin/npcinfo/defaultDropInfo.htm");

		if (targetWorldObject instanceof Npc)
		{
			final Npc targetNpc = (Npc) targetWorldObject;
			
			sendDropInfos(targetNpc, html, 1, command.equalsIgnoreCase("drop"));
			
			player.sendPacket(html);
		}		
		return true;
	}
    
    @Override
    public String[] getVoicedCommandList()
    {
        return _voicedCommands;
    }
	public static void sendDropInfos(Npc npc, NpcHtmlMessage html, int page, boolean isDrop)
	{

		List<DropData> list = (isDrop) ? npc.getTemplate().getAllDropData() : npc.getTemplate().getAllSpoilData();
		Integer npcId = npc.getNpcId();
		
		// Load static Htm.
		html.setFile("data/html/admin/npcinfo/defaultDropInfo.htm");
		if (list.isEmpty())
		{
			html.replace("%content%", "This NPC has no " + ((isDrop) ? "drops" : "spoils") + ".");
			return;
		}

		final int max = MathUtil.countPagesNumber(list.size(), PAGE_LIMIT);
		if (page > max)
			page = max;
		
		list = list.subList((page - 1) * PAGE_LIMIT, Math.min(page * PAGE_LIMIT, list.size()));

		final StringBuilder sb = new StringBuilder(2000);
		
		int row = 0;
		for (DropData drop : list)
		{
			sb.append(((row % 2) == 0 ? "<table width=\"280\" bgcolor=\"000000\"><tr>" : "<table width=\"280\"><tr>"));
			
			final double chance = Math.min(100, (((drop.getItemId() == 57) ? drop.getChance() * Config.RATE_DROP_ADENA : drop.getChance() * Config.RATE_DROP_ITEMS) / 10000));
			final Item item = ItemData.getInstance().getTemplate(drop.getItemId());
			String name = item.getName();
			if (name.startsWith("Recipe: "))
				name = "R: " + name.substring(8);
			
			if (name.length() >= 45)
				name = name.substring(0, 42) + "...";
			
			StringUtil.append(sb, "<td width=34 height=34><img src=" + item.get_icon() + " width=32 height=32></td>");
			StringUtil.append(sb, "<td width=246 height=34>", name, "<br1><font color=B09878>", ((isDrop) ? "Drop" : "Spoil"), ": ", chance, "% Min: ", drop.getMinDrop(), " Max: ", drop.getMaxDrop(), "</font></td>");
			
			sb.append("</tr></table><img src=\"L2UI.SquareGray\" width=277 height=1>");
			row++;
		}
		
		// Build page footer.
		sb.append("<br><img src=\"L2UI.SquareGray\" width=277 height=1><table width=\"100%\" bgcolor=000000><tr>");
		
		if (page > 1) 
			StringUtil.append(sb,"<td align=right width=70><a action=\"bypass droplist ", npcId ," ", page - 1," ", ((isDrop) ? "drop" : "spoil"),"\">Previous</a></td>");		
		else
			StringUtil.append(sb, "<td align=left width=70>Previous</td>");

		StringUtil.append(sb, "<td align=center width=100>Page ", page, "</td>");
		
		if (page < max)
			StringUtil.append(sb,"<td align=right width=70><a action=\"bypass droplist ", npcId ," ", page + 1," ", ((isDrop) ? "drop" : "spoil"),"\">Next</a></td>");
		else
			StringUtil.append(sb, "<td align=right width=70>Next</td>");
		sb.append("</tr></table><img src=\"L2UI.SquareGray\" width=277 height=1>");
		
		//sb.append("</tr></table><img src=\"L2UI.SquareGray\" width=277 height=1>");

		html.replace("%content%", sb.toString());
		
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.handler.IAdminCommandHandler#useAdminCommand(java.lang.String, net.sf.l2j.gameserver.model.actor.Player)
	 */
	@Override
	public void useAdminCommand(String command, Player player)
	{
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see net.sf.l2j.gameserver.handler.IAdminCommandHandler#getAdminCommandList()
	 */
	@Override
	public String[] getAdminCommandList()
	{
		// TODO Auto-generated method stub
		return null;
	}
}