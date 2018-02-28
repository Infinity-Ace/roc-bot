package jn.rocbot.commands.modcommands;

import jn.rocbot.commands.common.*;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

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
        //SUB_COMMANDS.add(new AssignRoleSub());
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
        return String.format(
                "The roles command is used for managing roles\n" +
                "Usage:\n" +
                    "\t%sroles  –  gives you a list of the current roles\n" +
                    "\t%sroles delete (<role-name>) -  deletes the specified role\n" +
                    "\tThe delete command is by default ignoring case, append \"!\" at the beginning of the role-name to make it case-sensitive\n" +
                    "\t%sroles create (<role-name>) -  creates role with specified name\n" +
                    "\t%sroles assign ( <username/nickname/ID> | <role-name>...)",
                PREFIXES.MODERATOR.PREFIX,
                PREFIXES.MODERATOR.PREFIX,
                PREFIXES.MODERATOR.PREFIX,
                PREFIXES.MODERATOR.PREFIX
        );
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
            }

            StringJoiner joined = new StringJoiner(" ");
            for(String string : args) joined.add(string);

            event.getGuild()
                .getController().createRole()
                    .setName(joined.toString())
                    .setColor(Color.GRAY)
                    .complete();
        }

        @Override
        public String invoke() {
            return "create";
        }

        @Override
        public String help() {
            return "";
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
            return "";
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
                event.getTextChannel().sendMessage(
                        "The delete-command must have a role to delete specified\nMake sure you spell it correctly"
                ).complete();
                return;
            } else  if(roleList.size() > 1) {
                event.getTextChannel().sendMessage("An error where more than one role was found").complete();
                event.getTextChannel().sendMessage("Contact <@319178540388057089>").complete();
                return;
            }

            roleList.get(0).delete().complete();
            event.getTextChannel().sendMessage(String.format("Role %s deleted", joined)).complete();
        }
    }

    /*class AssignRoleSub implements SubCommand {
        @Override
        public void action(String[] args, MessageReceivedEvent event) {
            StringJoiner joined = new StringJoiner(" ");
            for(String string : args) joined.add(string);
            if(args.length == 0){
                event.getTextChannel().sendMessage("You must specify a user, and a role").complete();
                return;
            }

            List<Role> roleList;

            roleList = event.getGuild().getRolesByName(
                    joined.toString().replaceFirst("!", ""), !joined.toString().startsWith("!")
            );

            if(roleList.size() == 0) {
                event.getTextChannel().sendMessage(
                        "The assign-command must have a role to be assigned specified\nMake sure you spell it correctly"
                ).complete();
                return;
            } else  if(roleList.size() > 1) {
                event.getTextChannel().sendMessage("An error where more than one role was found").complete();
                event.getTextChannel().sendMessage("Contact <@319178540388057089>").complete();
                return;
            }



            event.
        }

        @Override
        public String invoke() {
            return "assign";
        }

        @Override
        public String help() {
            return "";
        }

        private CommandConfig config = new CommandConfig(CommandType.MOD, false);

        @Override
        public CommandConfig getConfig() {
            return config;
        }
    }*/
}
