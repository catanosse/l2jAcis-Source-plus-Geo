package net.sf.l2j.gameserver.handler;

import java.util.HashMap;
import java.util.Map;

import net.sf.l2j.gameserver.handler.Voicedcommandhandlers.BlockXP;
import net.sf.l2j.gameserver.handler.Voicedcommandhandlers.Info;
import net.sf.l2j.gameserver.handler.Voicedcommandhandlers.Online;
import net.sf.l2j.gameserver.handler.Voicedcommandhandlers.RaidInfo;

public class VoicedCommandHandler
{
    private final Map<Integer, IVoicedCommandHandler> _datatable = new HashMap<>();
    
    public static VoicedCommandHandler getInstance()
    {
        return SingletonHolder.INSTANCE;
    }
    
    protected VoicedCommandHandler()
    {
        // coloque aqui os comandos
        registerHandler(new Online());
        registerHandler(new RaidInfo());
        registerHandler(new Info());
        registerHandler(new BlockXP());
    }
    
    public void registerHandler(IVoicedCommandHandler handler)
    {
        String[] ids = handler.getVoicedCommandList();
        
        for (int i = 0; i < ids.length; i++)        
            _datatable.put(ids[i].hashCode(), handler);
    }
        
    public IVoicedCommandHandler getHandler(String voicedCommand)
    {
        String command = voicedCommand;
        
        if (voicedCommand.indexOf(" ") != -1)        
            command = voicedCommand.substring(0, voicedCommand.indexOf(" "));        

        return _datatable.get(command.hashCode());        
    }
    
    public int size()
    {
        return _datatable.size();
    }
    
    private static class SingletonHolder
    {
        protected static final VoicedCommandHandler INSTANCE = new VoicedCommandHandler();
    }
}