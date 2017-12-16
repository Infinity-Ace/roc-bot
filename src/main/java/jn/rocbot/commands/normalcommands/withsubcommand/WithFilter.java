package jn.rocbot.commands.normalcommands.withsubcommand;

import jdk.jshell.JShell;

import java.util.StringJoiner;

public class WithFilter {
    public WithProperty[] properties;

    WithFilter(WithProperty[] properties) {
        this.properties = properties;
    }

    public String hrInfo() {
        new JShell.Builder().

        StringJoiner info = new StringJoiner("\n");
        info.add( "Filter has properties:");
        for (WithProperty property : properties){
            info.add("\t" + property.type + ", with value " + property.value);
        } return info.toString();
    }
}
