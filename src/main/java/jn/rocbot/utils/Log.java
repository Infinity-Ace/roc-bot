package jn.rocbot.utils;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Log {
    private final static Logger LOGGER = Logger.getLogger(Log.class.getName());

    private static FileHandler fh = null;

    public static void init(){
        try {
            fh = new FileHandler("logtest.log", true);
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }

        Logger l = Logger.getLogger("");
        fh.setFormatter(new SimpleFormatter());
        l.addHandler(fh);
        l.setLevel(Level.CONFIG);
    }
}