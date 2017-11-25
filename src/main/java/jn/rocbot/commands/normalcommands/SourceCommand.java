package jn.rocbot.commands.normalcommands;

import jn.rocbot.commands.common.Command;
import jn.rocbot.commands.common.CommandConfig;
import jn.rocbot.commands.common.CommandType;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class SourceCommand implements Command {
    private CommandConfig config = new CommandConfig(CommandType.NORMAL, true);

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return true;
    }

    // "Roc-bot github source" "https://github.com/Jens0512/roc-bot"

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        event.getTextChannel().sendMessage(
                new EmbedBuilder().setThumbnail("https://github.com/Jens0512/roc-bot")
                    .setTitle("Roc-bots innards!")
                    .setDescription("Roc-bot github source")
                .build()
        ).complete();
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
