package jn.rocbot.commands;

import jn.rocbot.commands.common.Command;
import jn.rocbot.commands.common.CommandConfig;
import jn.rocbot.commands.common.CommandType;
import jn.rocbot.commands.common.SubCommand;
import jn.rocbot.ships.Ship;
import jn.rocbot.info.Stores.ShipStore;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Random;
import java.util.StringJoiner;

public class ShipsCommand implements Command {
    private final static String HELP =
            "Blame jens";

    private final Random r = new Random();

    private CommandConfig config = new CommandConfig(CommandType.NORMAL, true);

    private RandomShipsSub rShip = new RandomShipsSub();
    private InfoSub info = new InfoSub();

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if(args.length > 0) {
            if (rShip.isInvoke(args[0])){
                if(args.length > 1) {
                    if(isInteger(args[1])){
                        rShip.rShipList(Integer.valueOf(args[1]), event);
                    } else {
                        if (info.isInvoke(args[1]))
                            info.sendInfo(rShip.get(), event);
                    }
                } else
                    rShip.sendRandomShip(event);
            } else {
                if(args[0].toLowerCase().equals("info")){
                    if(Ship.isShip(args[1])){
                        try {
                            info.sendInfo(ShipStore.getShip(args[1]), event);
                        } catch (ShipStore.ShipNotFoundException e) { }
                    }
                }
            }
        } else {
            sendAllShips(event);
        }
    }

    private void sendAllShips(MessageReceivedEvent event){
        StringJoiner shipList = new StringJoiner(", ");
        for(Ship ship : ShipStore.SHIPS){
            shipList.add(ship.name);
        } event.getTextChannel().sendMessage(shipList.toString()).complete();
    }

    private boolean isInteger(String str) {
        int size = str.length();
        for (int i = 0; i < size; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        } return size > 0;
    }

    private int getShipNotTaken(String list){
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



    private class RandomShipsSub implements SubCommand {
        private final Random r = new Random();
        private CommandConfig config = new CommandConfig(CommandType.NORMAL, true);

        @Override
        public String invoke() {
            return "random";
        }

        @Override
        public String help() {
            return  "random  –  gives a random ship\n" +
                    "random 20  –  gives a list of 20 random ships";
        }

        public void rShipList(int amount, MessageReceivedEvent event) {
            String list = "";

            if(amount > 30) {
                amount = 30;
                list += "Can not list more than 30 ships!\nSo here is 30:\n";
            } for(int i = 0; i < amount; i++){
                if(list.length() > 1500){
                    event.getTextChannel().sendMessage(list).complete();
                    list = "";
                } int ship = getShipNotTaken(list);

                list += "**"+ (i + 1) + ".** " + ShipStore.SHIPS.get(ship).rarity.toEmoji() + ShipStore.SHIPS.get(ship).name + "\n";
            } event.getTextChannel().sendMessage(list).complete();
        }

        public void sendRandomShip(MessageReceivedEvent event){
            Ship ship = get();
            event.getTextChannel().sendMessage(
                    "**" + ship.name + "** " + ship.rarity.toEmoji()
            ).complete();
        }

        public Ship get(){
            return ShipStore.SHIPS.get(ShipStore.SHIPS.size() - 1);
        }

        public Ship get(String notTakenFrom){
            Ship newShip = ShipStore.SHIPS.get(r.nextInt(ShipStore.SHIPS.size()));
            try {
                if(notTakenFrom.contains(
                        ShipStore.getShip(newShip.name.toLowerCase()).name))
                    return get(notTakenFrom);
                else
                    return newShip;
            } catch (ShipStore.ShipNotFoundException e) {
                e.printStackTrace();
                return get(notTakenFrom);
            }
        }


        @Override
        public CommandConfig getConfig() {
            return config;
        }
    }

    private class InfoSub implements SubCommand {
        private CommandConfig config = new CommandConfig(CommandType.NORMAL, true);

        @Override
        public String invoke() {
            return "info";
        }

        @Override
        public String help() {
            return "(<SOME_SHIP>) info  –  displays stats about whatever ship *SOME_SHIP* is replaced with";
        }

        public void sendInfo(Ship ship, MessageReceivedEvent event) {
            event.getTextChannel().sendMessage(ship.desc()).complete();
        }

        @Override
        public CommandConfig getConfig() {
            return null;
        }
    }
}
