package jn.rocbot.commands.devcommands;

import jn.rocbot.commands.common.Command;
import jn.rocbot.commands.common.CommandConfig;
import jn.rocbot.commands.common.CommandType;
import jn.rocbot.info.ShipStore;
import jn.rocbot.ships.Ship;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class TestCommand implements Command{
    private CommandConfig config = new CommandConfig(CommandType.DEV, false);

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if(args[0].equals("isShip") && args.length == 2){
            try {
                sendMessage(args[1] + " isShip: " + Ship.isShip(args[1]), event);
            } catch (ShipStore.ShipNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String help() {
        return "NO HELP YET";
    }

    @Override
    public boolean executed(boolean success, MessageReceivedEvent event) {
        return true;
    }

    @Override
    public CommandConfig getConfig() {
        return config;
    }

    @Override
    public CommandType getType() {
        return config.type;
    }
}
