package jn.rocbot;

import jn.rocbot.commands.common.Command;
import jn.rocbot.permissions.Masters;
import jn.rocbot.RocParser.CommandContainer;
import jn.rocbot.commands.common.CommandConfig;
import jn.rocbot.info.IDs;
import jn.rocbot.permissions.Moderators;
import jn.rocbot.utils.Log;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.joda.time.DateTime;

import java.util.Random;
import java.util.StringJoiner;
import java.util.logging.Logger;

import static jn.rocbot.utils.Log.LogType.*;
import static jn.rocbot.commands.Commands.COMMANDS;

public class Bot extends ListenerAdapter {
    public static boolean READY = false;

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

    public static void onReadyLog(){
        //Just some info to the log
        Log.log(INFO, "Startup at: " + new DateTime().toString());
        Log.log(INFO, "Logged in as " + Main.JDA.getSelfUser().getName());

        {
            StringJoiner serverList = new StringJoiner("\n");

            for (Guild g : Main.JDA.getGuilds()) {
                serverList.add("\t" + g.getName() + ", IDLong: " + g.getIdLong() + "Owner: " + g.getOwner().getEffectiveName());
            }
            Log.log(INFO, "Roaming in the servers: \n" + serverList.toString());
        }

        {
            //Showing masters
            StringJoiner masterList = new StringJoiner("\n");
            Masters.MASTERS.forEach((Masters.Master m) -> masterList.add("\t" + m.name + ", ID: " + m.longID));

            Log.log(
                    INFO,
                    "My masters are:"
                            + "\n" + masterList.toString()
            );
        }

        {
            StringJoiner moderatorsList = new StringJoiner("\n");
            Moderators.MODERATORS.forEach((Moderators.Moderator m) -> moderatorsList.add("\t" + m.name + ", ID: " + m.longID));
            Log.log(
                    INFO,
                    "My moderators are:"
                            + "\n" + moderatorsList.toString()
            );
        }

        if(IS_EVIL_TEST_TWIN){
            System.out.println("\n\t+------------------ I AM EVIL! ------------------+\n");
        }
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
        if(!READY) return false;

        if(event.getGuild().getIdLong() == IDs.GUILDS.get(IDs.ID_KEY.GUILD_BOT_CC)
                && !event.getAuthor().isBot()) return true;

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
                        event.getGuild().getIdLong() == 378949749883273217L) /* Rocs*/
                            &&
                            !event.getMessage().getAuthor().isBot() //Same as the other (^) !isbot
                                &&
                                Masters.isMaster(event.getAuthor())); //Wouldn't want anyone crashing the server due to an unstable build
        }
    }

    private String getPrefix(MessageReceivedEvent event){
        String message = event.getMessage().getContent();
        if(message.startsWith(Command.PREFIXES.NORMAL.PREFIX)){
            return Command.PREFIXES.NORMAL.PREFIX;
        }else if(message.charAt(0) == '§'){
            return Command.PREFIXES.MASTER.PREFIX;
        }else if(message.startsWith(Command.PREFIXES.MODERATOR.PREFIX)){
            return Command.PREFIXES.MODERATOR.PREFIX;
        } else return "?";
    }

    private void dLog(String msg){
        if(Main.DEBUG) Log.log(DEBUG, msg);
    }
    private void vLog(String msg){
        if(Main.VERBOSE) Log.log(VERBOSE, msg);
    }

    /**
     * Runs every time a message is received
     * @param event here all the neccessary information is found
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        Log.LogGroup group = new Log.LogGroup();

        //Checks if the bot is supposed to react
        if(!shouldReact(event)) return;

        String prefix = getPrefix(event);

        if(!prefix.startsWith("?"))
            group.add("Recieved message starting with "+prefix+" from " + event.getAuthor().getName());

        if (Main.LOG_MESSAGES)
            Log.logMessage(event.getMessage(), event.getTextChannel());

        if(isValidKey(
                event.getMessage().getContent().replace(
                        prefix, ""
                ).split(" ")[0]
        )) {
            if (prefix.equals(Command.PREFIXES.NORMAL.PREFIX)) {
                handleCommand(PARSER.parse(event.getMessage().getContent().toLowerCase(),
                        getConfig(
                                event.getMessage().getContent().replace(
                                        Command.PREFIXES.NORMAL.PREFIX, ""
                                ).split(" ")[0]
                        ), event)
                );
            } else if (prefix.equals(Command.PREFIXES.MASTER.PREFIX)
                    && Masters.isMaster(event.getAuthor())
                    ) { // If it is a mastercommand
                handleCommand(PARSER.parse(
                        event.getMessage().getContent(),
                        getConfig(event.getMessage().getContent().replace(Command.PREFIXES.MASTER.PREFIX, "").split(" ")[0]),
                        event)
                );
            } else if(prefix.equals(Command.PREFIXES.MODERATOR.PREFIX)
                    && Moderators.isModerator(event.getAuthor())) {
                handleCommand(PARSER.parse(
                        event.getMessage().getContent(),
                        getConfig(event.getMessage().getContent().replace(Command.PREFIXES.MODERATOR.PREFIX, "").split(" ")[0]),
                        event)
                );
            }
        } else {
            specialCases(event);
        }
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        if(event.getGuild().getIdLong() != IDs.GUILDS.get(IDs.ID_KEY.GUILD_PHOENIX_II)) return;

        sendMessageToBotChannel("Welcome, pilot <@"+event.getMember().getUser().getIdLong()+"> to the Phoenix 2 community!");

        if(event.getGuild().getIdLong() == IDs.GUILDS.get(IDs.ID_KEY.GUILD_PHOENIX_II)) {
            event.getGuild().getController().
                    addRolesToMember(
                        event.getMember(),
                        event.getGuild().getRolesByName("pilot", true)
            ).complete();
        }
    }

    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        sendMessageToBotChannel(event.getMember().getUser().getName() + " has left the server");
    }

    /**
     * Executes the command if it is valid
     * @param cmd A container containing the details for the command
     */
    private void handleCommand(CommandContainer cmd){
        dLog(cmd.hrInfo());
        if(COMMANDS.containsKey(cmd.invoke)){
            boolean safe = COMMANDS.get(cmd.invoke).called(cmd.args, cmd.event);
            //dLog("\tExecuted = " + COMMANDS.get(cmd.invoke).called(cmd.args, cmd.event));

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

    public void sendMessageToBotChannel(String message){
        Main.JDA.getGuildById(325430508379176961L).getTextChannelById(378546862627749908L).sendMessage(message).complete();
    }
}
