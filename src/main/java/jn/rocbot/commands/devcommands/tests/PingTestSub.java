package jn.rocbot.commands.devcommands.tests;

import jn.rocbot.Main;
import jn.rocbot.info.stores.ShipStore;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.StringJoiner;

public class PingTestSub extends TestSubCommand {
    private TestCommand testCommand;

    void sendResult(String[] args, MessageReceivedEvent event) {
        String ping = String.valueOf(Main.JDA.getPing());
        testCommand.sendMessage(ping, event);
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        sendResult(args, event);
    }

    public PingTestSub(TestCommand testCommand){
        super(false);
        this.testCommand = testCommand;
    }

    @Override public String invoke() {
        return "ping";
    }
}