package jn.rocbot.commands.common;

import jn.rocbot.Permissions.Masters;
import jn.rocbot.Permissions.Moderators;
import net.dv8tion.jda.core.entities.User;

import javax.naming.OperationNotSupportedException;

public enum CommandType {
    NORMAL("", "NORMAL"),
    MOD("mod", "MOD"),
    DEV("master", "DEV");

    private final String requiredPermission;
    public final String stringName;

    CommandType(String requiredPermission, String stringName){

        this.requiredPermission = requiredPermission;
        this.stringName = stringName;
    }

    public boolean hasPermission(CommandType commandType, User user){
        switch (commandType){
            case NORMAL:
                return true;
            case MOD:
                for (Moderators.Moderator moderator : Moderators.MODERATORS) if(user.getIdLong() == moderator.longID) return true;
                return false;
            case DEV:
                for (Masters.Master master : Masters.MASTERS) if(user.getIdLong() == master.longID) return true;
                return false;
        } try {
            throw new OperationNotSupportedException("No supported permission-check for " + commandType.stringName);
        } catch (OperationNotSupportedException e) {
            e.printStackTrace();
        } return false;
    }
}
