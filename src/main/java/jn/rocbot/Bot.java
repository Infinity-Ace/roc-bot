package jn.rocbot;

import jn.rocbot.Permissions.Masters;
import jn.rocbot.commands.*;
import jn.rocbot.commands.common.Command;
import jn.rocbot.commands.HelloCommand;
import jn.rocbot.commands.common.CommandConfig;
import jn.rocbot.commands.devcommands.SayCommand;
import jn.rocbot.commands.devcommands.TestCommand;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

import static jn.rocbot.Main.LOGTYPE.INFO;

import jn.rocbot.RocParser.CommandContainer;

public class Bot extends ListenerAdapter {
    private final Random r = new Random();
    public static HashMap<String, Command> COMMANDS;

    private static boolean IS_EVIL_TEST_TWIN;

    public Bot(boolean is_evil_twin){
        Bot.IS_EVIL_TEST_TWIN = is_evil_twin;
    }

    public static RocParser PARSER;

    static {
        PARSER = new RocParser();

        //Normal commands
        COMMANDS = new HashMap<String, Command>();
        COMMANDS.put("hello", new HelloCommand());
        COMMANDS.put("help", new HelpCommand());
        COMMANDS.put("ships", new ShipsCommand());
        //COMMANDS.put("source", new SourceCommand());

        //Master commands
        COMMANDS.put("test", new TestCommand());
        COMMANDS.put("say", new SayCommand());
    }
    @Override
    public void onReady(ReadyEvent event){

        //Just some info to the log
        Main.log(INFO, "Logged in as " + event.getJDA().getSelfUser().getName());

        Main.log(INFO, "Roaming in the servers: ");

        for (Guild g : event.getJDA().getGuilds()) {
            Main.log(INFO,"\t" + g.getName() + ", IDLong: " + g.getIdLong());
        }

        //Showing masters
        Main.log(INFO, ("My masters are:"));
        Masters.MASTERS.forEach((Masters.Master m) -> Main.log(INFO, "\t" + m.name + ", ID: " + m.longID));

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
                            !event.getMessage().getAuthor().isBot()
                                &&
                                Masters.isMaster(event.getAuthor()));
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
        Main.log(Main.LOGTYPE.DEBUG, msg);
    }
    private void vlog(String msg){
        Main.log(Main.LOGTYPE.VERBOSE, msg);
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
                Main.log(Main.LOGTYPE.INFO, event.getAuthor() + ": " + event.getMessage().getContent());
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
            }
        }
    }


    /**
     * Makes the bot say something in the bot-channel in the phoenix II server
     * @param event
     * @param message
     */
    public void say(MessageReceivedEvent event, String message){
        event.getJDA().getGuildById(325430508379176961L).getTextChannelById(378546862627749908L).sendMessage(message).complete();
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
                if(IS_EVIL_TEST_TWIN)say(cmd.event, "I am evil " + Emojis.EL);
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
