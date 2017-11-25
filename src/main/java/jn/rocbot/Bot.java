package jn.rocbot;

import jn.rocbot.Permissions.Masters;
import jn.rocbot.RocParser.CommandContainer;
import jn.rocbot.commands.commands.HelloCommand;
import jn.rocbot.commands.commands.HelpCommand;
import jn.rocbot.commands.commands.ShipsCommand;
import jn.rocbot.commands.common.Command;
import jn.rocbot.commands.common.CommandConfig;
import jn.rocbot.commands.devcommands.SayCommand;
import jn.rocbot.commands.devcommands.TestCommand;
import jn.rocbot.utils.Log;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.StringJoiner;
import java.util.logging.Logger;

import static java.util.logging.Level.*;
import static jn.rocbot.commands.Commands.COMMANDS;

public class Bot extends ListenerAdapter {
    private final Random r = new Random();

    private static boolean IS_EVIL_TEST_TWIN;

    public Bot(boolean is_evil_twin){
        Bot.IS_EVIL_TEST_TWIN = is_evil_twin;
    }

    public static RocParser PARSER;
    
    private Logger log = Logger.getLogger(Log.class.getName());

    static {
        PARSER = new RocParser();
    }
    @Override
    public void onReady(ReadyEvent event){

        //Just some info to the log
        log.log(INFO, "Logged in as " + event.getJDA().getSelfUser().getName());
        log.log(INFO, "Startup at: " + new DateTime().toString());
        log.log(INFO, "Roaming in the servers: ");

        StringJoiner serverList = new StringJoiner("\n");
        for (Guild g : event.getJDA().getGuilds()) {
            serverList.add("\t" + g.getName() + ", IDLong: " + g.getIdLong());
        } log.log(INFO, "\n" + serverList.toString());

        //Showing masters
        log.log(INFO, ("My masters are:"));
        StringJoiner masterList = new StringJoiner("\n");
        Masters.MASTERS.forEach((Masters.Master m) -> masterList.add("\t" + m.name + ", ID: " + m.longID));
        log.log(INFO,"\n" + masterList.toString());

        Guild phoenix2 = event.getJDA().getGuildById(325430508379176961L);

        if(IS_EVIL_TEST_TWIN){
            log.log(WARNING, ("\n\t+------------------ I AM EVIL! ------------------+\n"));
            phoenix2.getController().setNickname(phoenix2.getSelfMember(), "Evil twin-Roc-bot");
        } else { //Does not work!
            phoenix2.getController().setNickname(phoenix2.getSelfMember(), "Roc-bot");
        }

        //say(event, "I have rebooted");
    }

    private void specialCases(MessageReceivedEvent event){
        if (!event.getAuthor().isBot()) {
            String raw = event.getMessage().getContent();

            //Some special cases -----------------------------------------------------
            if (raw.contains("name the bot")) {
                event.getTextChannel().sendMessage("No " + Emojis.EL).complete();
            } else if (raw.toLowerCase().contains("thanks bot") || raw.toLowerCase().contains("thank you bot")) {
                String str = "";

                if (r.nextInt(10) == 1) str = " Glad to be of use";
                event.getTextChannel().sendMessage("No problem! ^^" + str).complete();
            } else if (raw.contains("best") && raw.contains("game")) {
                event.getTextChannel().sendMessage("The best game is **Phoenix 2**! " + Emojis.EL).complete();
            }
        }
    }

    private boolean shouldReact(MessageReceivedEvent event){
        //if(event.getMessage().getType())
        if(!IS_EVIL_TEST_TWIN) {
            return ((event.getTextChannel().getIdLong() == 378546862627749908L //Bot-channel
                        ||
                        event.getTextChannel().getIdLong() == 377889873694031872L //My test-server
                            ||
                            event.getGuild().getIdLong() == 378949749883273217L) /* Mug's test-server */
                                &&
                                !event.getMessage().getAuthor().isBot()); //To prevent recursion
        }else{
            return ((event.getTextChannel().getIdLong() == 378546862627749908L //Bot-channel
                    ||
                    event.getTextChannel().getIdLong() == 377889873694031872L //My test-server
                        ||
                        event.getGuild().getIdLong() == 378949749883273217L) /* Mug's test-server */
                            &&
                            !event.getMessage().getAuthor().isBot() //Same as the other (^) !isbot
                                &&
                                Masters.isMaster(event.getAuthor())); //Wouldn't want anyone crashing my pc
        }
    }

    private String getPrefix(MessageReceivedEvent event){
        String message = event.getMessage().getContent();
        if(message.charAt(0) == '!'){
            vlog("Recieved message starting with ! from " + event.getAuthor().getName());
            return "!";
        }else if(message.charAt(0) == '§'){
            vlog("Recieved message starting with § from " + event.getAuthor().getName());
            return "§";
        }else if(message.charAt(0) == '~'
                    &&
                    message.charAt(1) == '!'){
            vlog("Recieved message starting with ~! from " + event.getAuthor().getName());
            return "~!";
        } else return "?";
    }

    private void dlog(String msg){
        log.log(FINE, msg);
    }
    private void vlog(String msg){
        log.log(FINER, msg);
    }

    /**
     * Runs every time a message is received
     * @param event here all the neccessary information is found
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        //Checks if the bot is supposed to react
        if(shouldReact(event)){
            String prefix = getPrefix(event);
            if (Main.LOG_MESSAGES) {
                log.log(INFO, event.getAuthor().getName() + ": " + event.getMessage().getContent());

            } if(isValidKey(event.getMessage().getContent().replace(prefix, "").split(" ")[0])) {
                //Checks if the message starts with ! and if the sender is not a bot
                if (Objects.equals(prefix, "!") && !event.getMessage().getAuthor().isBot()) {
                    handleCommand(PARSER.parse(event.getMessage().getContent().toLowerCase(),
                            getConfig(event.getMessage().getContent().replace("!", "").split(" ")[0]),
                            event));
                } else if (prefix.equals("§")
                        && Masters.isMaster(event.getAuthor())
                        ) { // If it is a mastercommand
                    dlog("Recieved message starting with \"§\": " + event.getMessage().getContent());
                    handleCommand(PARSER.parse(
                            event.getMessage().getContent(),
                            getConfig(event.getMessage().getContent().replace("§", "").split(" ")[0]),
                            event)
                    );
                }
            } else {
                specialCases(event);
            }
        }
    }

    /**
     * Makes the bot say something in the bot-channel in the phoenix II server
     * @param event
     * @param message
     */
    public void say(MessageReceivedEvent event, String message, long channelid){
        if(channelid == 0L) channelid = 378546862627749908L;
        event.getJDA().getGuildById(325430508379176961L).getTextChannelById(channelid).sendMessage(message).complete();
    }

    /**
     * Executes the command if it is valid
     * @param cmd A container containing the details for the command
     */
    private void handleCommand(CommandContainer cmd){
        dlog(cmd.hrInfo());
        if(COMMANDS.containsKey(cmd.invoke)){
            boolean safe = COMMANDS.get(cmd.invoke).called(cmd.args, cmd.event);
            dlog("\tExecuted = " + COMMANDS.get(cmd.invoke).called(cmd.args, cmd.event));

            if(safe){
                COMMANDS.get(cmd.invoke).action(cmd.args, cmd.event);
                COMMANDS.get(cmd.invoke).executed(true, cmd.event);
            }else{
                COMMANDS.get(cmd.invoke).executed(false, cmd.event);
            }
        }
    }

    private boolean isValidKey(String commandKey){
        return COMMANDS.containsKey(commandKey);
    }

    private CommandConfig getConfig(String commandKey){
        for(String key : COMMANDS.keySet()){
            if(commandKey.equals(key)) {
                return COMMANDS.get(key).getConfig();
            }
        } try {
            throw new Exception("Found no command-configuration for: " + commandKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
