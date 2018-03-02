package jn.rocbot.utils;

import jn.rocbot.Main;
import jn.rocbot.IDs;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import org.joda.time.DateTime;

import java.io.IOException;
import java.util.StringJoiner;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Log {
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

    public enum LogType {
        INFO, DEBUG, ERROR, VERBOSE, CONNECTION
    }

    private static Logger log = Logger.getLogger(Log.class.getName());

    public static class LogGroup {
        private StringJoiner joined = new StringJoiner("\n");
        public void add(String string){
          joined.add(string);
        }

        public String get() {
            return joined.toString();
        }
    }

    public static void log(LogType logType, String message){
        logToControl(logType, message);
    }

    public static void logMessage(Message message, Channel from){
        Main.JDA.getGuildById(IDs.GUILDS.get(IDs.ID_KEY.GUILD_BOT_CC))
                .getTextChannelById(IDs.CHANNELS.get(IDs.ID_KEY.CHANNEL_GCC_LOGS_BOT_CHANNEL_RCWD))
                .sendMessage(
                        String.format("Received message from %s in `%s#%s`:", message.getAuthor().getName(), from.getGuild().getName(),from.getName())
                ).complete();

        Main.JDA.getGuildById(IDs.GUILDS.get(IDs.ID_KEY.GUILD_BOT_CC))
                .getTextChannelById(IDs.CHANNELS.get(IDs.ID_KEY.CHANNEL_GCC_LOGS_BOT_CHANNEL_RCWD))
                .sendMessage(
                        new MessageBuilder().appendCodeBlock(message.getRawContent(),null).build()
                ).complete();
    }

    private static void logToControl(LogType type, String message) throws UnsupportedOperationException {
        TextChannel channel;

        switch (type) {
            case INFO: log.log(Level.INFO, message);
                channel = Main.JDA.getGuildById(
                        IDs.GUILDS.get(IDs.ID_KEY.GUILD_BOT_CC)
                ).getTextChannelById(
                        IDs.CHANNELS.get(IDs.ID_KEY.CHANNEL_GCC_LOGS_INFO)
                ); break;
            case DEBUG: if(Main.DEBUG) log.log(Level.INFO, message);
                channel = Main.JDA.getGuildById(
                        IDs.GUILDS.get(IDs.ID_KEY.GUILD_BOT_CC)
                ).getTextChannelById(
                        IDs.CHANNELS.get(IDs.ID_KEY.CHANNEL_GCC_LOGS_DEBUG)
                ); break;
            case VERBOSE: if(Main.VERBOSE) log.log(Level.INFO, message);
                channel = Main.JDA.getGuildById(
                        IDs.GUILDS.get(IDs.ID_KEY.GUILD_BOT_CC)
                ).getTextChannelById(
                        IDs.CHANNELS.get(IDs.ID_KEY.CHANNEL_GCC_LOGS_VERBOSE)
                ); break;
            case CONNECTION: log.log(Level.INFO, message);
                channel = Main.JDA.getGuildById(
                        IDs.GUILDS.get(IDs.ID_KEY.GUILD_BOT_CC)
                ).getTextChannelById(
                        IDs.CHANNELS.get(IDs.ID_KEY.CHANNEL_GCC_LOGS_CONNECTION)
                ); break;
            case ERROR: log.log(Level.SEVERE, message);
                channel = Main.JDA.getGuildById(
                        IDs.GUILDS.get(IDs.ID_KEY.GUILD_BOT_CC)
                ).getTextChannelById(
                        IDs.CHANNELS.get(IDs.ID_KEY.CHANNEL_GCC_LOGS_ERRORS)
                ); break;
            default: log.log(Level.SEVERE, message);
                throw new UnsupportedOperationException(String.format("No supported handle for %s yet!", type.name()));
        }

        channel.sendMessage(String.format(
                "----------------------"
                + "---------------------"
                + "\nDate: %s", new DateTime().toString()) + "\n\t:\n"
                + message
        ).complete();
    }
}