package jn.rocbot.commands;

import jn.rocbot.commands.normalcommands.HelloCommand;
import jn.rocbot.commands.normalcommands.HelpCommand;
import jn.rocbot.commands.normalcommands.ShipsCommand;
import jn.rocbot.commands.normalcommands.SourceCommand;
import jn.rocbot.commands.common.Command;
import jn.rocbot.commands.devcommands.SayCommand;
import jn.rocbot.commands.devcommands.TestCommand;
import jn.rocbot.commands.modcommands.StatsCommand;

import java.util.HashMap;

public class Commands {
    public static final HashMap<String, Command> COMMANDS;

    static {
        COMMANDS = new HashMap<>();

        //Normal commands
        COMMANDS.put("hello", new HelloCommand());
        COMMANDS.put("help", new HelpCommand());
        COMMANDS.put("ships", new ShipsCommand());
        COMMANDS.put("source", new SourceCommand());

        //Moderator commads
        COMMANDS.put("stats", new StatsCommand());

        //Master commands
        COMMANDS.put("test", new TestCommand());
        COMMANDS.put("say", new SayCommand());
    }
}