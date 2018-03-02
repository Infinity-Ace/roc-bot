package jn.rocbot.commands.devcommands.tests;

import jn.rocbot.commands.common.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;

public class TestCommand implements Command {
    private CommandConfig config = new CommandConfig(CommandType.DEV, false);

    private static ArrayList<TestSubCommand> TEST_COMMANDS;

    public TestCommand() {
        TEST_COMMANDS = new ArrayList<>();

        TEST_COMMANDS.add(new IsShipTestSub(this));
        TEST_COMMANDS.add(new SearchTestSub(this));
        TEST_COMMANDS.add(new EveryXHasY());
        TEST_COMMANDS.add(new RolesTestSub());
        TEST_COMMANDS.add(new ShipsMissingDPS());
        TEST_COMMANDS.add(new PingTestSub(this));
    }

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return true;
    }

    private IsShipTestSub isShipTestSub = new IsShipTestSub(this);
    private SearchTestSub searchTestSub = new SearchTestSub(this);

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        for(TestSubCommand cmd : TEST_COMMANDS){
            if(cmd.isInvoke(args[0])){
                cmd.action(args, event); break;
            }
        }
    }

    //<editor-fold desc="Boring stuff">
    @Override
    public String help() {
        return "No help yet" +
                "";
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


}
