package jn.rocbot.commands.devcommands;

import jn.rocbot.commands.common.Command;
import jn.rocbot.commands.common.CommandConfig;
import jn.rocbot.commands.common.CommandType;
import jn.rocbot.info.ShipStore;
import jn.rocbot.info.ZenStore;
import jn.rocbot.ships.Ship;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.StringJoiner;

public class TestCommand implements Command{
    private CommandConfig config = new CommandConfig(CommandType.DEV, false);

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if(args[0].equals("isShip") && args.length > 1){
            StringJoiner ship = new StringJoiner(" ");
            for(String arg : args) ship.add(arg);
            try {
                sendMessage(ship.toString() + " isShip: " + Ship.isShip(ship.toString()), event);
            } catch (ShipStore.ShipNotFoundException e) {
                e.printStackTrace();
            }
        } else if(args[0].equals("zen") && args.length > 1){
            StringJoiner zen = new StringJoiner(" ");
            for (int i = 1; i < args.length; i++) {
                zen.add(args[i]);
            } if(ZenStore.isZen(args[1])) {
                try {
                    sendMessage(ZenStore.fromName(zen.toString()).desc, event);
                } catch (ZenStore.ZenNotFoundException e) {
                    e.printStackTrace();
                }
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
