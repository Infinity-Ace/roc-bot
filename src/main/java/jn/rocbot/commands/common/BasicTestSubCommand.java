package jn.rocbot.commands.common;

public abstract class BasicTestSubCommand implements SubCommand {
    private boolean ignoreCase;

    public BasicTestSubCommand(boolean ignoreCase){
        this.ignoreCase = ignoreCase;
    }

    public BasicTestSubCommand(){ this.ignoreCase = true; }
    @Override
    public CommandType getType() {
        return CommandType.DEV;
    }

    @Override
    public String help() {
        return "This is a dev command without help";
    }

    private CommandConfig config = new CommandConfig(CommandType.DEV, ignoreCase);

    @Override
    public CommandConfig getConfig() {
        return null;
    }


}
