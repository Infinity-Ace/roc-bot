package jn.rocbot.commands.normalcommands;

import jn.rocbot.commands.common.PREFIXES;
import jn.rocbot.permissions.Masters;
import jn.rocbot.permissions.Moderators;
import jn.rocbot.commands.common.Command;
import jn.rocbot.commands.common.CommandConfig;
import jn.rocbot.commands.common.CommandType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Iterator;
import java.util.StringJoiner;

import static jn.rocbot.commands.Commands.COMMANDS;

public class HelpCommand implements Command {
    private final static String HELP = "Usage: !help *command*";

    private CommandConfig config = new CommandConfig(CommandType.NORMAL, true);

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if(args.length == 0){
            String message = "Use *!help <SomeCommand>* to toString help on specific command";
            message += "\nAvailable commands are: ";

            StringJoiner allCommands = new StringJoiner(", ");
            for (Iterator<String> iterator = COMMANDS.keySet().iterator(); iterator.hasNext(); ) {
                String key = iterator.next();
                Command cmd = COMMANDS.get(key);

                switch (cmd.getType()) {
                    case NORMAL:
                        allCommands.add(String.format("%s%s", PREFIXES.NORMAL.PREFIX, key));
                        break;
                    case MOD:
                        if (Moderators.authorIsModerator(event)) allCommands.add(String.format("%s%s", PREFIXES.MODERATOR.PREFIX, key));
                        break;
                    case DEV:
                        if (Masters.isMaster(event.getAuthor())) allCommands.add(String.format("%s%s", PREFIXES.MASTER.PREFIX, key));
                        break;
                }
            }

            message += allCommands.toString();

            event.getTextChannel().sendMessage(message).complete();

        } else {
            if(COMMANDS.containsKey(args[0].toLowerCase())){
                event.getTextChannel().sendMessage(COMMANDS.get(args[0].toLowerCase()).help()).complete();
            } else {
                event.getTextChannel().sendMessage("No command named *" + args[0] + "* use !help for a list of avaible commands").complete();
            }
        }
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
}
