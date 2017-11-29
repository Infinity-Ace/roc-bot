package jn.rocbot.commands.normalcommands;

import jn.rocbot.commands.common.Command;
import jn.rocbot.commands.common.CommandConfig;
import jn.rocbot.commands.common.CommandType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringJoiner;

public class SuggestCommand implements Command{
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return true;
    }

    @Override
    public synchronized void action(String[] args, MessageReceivedEvent event) {
        sendMessage("Thank you! The message has been sent to Jens", event);
        BufferedWriter writer = null;
        StringJoiner message = new StringJoiner(" ");
        for (int i = 0; i < args.length; i++) {
            message.add(args[i]);
        }
        try {
            writer = new BufferedWriter(new FileWriter("Suggestions.txt"));

            writer.write("\n\n" + message.toString() + "\n\n");

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String help() {
        return "Usage: !suggest Make the bot awesome!";
    }

    @Override
    public boolean executed(boolean success, MessageReceivedEvent event) {
        return true;
    }

    private CommandConfig config =
            new CommandConfig(CommandType.NORMAL, true);

    @Override
    public CommandConfig getConfig() {
        return config;
    }
}
