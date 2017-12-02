package jn.rocbot.commands.normalcommands;

import jn.rocbot.commands.common.Command;
import jn.rocbot.commands.common.CommandConfig;
import jn.rocbot.commands.common.CommandType;
import jn.rocbot.ships.Aura;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.StringJoiner;

import static jn.rocbot.info.stores.AuraStore.AURAS;

public class AurasCommand implements Command {
    private CommandConfig config = new CommandConfig(CommandType.NORMAL, true);

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if(args.length == 0){
            StringJoiner allAuras = new StringJoiner(", ");
            for(Aura aura : AURAS) {
                allAuras.add(aura.name);
            } sendMessage(allAuras.toString(), event);
        }
    }

    @Override
    public String help() {
        return "Usage: !auras";
    }

    @Override
    public CommandConfig getConfig() {
        return config;
    }
}
