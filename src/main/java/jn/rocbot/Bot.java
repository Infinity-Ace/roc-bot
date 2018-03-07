package jn.rocbot;

import jn.rocbot.commands.common.PREFIXES;
import jn.rocbot.permissions.Masters;
import jn.rocbot.CommandParser.CommandContainer;
import jn.rocbot.commands.common.CommandConfig;
import jn.rocbot.permissions.Moderators;
import jn.rocbot.utils.Log;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.impl.EmoteImpl;
import net.dv8tion.jda.core.entities.impl.GuildImpl;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.react.GenericGuildMessageReactionEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.joda.time.DateTime;

import java.util.Random;
import java.util.StringJoiner;
import java.util.logging.Logger;

import static jn.rocbot.IDs.CHANNELS;
import static jn.rocbot.utils.Log.LogType.*;
import static jn.rocbot.commands.Commands.COMMANDS;

public class Bot extends ListenerAdapter {
    public static boolean READY = false;

    private final Random r = new Random();

    private static boolean IS_EVIL_TEST_TWIN;

    public Bot(boolean is_evil_twin){
        Bot.IS_EVIL_TEST_TWIN = is_evil_twin;
    }

    public static CommandParser PARSER;
    
    private Logger log = Logger.getLogger(Log.class.getName());

    static {
        PARSER = new CommandParser();
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
            String raw = event.getMessage().getContent().toLowerCase();

            //Some special cases -----------------------------------------------------
            if (raw.contains("name the bot")) {
                event.getTextChannel().sendMessage("No " + Emojis.EL).complete();
            } else if (raw.contains("thanks bot") || raw.contains("thank you bot")) {
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
            return (
                    (event.getTextChannel().getIdLong() == CHANNELS.get(IDs.ID_KEY.CHANNEL_GP2_BOT_CHANNEL)
                        ||
                        event.getTextChannel().getIdLong() == CHANNELS.get(IDs.ID_KEY.CHANNEL_GMH_BOT_INTERACT))
                            && !event.getMessage().getAuthor().isBot()
            );

        } else {
            return ((event.getTextChannel().getIdLong() == CHANNELS.get(IDs.ID_KEY.CHANNEL_GP2_BOT_CHANNEL)
                        ||
                        event.getTextChannel().getIdLong() == CHANNELS.get(IDs.ID_KEY.CHANNEL_GMH_BOT_INTERACT))
                            &&
                            !event.getMessage().getAuthor().isBot())
                                && // Wouldn't want anyone crashing the server due to an unstable build
                                Masters.isMaster(event.getAuthor());
        }
    }

    private String getPrefix(MessageReceivedEvent event){
        String message = event.getMessage().getContent();
        if(message.startsWith(PREFIXES.NORMAL.PREFIX)){
            return PREFIXES.NORMAL.PREFIX;

        }else if(message.startsWith(PREFIXES.MASTER.PREFIX)){
            return PREFIXES.MASTER.PREFIX;

        }else if(message.startsWith(PREFIXES.MODERATOR.PREFIX)){
            return PREFIXES.MODERATOR.PREFIX;
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
     * @param event here all the necessary information is found
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        Log.LogGroup group = new Log.LogGroup();

        if (event.getMessage().getContent().toLowerCase().contains(":el:")) {
            if(event.getGuild().getIdLong() == IDs.GUILDS.get(IDs.ID_KEY.GUILD_PHOENIX_II))
                event.getMessage().addReaction(new EmoteImpl(Emojis.EL_LONG, (GuildImpl) event.getGuild())).complete();
        }

        //Checks if the bot is supposed to react
        if(!shouldReact(event)) return;

        String prefix = getPrefix(event);

        if(!prefix.startsWith("?"))
            group.add("Received message starting with " + prefix + " from " + event.getAuthor().getName());

        if (Main.LOG_MESSAGES)
            Log.logMessage(event.getMessage(), event.getTextChannel());

        if(isValidKey(
                event.getMessage().getContent().replace(
                        prefix, ""
                ).split(" ")[0]
        )) {
            if (prefix.equals(PREFIXES.NORMAL.PREFIX)) {
                handleCommand(PARSER.parse(event.getMessage().getContent().toLowerCase(),
                        getConfig(
                                event.getMessage().getContent().replace(
                                        PREFIXES.NORMAL.PREFIX, ""
                                ).split(" ")[0]
                        ), event)
                );
            } else if (prefix.equals(PREFIXES.MASTER.PREFIX)
                    && Masters.isMaster(event.getAuthor())
                    ) { // If it is a mastercommand
                handleCommand(PARSER.parse(
                        event.getMessage().getContent(),
                        getConfig(
                            event.getMessage().getContent().replace(PREFIXES.MASTER.PREFIX, "").split(" ")[0]),
                            event
                        )
                );
            } else if(prefix.equals(PREFIXES.MODERATOR.PREFIX)
                    && Moderators.authorIsModerator(event)) {

                handleCommand(PARSER.parse(
                        event.getMessage().getContent(),
                        getConfig(
                            event.getMessage().getContent().replace(PREFIXES.MODERATOR.PREFIX, "").split(" ")[0]),
                            event
                        )
                );
            }
        } else {
            specialCases(event);
        }
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        if(event.getGuild().getIdLong() != IDs.GUILDS.get(IDs.ID_KEY.GUILD_PHOENIX_II)) return;

        event.getJDA().getTextChannelById(CHANNELS.get(IDs.ID_KEY.CHANNEL_GP2_WELCOME)).
                sendMessage("Welcome, pilot <@"+event.getMember().getUser().getIdLong()+"> to the Phoenix 2 community!")
                .complete();

        if(event.getGuild().getIdLong() == IDs.GUILDS.get(IDs.ID_KEY.GUILD_PHOENIX_II)) {
            event.getGuild().getController().
                    addRolesToMember(
                        event.getMember(),
                        event.getGuild().getRolesByName("pilot", true)
            ).complete();
        }
    }

    @Override
    public void onGenericGuildMessageReaction(GenericGuildMessageReactionEvent event) {
        if(event.getGuild() == event.getJDA().getGuildById(IDs.GUILDS.get(IDs.ID_KEY.GUILD_PHOENIX_II))) {
            if(event.getReaction().getReactionEmote().getIdLong() == Emojis.EL_LONG) {
                event.getChannel().getMessageById(event.getReaction().getMessageIdLong())
                        .queue(
                                message -> message.addReaction(
                                        new EmoteImpl(Emojis.EL_LONG, (GuildImpl) event.getGuild())
                                ).complete()
                        );
            }
        }

    }

    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        sendMessageToBotChannel(event.getMember().getUser().getName() + " has left the server");
    }

    @Override
    public void onReady(ReadyEvent event) {
        event.getJDA().getGuilds().forEach(
                (Guild g) -> g.getController()
                        .setNickname(g.getMemberById(IDs.BOT_USER_IDLONG), "Roc-Bot").complete()
        );
    }

    /**
     * Executes the command if it is valid
     * @param cmd A container containing the details for the command
     */
    private void handleCommand(CommandContainer cmd){
        dLog(cmd.hrInfo());
        if(COMMANDS.containsKey(cmd.invoke)){
            boolean safe = COMMANDS.get(cmd.invoke).called(cmd.args, cmd.event);
            //dLog("\tExecuted = " + COMMANDS.toString(cmd.invoke).called(cmd.args, cmd.event));

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
        } return null;
    }

    public void sendMessageToBotChannel(String message){
        Main.JDA.getGuildById(IDs.GUILDS.get(IDs.ID_KEY.GUILD_PHOENIX_II))
                .getTextChannelById(IDs.CHANNELS.get(IDs.ID_KEY.CHANNEL_GP2_BOT_CHANNEL))
                        .sendMessage(message).complete();
    }

    public void sendMessageToAnnouncementsChannel(String message) {
        Main.JDA.getGuildById(IDs.GUILDS.get(IDs.ID_KEY.GUILD_PHOENIX_II))
                .getTextChannelById(IDs.CHANNELS.get(IDs.ID_KEY.CHANNEL_GP2_ANNOUNCEMENTS))
                        .sendMessage(message).complete();
    }
}
