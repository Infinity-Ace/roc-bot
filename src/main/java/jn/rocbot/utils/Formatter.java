package jn.rocbot.utils;

public interface Formatter {
    default String italic(String s){
        return "*" + s + "*";
    }

    default String spaced_italic(String s){
        return " *" + s + "* ";
    }

    default String bold(String s){
        return "**" + s + "**";
    }
}
