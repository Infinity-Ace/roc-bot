package jn.rocbot;

import jn.rocbot.info.stores.*;
import jn.rocbot.permissions.Masters;
import jn.rocbot.permissions.Moderators;
import jn.rocbot.info.IDs;
import jn.rocbot.utils.Log;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;

import java.util.StringJoiner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static boolean DEBUG;
    public static boolean VERBOSE;
    public static boolean LOG_MESSAGES;

    public static String TOKEN;

    public static net.dv8tion.jda.core.JDA JDA;

    public static String[] ARGUMENTS;

    private static Logger log = Logger.getLogger(Log.class.getName());

    //requires the arguments String Token, boolean Debug, boolean Verbose
    public static void main(String[] args) {
        StringJoiner env_args_received = new StringJoiner(", ");
        for(String arg : args) env_args_received.add(arg);
        log.log(Level.INFO, "Ran with args: " + env_args_received);
        log.log(Level.INFO, "Provided token: " + args[0]);

        //Just sets some variables from the main method arguments
        //See the Procfile for the execution
        TOKEN = args[0];
        DEBUG = Boolean.parseBoolean(args[1].toLowerCase());
        VERBOSE = Boolean.parseBoolean(args[2].toLowerCase());
        LOG_MESSAGES = Boolean.parseBoolean(args[3].toLowerCase());

        ARGUMENTS = args; //For verbose debugging

        init(); //======================================================= IMPORTANT

        try { //Establishes a connection to the the servers that have added the bot as a user
            JDA = new JDABuilder(AccountType.BOT).addEventListener(
                    new Bot(/*Evil twin or not*/ Boolean.parseBoolean(args[4]))
            ).setToken(TOKEN).buildBlocking();

            JDA.setAutoReconnect(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void init() {
        Log.init(); //Keep above the init's which may need to log something!

        IDs.init();

        Masters.init();
        Moderators.init();

        ShipPicStore.init();
        WeaponStore.init();
        AuraStore.init();
        ZenStore.init();

        ShipStore.init(); //Must be kept at bottom!
    }
}
