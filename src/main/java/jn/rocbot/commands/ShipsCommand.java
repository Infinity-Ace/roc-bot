package jn.rocbot.commands;

import jn.rocbot.Main;
import jn.rocbot.commands.common.Command;
import jn.rocbot.commands.common.CommandConfig;
import jn.rocbot.commands.common.CommandType;
import jn.rocbot.ships.Ship;
import jn.rocbot.info.Stores.ShipStore;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Objects;
import java.util.Random;

public class ShipsCommand implements Command {
    private final static String HELP = "Usage:\n\t!ships returns a list of all the ships\n\t!ships random gives a random ship\n\tAnd !ships random 10 gives a list of 10 random ships";

    private final Random r = new Random();

    private CommandConfig config = new CommandConfig(CommandType.NORMAL, true);

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if(args.length > 0) {
            if (args[0].toLowerCase().equals("random") || (args[0].toLowerCase().equals("warp"))){
                if(args.length > 1) {
                    if(isInteger(args[1])){
                        String list = "";
                        int num = Integer.valueOf(args[1]);

                        if(num > 30) {
                            num = 30;
                            list += "Can not list more than 30 ships!\nSo here is 30:\n";
                        }

                        for(int i = 0; i < num; i++){
                            if(list.length() > 1500){
                                event.getTextChannel().sendMessage(list).complete();
                                list = "";
                            } int ship = getShipNotTaken(list);

                            list += "**"+ (i + 1) + ".** " + ShipStore.SHIPS.get(ship).rarity.toEmoji() + ShipStore.SHIPS.get(ship).name + "\n";
                        } event.getTextChannel().sendMessage(list).complete();
                    } else {
                        if (args[1].toLowerCase().equals("info"))
                            event.getTextChannel().sendMessage(ShipStore.SHIPS.get(r.nextInt(ShipStore.SHIPS.size() - 1)).desc()).complete();
                    }
                } else {
                    int ship = r.nextInt(ShipStore.SHIPS.size());
                    event.getTextChannel().sendMessage("**" + ShipStore.SHIPS.get(ship).name + "** " + ShipStore.SHIPS.get(ship).rarity.toEmoji()).complete();
                }
            } else {
                if(Ship.isShip(args[0])){
                    if(args[1].toLowerCase().equals("info")){
                        try {
                            event.getTextChannel().sendMessage(ShipStore.getShip(args[0]).desc()).complete();
                        } catch (ShipStore.ShipNotFoundException e) {
                            event.getTextChannel().sendMessage("No ship named: " + args[0]).complete();
                        }
                    }
                }
            }
        } else {
            String shiplist = "";

            for(Ship ship : ShipStore.SHIPS){
                if(!Objects.equals(ship.name, "Shogun")) shiplist += ship.name + ", ";
                else shiplist += "Shogun";
            }

            event.getTextChannel().sendMessage(shiplist).complete();
        }
    }

    public boolean isInteger(String str) {
        int size = str.length();

        for (int i = 0; i < size; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }

        return size > 0;
    }

    public int getShipNotTaken(String list){
        int newship = r.nextInt(ShipStore.SHIPS.size());
        if(list.contains(ShipStore.SHIPS.get(newship).name)) return getShipNotTaken(list);
        else return newship;
    }

    @Override
    public String help() {
        return HELP;
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

    private void dlog(String msg){
        Main.log(Main.LOGTYPE.DEBUG, msg);
    }
}
