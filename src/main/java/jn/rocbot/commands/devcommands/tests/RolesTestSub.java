package jn.rocbot.commands.devcommands.tests;

import jn.rocbot.info.IDs;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.StringJoiner;

public class RolesTestSub extends TestSubCommand{

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        /*if(args.length >= 1){ StringJoiner roles = new StringJoiner(", ");
            for(Role role: event.getGuild().getRoles()) {
                role.getManager().revokePermissions(Permission.MESSAGE_WRITE);
                roles.add(role.getName().replace("@", "[at]"));
            }
            event.getTextChannel().sendMessageToBotChannel(roles.toString()).complete();
        }else{
            event.getTextChannel().sendMessageToBotChannel("Not valid arguments").complete();
        }*/
    }

    @Override
    public String invoke() {
        return "roles";
    }
}
