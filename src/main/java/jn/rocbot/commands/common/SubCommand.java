package jn.rocbot.commands.common;

public interface SubCommand{
    String invoke();
    String help();

    CommandConfig getConfig();

    default CommandType getType(){
        return getConfig().type;
    }

    default boolean isInvoke(String arg){
        if(arg.toLowerCase().equals(invoke().toLowerCase())) return true;
        if(getConfig().OptionalInvokes.length != 0){
            for (String opt : getConfig().OptionalInvokes){
                if(opt.toLowerCase().equals(arg)) return true;
            }
        } return false;
    }
}
