package jn.rocbot.commands.common;

public enum PREFIXES {
    NORMAL("!"),
    MODERATOR("%"),
    MASTER("§");

    public final String PREFIX;

    PREFIXES(String prefix){
        this.PREFIX = prefix;
    }
}
