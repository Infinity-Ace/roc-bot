package jn.rocbot.ships;

import java.util.StringJoiner;

public final class Rarity extends Ship.ShipProperty{
    public static Rarity COMMON = new Rarity("Common", "<:common:378807923318718464>");
    public static Rarity RARE = new Rarity("Rare", "<:rare:378807923377307648>");
    public static Rarity SUPER_RARE = new Rarity("Super Rare","<:superrare:378808209709858819>");

    private static Rarity[] values = {COMMON, RARE, SUPER_RARE};

    public static Rarity[] values() {
        return values;
    }

    private String emoji;

    private Rarity(String name, String emoji) {
        super(name);
        this.emoji = emoji;
    }

    public String toEmoji() {
        return this.emoji;
    }

    public static String fromInt(int i) throws ArrayIndexOutOfBoundsException {
        switch (i) {
            case 1:
                return "Common";
            case 2:
                return "Rare";
            case 3:
                return "Super Rare";
        } throw new ArrayIndexOutOfBoundsException("The rarity must be from 1 - 3 not: " + i);
    }

    public static boolean isRarity(String name){
        String proccesed_name = name.replace("rarity ", "");

        for (Rarity rarity : values())
            if(rarity.name.toLowerCase().equals(proccesed_name.toLowerCase())) return true;

        return false;
    }

    public static Rarity fromString(String name){
        String[] temp =  name.replace("rarity", "").split(" ");
        StringJoiner proccessed = new StringJoiner(" ");
        for (String string : temp) {
            if(!string.isEmpty()) proccessed.add(string);
        }

        switch (proccessed.toString().toLowerCase()){
            case "common":
                return COMMON;
            case "rare":
                return RARE;
            case "super rare":
                return SUPER_RARE;
            default: throw new IllegalArgumentException("There is no rarity named " + name + "!");
        }
    }
}

