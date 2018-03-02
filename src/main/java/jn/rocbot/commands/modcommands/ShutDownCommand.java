package jn.rocbot.commands.modcommands;

import jn.rocbot.IDs;
import jn.rocbot.Main;
import jn.rocbot.commands.common.Command;
import jn.rocbot.commands.common.CommandConfig;
import jn.rocbot.commands.common.CommandType;
import jn.rocbot.commands.common.PREFIXES;
import jn.rocbot.utils.Log;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ShutDownCommand implements Command {
    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        event.getTextChannel().sendMessage("Shutting down; Bzzzt... z").complete();
        Log.log(Log.LogType.SEVERE, String.format("Bot shutdown by user %s", event.getAuthor().getName()));
        Main.JDA.shutdown();
        System.exit(1);
    }

    @Override
    public String help() {
        return String.format(
                "Usage: %sshutdown – shut downs the bot, for emergencies; to re-enable it, ask %s",
                PREFIXES.MODERATOR.PREFIX,
                Main.JDA.getUserById(IDs.JENS_USER_IDLONG).getName()
        );
    }

    private CommandConfig config = new CommandConfig(CommandType.MOD, true);

    @Override
    public CommandConfig getConfig() {
        return config;
    }
}
