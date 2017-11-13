package jn.rocbot;

import jn.rocbot.commands.Command;
import jn.rocbot.commands.HelpCommand;
import jn.rocbot.commands.ShipsCommand;
import jn.rocbot.commands.SourceCommand;
import jn.rocbot.commands.testcommands.HelloCommand;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.Random;

import static jn.rocbot.Main.LOGTYPE.INFO;
import static jn.rocbot.Main.LOGTYPE.VERBOSE;

import jn.rocbot.RocParser.CommandContainer;

public class Bot extends ListenerAdapter {
    private final Random r = new Random();
    public static HashMap<String, Command> COMMANDS;

    public static RocParser PARSER;

    static {
        PARSER = new RocParser();

        COMMANDS = new HashMap<String, Command>();
        COMMANDS.put("hello", new HelloCommand());
        COMMANDS.put("help", new HelpCommand());
        COMMANDS.put("ships", new ShipsCommand());
        //COMMANDS.put("source", new SourceCommand());
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
        if(event.getTextChannel().getIdLong() == 378546862627749908L) {
            if (Main.SHOW_MESSAGES) {
                Main.log(Main.LOGTYPE.INFO, "MessageRecieved!: " + event.getMessage().getContent() + "\nFrom user: " + event.getMessage().getAuthor().getName() + ", isbot: " + event.getMessage().getAuthor().isBot());
            }
            //Checks if the message starts with ! and if the sender is not a bot
            if (event.getMessage().getContent().startsWith("!") && !event.getMessage().getAuthor().isBot()) {
                dlog("Recieved message starting with \"!\": " + event.getMessage().getContent());
                handleCommand(PARSER.parse(event.getMessage().getContent().toLowerCase(), event));
            } else {
                if (!event.getAuthor().isBot()) {
                    String raw = event.getMessage().getContent().toLowerCase();

                    //Some special cases -----------------------------------------------------

                    if (raw.contains("name the bot")) {
                        event.getTextChannel().sendMessage("No " + Emojis.EL).complete();
                    } else if (raw.contains("thanks bot")) {
                        String str = "";

                        if (r.nextInt(10) == 1) str = " Glad to be of use";
                        event.getTextChannel().sendMessage("No problem! ^^" + str).complete();
                    } else if (raw.contains("best") && raw.contains("game")) {
                        event.getTextChannel().sendMessage("The best game is **Phoenix 2**! " + Emojis.EL).complete();
                    }
                }
            }
        }
    }

    @Override
    public void onReady(ReadyEvent event){
        //Just some info to the log
        Main.log(INFO, "Logged in as " + event.getJDA().getSelfUser().getName());
        Main.log(INFO, "Log variables; DEBUG: " + Main.DEBUG + ", VERBOSE: " + Main.VERBOSE);
        Main.log(INFO, "Roaming in the servers: ");        

        for (Guild g : event.getJDA().getGuilds()) {
            dlog("\t" + g.getName());
        }
        
        event.getJDA().getGuildById(325430508379176961L).getTextChannelById(378546862627749908L).sendMessage("I have rebooted").complete();
    }

    /**
     * Executes the command if it is valid
     * @param cmd A container containing the details for the command
     */
    private void handleCommand(CommandContainer cmd){
        dlog(cmd.hrInfo());
        if(COMMANDS.containsKey(cmd.invoke)){
            boolean safe = COMMANDS.get(cmd.invoke).called(cmd.args, cmd.event);

            if(safe){
                COMMANDS.get(cmd.invoke).action(cmd.args, cmd.event);
                COMMANDS.get(cmd.invoke).executed(true, cmd.event);
            }else{
                COMMANDS.get(cmd.invoke).executed(false, cmd.event);
            }
        }
    }
}
