package jn.rocbot.commands.normalcommands;

import jn.rocbot.Permissions.Masters;
import jn.rocbot.Permissions.Moderators;
import jn.rocbot.commands.common.Command;
import jn.rocbot.commands.common.CommandConfig;
import jn.rocbot.commands.common.CommandType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

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
            String header = "Use *!help <SomeCommand>* to get help on specific command";
            header += "\nAvaible commands are: ";
            StringJoiner allCommands = new StringJoiner(", ");
            if(!Masters.isMaster(event.getAuthor())) {
                for (String key : COMMANDS.keySet()) {
                    if(COMMANDS.get(key).getType() == CommandType.NORMAL)
                        allCommands.add("!" + key);
                } event.getTextChannel().sendMessage(header).complete();
            }else{
                for (String key : COMMANDS.keySet()) {
                    switch (COMMANDS.get(key).getType()) {
                        case NORMAL:
                            allCommands.add("!" + key);
                            break;
                        case MOD:
                            allCommands.add("~!" + key);
                            break;
                        case DEV:
                            allCommands.add("§" + key);
                            break;
                    }
                } header += "\n\tUse *!help <SomeCommand>* to get help on specific command";

                event.getTextChannel().sendMessage(header).complete();
            }
            if(Moderators.isModerator(event.getAuthor())){

            }
        } else if (args.length > 0){
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
