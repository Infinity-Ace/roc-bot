package jn.rocbot.commands.devcommands.tests;

import jn.rocbot.commands.common.CommandConfig;
import jn.rocbot.commands.common.CommandType;
import jn.rocbot.commands.common.SubCommand;
import jn.rocbot.commands.devcommands.tests.TestCommand;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public abstract class TestSubCommand implements SubCommand {
    private boolean ignoreCase;

    public TestSubCommand(boolean ignoreCase){
        this.ignoreCase = ignoreCase;
    }

    public TestSubCommand(){ this.ignoreCase = true; }
    @Override
    public CommandType getType() {
        return CommandType.DEV;
    }

    @Override
    public String help() {
        return "This is a dev command without help";
    }

    public void action(String[] args, MessageReceivedEvent event) {}

    private CommandConfig config = new CommandConfig(CommandType.DEV, ignoreCase);

    @Override
    public CommandConfig getConfig() {
        return config;
    }


}
