package jn.rocbot.commands.testcommands;

import jn.rocbot.Main;
import jn.rocbot.commands.Command;
import jn.rocbot.commands.Master;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class HelloCommand implements Command {
    private final static String HELP = "Usage: !Hello";

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        boolean isMaster = false;
        for (Master master : Master.MASTERS) {
            isMaster = master.longID == event.getAuthor().getIdLong();

            if(isMaster) break;
        }

        if (isMaster) {
            event.getTextChannel().sendMessage(Master.fromLongID(event.getAuthor().getIdLong()).greeting).complete();
        } else {
            event.getTextChannel().sendMessage("Hello " + event.getAuthor().getName() + "!").complete();
        }
    }

    @Override
    public String help() {
        return HELP;
    }

    @Override
    public boolean executed(boolean success, MessageReceivedEvent event) {
        return true;
    }
}
