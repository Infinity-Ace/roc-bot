package jn.rocbot.commands.common;

public class CommandConfig {
    public CommandType type;
    public boolean ignoreCase;

    public CommandConfig(CommandType type, boolean ignoreCase){
        this.type = type;
        this.ignoreCase = ignoreCase;
    }
}
