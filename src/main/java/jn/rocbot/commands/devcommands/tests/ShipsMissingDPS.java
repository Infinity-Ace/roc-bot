package jn.rocbot.commands.devcommands.tests;

import jn.rocbot.info.stores.ShipStore;
import jn.rocbot.ships.Ship;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.StringJoiner;

public class ShipsMissingDPS extends TestSubCommand{
    @Override
    public String invoke() {
        return "shipsMissingDPS";
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        StringJoiner ships = new StringJoiner(", ");
        for(Ship ship : ShipStore.SHIPS) {
            if((int) ship.weapon.dps == 0) ships.add(ship.name);
        }

        event.getTextChannel().sendMessage(ships.toString()).complete();
    }
}
