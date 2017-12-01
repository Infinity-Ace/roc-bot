package jn.rocbot.commands.normalcommands;

import jn.rocbot.commands.common.Command;
import jn.rocbot.commands.common.CommandConfig;
import jn.rocbot.commands.common.CommandType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.io.*;
import java.nio.file.Files;
import java.util.StringJoiner;

public class SuggestCommand implements Command{
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return true;
    }

    @Override
    public synchronized void action(String[] args, MessageReceivedEvent event) {
        sendMessage("Thank you! The message has been sent to Jens", event);
        BufferedWriter writer;
        StringJoiner message = new StringJoiner(" ");

        for (String arg : args) {
            message.add(arg);
        }
        try {
            String content = new String(Files.readAllBytes(new File("suggestions.txt").toPath()));

            writer = new BufferedWriter(new FileWriter("suggestions.txt"));
            writer.write(content + "\n\n" + event.getAuthor().getName() + ":\n" + message.toString() + "\n\n");

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String help() {
        return "Usage: !suggest <(Suggestion)>";
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
