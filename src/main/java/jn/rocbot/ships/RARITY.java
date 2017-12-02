package jn.rocbot.ships;

public final class RARITY {
    public static RARITY COMMON = new RARITY("Common", "<:common:378807923318718464>");
    public static RARITY RARE = new RARITY("Rare", "<:rare:378807923377307648>");
    public static RARITY SUPER_RARE = new RARITY("Super Rare","<:superrare:378808209709858819>");

    private static RARITY[] values = {COMMON, RARE, SUPER_RARE};

    public static RARITY[] values() {
        return values;
    }

    public String name;
    private String emoji;

    private RARITY(String name, String emoji) {
        this.name = name;
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

    public static boolean isRarity(String string){
        for (RARITY rarity : values())
            if(rarity.name.toLowerCase().equals(string.toLowerCase())) return true;

        return false;
    }

    public static RARITY fromString(String name){
        switch (name){
            case "Common":
                return COMMON;
            case "Rare":
                return RARE;
            case "Super Rare":
                return SUPER_RARE;
            default: throw new IllegalArgumentException("There is no rarity named " + name + "!");
        }
    }
}
