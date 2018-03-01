package jn.rocbot.commands.normalcommands.withsubcommand;


import jn.rocbot.Main;
import jn.rocbot.commands.common.CommandConfig;
import jn.rocbot.commands.common.CommandType;
import jn.rocbot.commands.common.SubCommand;
import jn.rocbot.info.ListAble;
import jn.rocbot.misc.NotFoundException;
import jn.rocbot.ships.Ship;
import jn.rocbot.ships.ShipPropertyType;
import jn.rocbot.utils.Log;
import jn.rocbot.utils.Search;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import javax.naming.OperationNotSupportedException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringJoiner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WithSubCommand implements SubCommand {
    private CALLER caller;

    static final int LOWEST_MATCH_RATIO = 60;

    public WithSubCommand(CALLER caller) {
        this.caller = caller;
    }

    @Override
    public String invoke() {
        return "with";
    }

    public enum CALLER {
        Ships, Auras
    }

    public WithFilter parseFilter(String[] args) throws NotFoundException {
        return parseFilter(args, this.caller);
    }

    public WithFilter parseFilter(String[] args, CALLER caller) throws NotFoundException {
        switch (caller) {
            case Ships:
                return getShipsFilter(args);
            case Auras:
                break;
            default:
                try {
                    throw new OperationNotSupportedException(String.format("the with command does not support %s yet", caller.name()));
                } catch (OperationNotSupportedException e) {
                    e.printStackTrace();
                }
        } return null; //Hopefully it never gets here
    }

    private static Logger log = Logger.getLogger(Log.class.getName());

    private void dLog(String msg){
        if(Main.DEBUG) log.log(Level.INFO, msg);
    }

    private WithFilter getShipsFilter(String[] passed_args) throws NotFoundException {
        ArrayList<WithProperty> properties = new ArrayList<>();

        StringJoiner passed = new StringJoiner(" ");
        for (String arg : passed_args) passed.add(arg);

        dLog("Filtering ships, received" +
                "\n\targs: " + Arrays.toString(passed_args) +
                "\n\tJoined: " + passed.toString() +
                "\n\tType: " + ShipPropertyType.getShipPropertyType(passed.toString()).name());

        if(!passed.toString().contains(",")) {

            try { properties.add(WithProperty.createFrom(passed.toString())); }
            catch (NotFoundException ignored) { }

            return new WithFilter(properties.toArray(new WithProperty[properties.size()]));

        } else {
            String[] passed_properties = passed.toString().split(", ");
            for(String property : passed_properties){
                if(!property.isEmpty())
                    properties.add(WithProperty.createFrom(property));

            } return new WithFilter(properties.toArray(new WithProperty[properties.size()]));
        }
    }

    public ListAble getWith(WithFilter filter, MessageReceivedEvent event) {
        return getWith(filter, this.caller, event);
    }

    public ListAble getWith(WithFilter filter, CALLER caller, MessageReceivedEvent event) {
        switch (caller){
            case Ships:
                Ship[] results =  Search.shipsWith(filter);
                log.log(Level.INFO, filter.hrInfo());
                if(filter.properties.length > 0) {
                    StringJoiner filtered = new StringJoiner(", ");

                    for (Ship ship : results)
                        filtered.add(ship.name);

                    event.getTextChannel().sendMessage(filtered.toString()).complete();
                } else {
                    event.getTextChannel().sendMessage("Those are not valid arguments").complete();
                } break;

            case Auras:
                break;
        } return null;
    }

    @Override
    public String help() {
        switch (caller) {
            case Ships:
                return "\t!ships with VL  –  returns a list with all ships with Vorpal Lance";
            case Auras:
                return "\t!auras with damage > 20";
            default:
                try {
                    throw new OperationNotSupportedException(
                        String.format("No help for caller %s yet", caller.name())
                    );
                } catch (OperationNotSupportedException e) {
                    e.printStackTrace();
                }
        } return null; //Hopefully things never gets here
    }

    private CommandConfig config = new CommandConfig(CommandType.NORMAL, true);

    @Override
    public CommandConfig getConfig() {
        return config;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {

    }

    private  <T> T[] concatenate (T[] a, T[] b) {
        int aLength = a.length;
        int bLength = b.length;

        @SuppressWarnings("unchecked")
        T[] c = (T[]) Array.newInstance(a.getClass().getComponentType(), aLength+bLength);

        System.arraycopy(a, 0, c, 0, aLength);
        System.arraycopy(b, 0, c, aLength, bLength);

        return c;
    }
}
