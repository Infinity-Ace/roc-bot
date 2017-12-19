package jn.rocbot.ships;

import jn.rocbot.Emojis;

public final class DamageType {
    public static DamageType SB = new DamageType("sb", Emojis.SB);
    public static DamageType AP = new DamageType("ap", Emojis.AP);
    public static DamageType HI = new DamageType("hi", Emojis.HI);

    private String string;
    public final String emoji;

    private DamageType(String s, String emoji) {
        this.string = s;

        this.emoji = emoji;
    }

    private static DamageType[] values = {SB, AP, HI};

    public static DamageType[] values() {
        return values;
    }

    public static boolean isDamageType(String search){
        for(DamageType dt : DamageType.values()){
            if(dt.string.equals(search.toLowerCase())) return true;
            if(dt.toString().toLowerCase().equals(search.toLowerCase())) return true;
        } return false;
    }

    @Override
    public String toString(){
        switch (string) {
            case "sb": return "Shield breaking";
            case "ap": return "Armor piercing";
            case "hi": return "High impact";

            default: //Doesn't happen
                try {
                    throw new DamageTypeNotFoundException(String.format("No damagetype abbreviated %s", string));
                } catch (DamageTypeNotFoundException e) {
                    e.printStackTrace();
                }
        } return "";
    }

    public static DamageType fromString(String search) throws DamageTypeNotFoundException {
        if(search.toLowerCase().equals(SB.string)) return SB;
        else if(search.toLowerCase().equals(AP.string)) return AP;
        else if(search.toLowerCase().equals(HI.string)) return HI;

        else if(search.toLowerCase().equals("shield breaking")) return SB;
        else if(search.toLowerCase().equals("armor piercing")) return AP;
        else if(search.toLowerCase().equals("high impacting")) return HI;

        throw new DamageTypeNotFoundException("No damagetype named " + search + "'");
    }

    public static class DamageTypeNotFoundException extends Exception {
        public DamageTypeNotFoundException(String message) {
            super(message);
        }
    }
}
