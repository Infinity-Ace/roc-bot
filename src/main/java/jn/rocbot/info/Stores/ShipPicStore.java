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
}
