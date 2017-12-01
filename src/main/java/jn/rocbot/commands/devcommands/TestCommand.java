package jn.rocbot.commands.devcommands;

import jn.rocbot.commands.common.*;
import jn.rocbot.ships.Ship;
import jn.rocbot.utils.Search;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.StringJoiner;

public class TestCommand implements Command{
    private CommandConfig config = new CommandConfig(CommandType.DEV, false);

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return true;
    }

    private IsShipTest isShipTest = new IsShipTest();
    private SearchTestSub searchTestSub = new SearchTestSub();

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if(isShipTest.isInvoke(args[0]) && args.length > 1){
           isShipTest.sendResult(args, event);
        }else if(searchTestSub.isInvoke(args[0])){
            searchTestSub.sendResult(args[0], event);
        }
    }

    //<editor-fold desc="Boring stuff">
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
    //</editor-fold>

    private class SearchTestSub extends BasicTestSubCommand{

        public void sendResult(String search, MessageReceivedEvent event){
            sendMessage(new Search().testShipSearch(search), event);
        }

        @Override
        public String invoke() {
            return "search";
        }
    }
    private class IsShipTest extends BasicTestSubCommand{
        public void sendResult(String[] args, MessageReceivedEvent event){
            if(!args[0].equals("simple")) {
                StringJoiner ship = new StringJoiner(" ");
                for (String arg : args) if (!args.equals("extra")) ship.add(arg);
                sendMessage(ship.toString() + " isShip: " + Ship.isShip(ship.toString()), event);
            }else{

            }
        }
        public IsShipTest(){
            super(false);
        }
        @Override
        public String invoke() {
            return "isShip";
        }
    }
}
