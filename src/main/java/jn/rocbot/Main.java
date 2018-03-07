package jn.rocbot;

import jn.rocbot.info.stores.*;
import jn.rocbot.misc.BotIsDoing;
import jn.rocbot.misc.Sessions;
import jn.rocbot.permissions.Masters;
import jn.rocbot.permissions.Moderators;
import jn.rocbot.ships.DamageType;
import jn.rocbot.utils.Log;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

import javax.security.auth.login.LoginException;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import java.util.StringJoiner;
import java.util.logging.Level;
import java.util.logging.Logger;

import static jn.rocbot.utils.Log.LogType.*;

public class Main {
    public static boolean DEBUG, VERBOSE, LOG_MESSAGES;

    private static String TOKEN;

    public static net.dv8tion.jda.core.JDA JDA;

    private static Logger log = Logger.getLogger(Log.class.getName());

    //requires the arguments String Token, boolean Debug, boolean Verbose
    public static void main(String[] args) {
        IDs.init();

        StringJoiner env_args_received = new StringJoiner(", ");
        for(String arg : args) env_args_received.add(arg);

        log.log(Level.INFO,"Ran with args: " + env_args_received);
        log.log(Level.INFO,"Provided token: " + args[0]);

        //Just sets some variables from the main method arguments
        TOKEN = args[0];
        DEBUG = Boolean.parseBoolean(args[1].toLowerCase());
        VERBOSE = Boolean.parseBoolean(args[2].toLowerCase());
        LOG_MESSAGES = Boolean.parseBoolean(args[3].toLowerCase());

        boolean IS_EVIL_TWIN = Boolean.parseBoolean(args[4].toLowerCase());

        Bot bot = new Bot(IS_EVIL_TWIN);

        try { //Establishes a connection to the the servers that have added the bot as a user
            JDA = new JDABuilder(AccountType.BOT)
                    .setGame(BotIsDoing.watchingYou)
                    .addEventListener(
                            bot // Bzzt
            ).setToken(TOKEN).buildBlocking();

            JDA.setAutoReconnect(true);
        } catch (InterruptedException | RateLimitedException | LoginException e) {
            e.printStackTrace();
            abort();
        }

        init(); //======================================================= IMPORTANT
        secondInit();

        StringJoiner logVars = new StringJoiner(", ");
        logVars.add("DEBUG: " + DEBUG);
        logVars.add("VERBOSE: " + VERBOSE);
        logVars.add("LOG MESSAGES: " + LOG_MESSAGES);

        Log.LogGroup group = new Log.LogGroup();
        group.add(logVars.toString());

        if (IS_EVIL_TWIN) {
            group.add("Bot is evil! " + Emojis.EL );
        }

        Log.log(INFO, String.format("Log variables are: %s", group.get()));

        Bot.onReadyLog();

        Sessions.start();

        Log.log(Log.LogType.CONNECTION, "Established connection with discord\nSession: " + Sessions.SESSIONS);
    }

    private static void abort() { abort(1); }
    private static void abort(int status) {
        System.exit(status);
    }

    private static void init() {
        Log.LogGroup group = new Log.LogGroup();

        Log.init(); //Keep above the init's which may need to log something!
        group.add("Ids initiated");

        try {
            Masters.init();
        } catch (FileNotFoundException e) {
            Log.log(ERROR, "The masters section in the permissions.json file was not found by the bot\n" + e.getLocalizedMessage());
            e.printStackTrace();
            abort();
        } try {
            Moderators.init();
        } catch (FileNotFoundException e) {
            Log.log(ERROR, "The moderators section in the permissions.json file was not found by the bot\n" + e.getLocalizedMessage());
            e.printStackTrace();
            abort();
        } group.add("Roles initiated");

        ShipPicStore.init();
        group.add("Ship-pictures initiated");

        try {
            WeaponStore.init();
        } catch (FileNotFoundException | DamageType.DamageTypeNotFoundException | UnsupportedEncodingException e) {
            Log.log(ERROR, "Something went wrong in the initialization of the weapons in the ships.json file:\n" + e.getMessage());
            e.printStackTrace();
            abort();
        } group.add("Ship-weapons initiated");

        try {
            AuraStore.init();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            Log.log(ERROR, "Something went wrong in the initialization of the auras in the auras.json file:\n" + e.getMessage());
            e.printStackTrace();
            abort();
        } group.add("Auras initiated");

        try {
            ZenStore.init();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            Log.log(ERROR, "Something went wrong in the initialization of the zens in the zens.json file:\n" + e.getMessage());
            e.printStackTrace();
            abort();
        } group.add("Zens initiated");

        try {
            ShipStore.init(); //Must be kept at bottom!
        } catch (FileNotFoundException e) {
            Log.log(ERROR, "The ships.json file was not found by the bot");
            e.printStackTrace();
            abort();
        } catch (UnsupportedEncodingException e) {
            Log.log(ERROR, "Somethings went wrong in reading the ships.json file: " + e.getMessage());
            e.printStackTrace();
            abort();
        } catch (AuraStore.AuraNotFoundException | WeaponStore.WeaponNotFoundException | ZenStore.ZenNotFoundException e) {
            Log.log(ERROR, e.getMessage());
            e.printStackTrace();
            abort();
        } group.add("Ships initiated!\nMeaning everything worked as it should");

        Log.log(Log.LogType.VERBOSE, group.get());

        Bot.READY = true;
    }

    private static void secondInit() {
        Moderators.secondInit(JDA);
    }
}
