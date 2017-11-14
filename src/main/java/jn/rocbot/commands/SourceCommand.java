package jn.rocbot.commands;

import jn.rocbot.commands.common.Command;
import jn.rocbot.commands.common.CommandConfig;
import jn.rocbot.commands.common.CommandType;
import net.dv8tion.jda.core.entities.impl.MessageEmbedImpl;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class SourceCommand implements Command {
    private CommandConfig config = new CommandConfig(CommandType.NORMAL, false);

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return true;
    }

    // "Roc-bot github source" "https://github.com/Jens0512/roc-bot"

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        event.getTextChannel().sendMessage(
                new MessageEmbedImpl()
                    .setUrl("https://github.com/Jens0512/roc-bot")
                    .setTitle("Roc-bots innards!")
                    .setDescription("Roc-bot github source")
        );
    }

    @Override
    public String help() {
        return "Usage: !source";
    }

    @Override
    public boolean executed(boolean success, MessageReceivedEvent event) {
        return false;
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
