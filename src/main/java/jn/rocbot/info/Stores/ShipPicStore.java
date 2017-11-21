package jn.rocbot.info.Stores;

import jn.rocbot.Main;
import jn.rocbot.ships.Ship;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class ShipPicStore {
    private static ArrayList<String> pics;

    public static void init(){
        pics = new ArrayList<>();
        File folder = new File("pics/ships");
        for(File file : folder.listFiles()) {
            pics.add(file.getName().replace(".png", ""));
        }
    }

    public static boolean hasPic(Ship ship){
        return pics.contains(ship.name.toLowerCase().replace(" ", ""));
    }

    public static String getPicURL(Ship ship){
        return "https://raw.githubusercontent.com/Jens0512/roc-bot/master/pics/ships/"
                + ship.name.toLowerCase().replace(" ","") + ".png";
    }

    private static Color bgc = new Color(54, 57, 62, 255);
    public static Color getShipsAvgColor(Ship ship){
        HashMap<Color, Integer> colorhits = new HashMap<>();
        if(hasPic(ship)){
            try {
                BufferedImage img = ImageIO.read(new URL(getPicURL(ship)));
                for (int x = 0; x < img.getWidth(); x++) {
                    for (int y = 0; y < img.getHeight(); y++) {
                        Color c = new Color(img.getRGB(x, y));
                        if(!c.equals(bgc)) {
                            if(!colorhits.containsKey(c)){
                                colorhits.put(c, 1);
                            } else {
                                Integer hits = colorhits.get(c) + 1;
                                colorhits.replace(c, hits);
                            }
                        }
                    }
                }

                Color mostFrequent = (Color) colorhits.keySet().toArray()[0];
                for (Color key : colorhits.keySet()){
                    if(colorhits.get(key) > colorhits.get(mostFrequent)) mostFrequent = key;
                }

                return mostFrequent;

            } catch (IOException e) {
                e.printStackTrace();
                return Color.GRAY;
            }
        } else {
            return Color.GRAY;
        }
    }
}
