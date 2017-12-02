package jn.rocbot.commands.devcommands.tests;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Arrays;

public class EveryXHasY extends TestSubCommand{
    @Override
    public String invoke() {
        return "check";
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        sendMessage(Arrays.toString(args), event);
    }
}
