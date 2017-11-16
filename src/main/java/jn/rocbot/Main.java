package jn.rocbot;

import jn.rocbot.Permissions.Masters;
import jn.rocbot.Permissions.Moderators;
import jn.rocbot.info.Stores.AuraStore;
import jn.rocbot.info.Stores.ShipStore;
import jn.rocbot.info.Stores.ZenStore;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;

import java.util.StringJoiner;

public class Main {

    //<editor-fold desc="ANSI codes">
    //For use in the logger
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";
    //</editor-fold>

    public static boolean DEBUG;
    public static boolean VERBOSE;
    public static boolean LOG_MESSAGES;

    public static String TOKEN;

    public static net.dv8tion.jda.core.JDA JDA;

    public static String[] ARGUMENTS;

    //requires the arguments String Token, boolean Debug, boolean Verbose
    public static void main(String[] args) {
        StringJoiner env_args_received = new StringJoiner(", ");
        for(String arg : args) env_args_received.add(arg);
        Main.log(LOGTYPE.INFO, "Ran with args: " + env_args_received);
        Main.log(LOGTYPE.INFO, "Provided token: " + args[0]);

        if(Boolean.parseBoolean(args[0].toLowerCase())) {

            //Just sets some variables from the main method arguments
            //See the Procfile for the execution
            TOKEN = args[0];
            DEBUG = Boolean.parseBoolean(args[1].toLowerCase());
            VERBOSE = Boolean.parseBoolean(args[2].toLowerCase());
            LOG_MESSAGES = Boolean.parseBoolean(args[3].toLowerCase());

            ARGUMENTS = args; //For verbose debugging

            init();

            try { //Establishes a connection to the the chats that have added the bot as a user

                JDA = new JDABuilder(AccountType.BOT).addEventListener(
                        new Bot(/*Evil twin or not*/ Boolean.parseBoolean(args[4]))
                ).setToken(TOKEN).buildBlocking();

                JDA.setAutoReconnect(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void init() {
        Masters.init();
        Moderators.init();

        AuraStore.init();
        ZenStore.init();
        ShipStore.init(); //Must be kept at bottom!
    }

    public enum LOGTYPE {
        INFO,
        ERROR,
        VERBOSE,
        DEBUG
    }

    public static void log(LOGTYPE type, String message){
        switch (type){
            case INFO:
                System.out.println(ANSI_BLUE + message + ANSI_RESET);
                break;
            case ERROR:
                System.out.println(ANSI_RED + message + ANSI_RESET);
                break;
            case VERBOSE:
                if(Main.VERBOSE)
                    System.out.println(ANSI_CYAN+ message + ANSI_RESET);
                break;
            case DEBUG:
                if(Main.DEBUG)
                    System.out.println(ANSI_YELLOW + message + ANSI_RESET);
                break;
        }
    }
}
