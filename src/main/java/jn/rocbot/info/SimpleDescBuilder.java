package jn.rocbot.info;

import java.util.StringJoiner;

public class SimpleDescBuilder {
    StringJoiner desc = new StringJoiner("\n");

    public SimpleDescBuilder(){ }

    public SimpleDescBuilder(String title){
        addLine(title + ":");
    }

    public void addLine(String line){
        desc.add(line);
    }

    public void add(String string) {
        desc.setEmptyValue(string);
    }

    public String get() {
        return desc.toString();
    }
}
