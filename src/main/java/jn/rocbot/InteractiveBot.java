package jn.rocbot;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class InteractiveBot extends ListenerAdapter{
    public static String TOKEN;

    public static JDA JDA;

    public static void main(String[] args){
        TOKEN = args[0];

        try { //Establishes a connection to the the chats that have added the bot as a user
            JDA = new JDABuilder(AccountType.BOT).addEventListener(
                    new InteractiveBot()).setToken(TOKEN).buildBlocking();

            JDA.setAutoReconnect(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReady(ReadyEvent event) {

    }

    //<editor-fold desc="Logging">

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
    //</editor-fold>
}
