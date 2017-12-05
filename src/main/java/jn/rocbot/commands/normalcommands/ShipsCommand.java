package jn.rocbot.commands.normalcommands;

import jn.rocbot.commands.common.*;
import jn.rocbot.commands.normalcommands.withsubcommand.WithSubCommand;
import jn.rocbot.ships.Ship;
import jn.rocbot.info.stores.ShipStore;
import jn.rocbot.utils.Search;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.Random;
import java.util.StringJoiner;

public class ShipsCommand implements Command {
    private RandomShipsSub randomShipsSub = new RandomShipsSub();
    private InfoSub infoSub = new InfoSub();
    private WithSubCommand withSub = new WithSubCommand(WithSubCommand.CALLER.Ships);

    private String HELP =
            "Usage: !ships <SomeCommand>" +
                    "\nCommands: " +
                    "\nRandom:\n" + randomShipsSub.help() +
                    "\nInfo:\n" + infoSub.help() +
                    "\nWith:\n" + withSub.help();

    private final Random r = new Random();

    private CommandConfig config = new CommandConfig(CommandType.NORMAL, true);

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if(args.length > 0) {
            if (randomShipsSub.isInvoke(args[0])){
                if(args.length > 1) {
                    if(isInteger(args[1])) {
                        randomShipsSub.rShipList(Integer.valueOf(args[1]), event);
                    }
                } else
                    randomShipsSub.sendRandomShip(event);
            } else if(infoSub.isInvoke(args[0])){
                if(args.length == 2) {
                    if (randomShipsSub.isInvoke(args[1]))
                        infoSub.sendInfo(randomShipsSub.get(), event);
                    else
                        try {
                        infoSub.sendInfo(Search.findShip(args[1]), event);
                    } catch (ShipStore.ShipNotFoundException e) {
                        sendMessage(String.format("Found no ship named %s!", args[1]), event);
                    }
                }else if (args.length > 2){
                    StringJoiner shipName = new StringJoiner(" ");
                    for(int i = 1; i < args.length; i++){
                        shipName.add(args[i]);
                    } try {
                        infoSub.sendInfo(Search.findShip(shipName.toString()), event);
                    } catch (ShipStore.ShipNotFoundException e) {
                        sendMessage(String.format("Found no ship named %s!", args[1]), event);
                    }
                }
            } else if(withSub.isInvoke(args[0]) && args.length > 1){
                withSub.getWith(withSub.parseFilter(Arrays.copyOfRange(args, 1, args.length)), event);
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
        int newShip = r.nextInt(ShipStore.SHIPS.size());
        if(list.contains(ShipStore.SHIPS.get(newShip).name))
            return getShipNotTaken(list);
        else
            return newShip;
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
            return  "\t!ships random  –  gives a random ship\n" +
                    "\t!ships random 20  –  gives a list of 20 random ships";
        }

        void rShipList(int amount, MessageReceivedEvent event) {
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

        void sendRandomShip(MessageReceivedEvent event){
            Ship ship = get();
            event.getTextChannel().sendMessage(
                    "**" + ship.name + "** " + ship.rarity.toEmoji()
            ).complete();
        }

        public Ship get(){
            return ShipStore.randomShip();
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
            return "\t!ships info <SomeShip>  –  displays stats about the specified ship";
        }

        void sendInfo(Ship ship, MessageReceivedEvent event) {
            event.getTextChannel().sendMessage(ship.desc()).complete();
        }

        @Override
        public CommandConfig getConfig() {
            return config;
        }
    }
}
