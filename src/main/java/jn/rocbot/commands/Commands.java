package jn.rocbot.commands;

import jn.rocbot.commands.modcommands.RolesCommand;
import jn.rocbot.commands.normalcommands.*;
import jn.rocbot.commands.common.Command;
import jn.rocbot.commands.devcommands.SayCommand;
import jn.rocbot.commands.devcommands.tests.TestCommand;

import java.util.HashMap;

public class Commands {
    public static final HashMap<String, Command> COMMANDS;

    static {
        COMMANDS = new HashMap<>();

        //Normal commands
        COMMANDS.put("hello", new HelloCommand());
        COMMANDS.put("help", new HelpCommand());
        COMMANDS.put("ships", new ShipsCommand());
        COMMANDS.put("auras", new AurasCommand());
        COMMANDS.put("source", new SourceCommand());
        COMMANDS.put("suggest", new SuggestCommand());

        //Moderator commads
        COMMANDS.put("roles", new RolesCommand());

        //Master commands
        COMMANDS.put("test", new TestCommand());
        COMMANDS.put("say", new SayCommand());
    }
}
