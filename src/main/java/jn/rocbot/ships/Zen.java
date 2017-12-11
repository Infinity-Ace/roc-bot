package jn.rocbot.ships;

import jn.rocbot.info.SimpleDescBuilder;
import jn.rocbot.utils.Formatter;

import java.util.Arrays;
import java.util.HashMap;

public class Zen extends Ship.ShipProperty implements Formatter{
    public final String desc;

    public final String ultimateName;
    public final String ultimateDesc;

    public final HashMap<String, String> properties;
    private final HashMap<String, String> formatting;
    public String[] abbreviations;

    public Zen(String name, String desc, String ultimateName, String ultimateDesc, HashMap<String, String> properties, HashMap<String, String> formatting) {
         /*  */ super(name);
         /*  */ this.desc = desc;
        this.ultimateName = ultimateName;
        this.ultimateDesc = ultimateDesc;

        this.properties = properties;

        this.formatting = formatting;
    }

    public String simpleDesc(boolean withTitle){
        SimpleDescBuilder desc;
        if(withTitle) desc = new SimpleDescBuilder(bold(name));
        else desc = new SimpleDescBuilder();
        desc.addLine(this.desc);
        for(String key : properties.keySet()){
            if(formatting.containsKey(key + "-format")){
                desc.addLine("\t" + key + ": " + spaced_italic(properties.get(key) + formatting.get(key + "-format")));
            } else {
                desc.addLine("\t" + key + ": " + spaced_italic(properties.get(key)));
            }
        }

        desc.addLine("Ultimate: " + italic(ultimateName));
        desc.addLine("\tDescription: " + this.ultimateDesc);

        return desc.get();
    }

    public Zen setAbbreviations(String[] abbreviations){
        this.abbreviations = abbreviations;
        return this;
    }


    public boolean isInteger(String str) {
        int size = str.length();

        for (int i = 0; i < size; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        } return size > 0;
    }
}
