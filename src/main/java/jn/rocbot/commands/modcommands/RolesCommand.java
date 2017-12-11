package jn.rocbot.commands.modcommands;

import jn.rocbot.commands.Commands;
import jn.rocbot.commands.common.Command;
import jn.rocbot.commands.common.CommandConfig;
import jn.rocbot.commands.common.CommandType;
import jn.rocbot.commands.common.SubCommand;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.requests.restaction.RoleAction;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public class RolesCommand implements Command{
    private final ArrayList<SubCommand> SUB_COMMANDS;

    public RolesCommand(){
        SUB_COMMANDS = new ArrayList<>();
        SUB_COMMANDS.add(new CreateRoleSub());
        SUB_COMMANDS.add(new DeleteRoleSub());
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if(args.length == 0){
           sendAllRoles(event);
        } else {
            for(SubCommand sub : SUB_COMMANDS)
                if(sub.isInvoke(args[0]))
                    sub.action(Arrays.copyOfRange(args, 1, args.length), event);
        }
    }

    private void sendAllRoles(MessageReceivedEvent event){
        StringJoiner roles = new StringJoiner(", ");
        for(Role role: event.getGuild().getRoles()){
            roles.add(role.getName().replace("@", "<at>"));
        } event.getTextChannel().sendMessage(roles.toString()).complete();
    }

    @Override
    public String help() {
        return "Blame Jens";
    }

    private CommandConfig config = new CommandConfig(CommandType.MOD, false);

    @Override
    public CommandConfig getConfig() {
        return config;
    }

    class CreateRoleSub implements SubCommand{
        @Override
        public void action(String[] args, MessageReceivedEvent event) {
            if(args.length == 0) {
                event.getTextChannel().sendMessage("You must at least specify a name!").complete();
                return;
            } event.getGuild()
                .getController().createRole()
                    .setName(args[0]).setColor(Color.GRAY).complete();
        }

        @Override
        public String invoke() {
            return "create";
        }

        @Override
        public String help() {
            return "Blame Jens once again";
        }

        private CommandConfig config = new CommandConfig(CommandType.MOD, false);

        @Override
        public CommandConfig getConfig() {
            return config;
        }
    }

    class DeleteRoleSub implements SubCommand {

        @Override
        public String invoke() {
            return "delete";
        }

        @Override
        public String help() {
            return String.format("Usage: %sroles <delete (rolename)>", PREFIXES.MODERATOR.PREFIX);
        }

        private CommandConfig config = new CommandConfig(CommandType.MOD, false);

        @Override
        public CommandConfig getConfig() {
            return config;
        }

        @Override
        public void action(String[] args, MessageReceivedEvent event) {
            StringJoiner joined = new StringJoiner(" ");
            for(String string : args) joined.add(string);
            if(args.length == 0){
                event.getTextChannel().sendMessage("No role to delete specified").complete();
                return;
            }

            List<Role> roleList;

            if(joined.toString().startsWith("!")) {
                roleList = event.getGuild().getRolesByName(
                    joined.toString().replaceFirst("!", ""), false
                );
            } else {
                roleList = event.getGuild().getRolesByName(
                        joined.toString(), true
                );
            }

            if(roleList.size() == 0) {
                event.getTextChannel().sendMessage("The delete-command must have a role to delete specified").complete();
                return;
            }

            if(roleList.size() > 1) {
                event.getTextChannel().sendMessage("An error where more than one role was found").complete();
                event.getTextChannel().sendMessage("Contact <@319178540388057089>").complete();
                //event.getTextChannel().sendMessage(String.format("%s is not a role", joined)).complete();
                return;
            }

            roleList.get(0).delete().complete();
            event.getTextChannel().sendMessage(String.format("Role %s deleted", joined)).complete();
        }
    }
}
