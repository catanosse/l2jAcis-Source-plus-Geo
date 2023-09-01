package net.sf.l2j.gameserver.handler.Voicedcommandhandlers;

import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.model.actor.Player;

public class BlockXP implements IVoicedCommandHandler
{
    private static final String[] _voicedCommands = {"xpon","xpoff","xp"};
    
    @Override
    public boolean useVoicedCommand(String command, Player activeChar, String target)
    {
        if (command.contains("xpoff"))    
        {
            activeChar.setxpOff(true);
            activeChar.sendMessage("You will not earn XP/SP - XPOFF");
        }
        else if (command.contains("xpon")){
        	activeChar.setxpOff(false);
            activeChar.sendMessage("You will earn XP/SP - XPON");
        }
        else {
        	activeChar.setxpOff(!activeChar.isxpOff());
            activeChar.sendMessage("Você alterou o status do xp, agora ele esta: " +!activeChar.isxpOff());
        }
        return true;
    }
    
    @Override
    public String[] getVoicedCommandList()
    {
        return _voicedCommands;
    }
    
}