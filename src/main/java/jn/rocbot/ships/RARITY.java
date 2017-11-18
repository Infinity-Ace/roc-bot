package jn.rocbot.ships;

public enum RARITY{
    COMMON("<:common:378807923318718464>"),
    RARE("<:rare:378807923377307648>"),
    SUPER_RARE("<:superrare:378808209709858819>");

    private final String emoji;

    public String toEmoji(){
        return this.emoji;
    }

    public static String fromInt(int i) throws ArrayIndexOutOfBoundsException{
        switch(i){
            case 1:
                return "COMMON";
            case 2:
                return "RARE";
            case 3:
                return "SUPER_RARE";
        } throw new ArrayIndexOutOfBoundsException("The rarity must be from 1 - 3 not: " + i);
    }

    RARITY(String s) {
        this.emoji = s;
    }
}
