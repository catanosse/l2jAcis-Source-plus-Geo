package net.sf.l2j.gameserver.handler.Voicedcommandhandlers;

import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Player;

public class Online implements IVoicedCommandHandler
{
    private static final String[] _voicedCommands = {"online"};
    
    @Override
    public boolean useVoicedCommand(String command, Player activeChar, String target)
    {
        if (command.equals("online"))    
        {
            activeChar.sendMessage("====[Online Players]====");
            activeChar.sendMessage("Player(s): " + World.getInstance().getPlayers().size() + " Online.");        
            activeChar.sendMessage("======[L2 Foda]======");
        }
        return true;
    }
    
    @Override
    public String[] getVoicedCommandList()
    {
        return _voicedCommands;
    }
    
}