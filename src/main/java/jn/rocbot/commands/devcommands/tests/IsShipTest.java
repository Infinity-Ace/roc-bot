package jn.rocbot.commands.devcommands.tests;

import jn.rocbot.info.stores.ShipStore;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.StringJoiner;

class IsShipTest extends TestSubCommand {
    private TestCommand testCommand;

    public void sendResult(String[] args, MessageReceivedEvent event) {
        StringJoiner ship = new StringJoiner(" ");
        for (String arg : args) if (!args.equals("extra")) ship.add(arg);
        testCommand.sendMessage(ship.toString() + " isShip: " + ShipStore.isShip(ship.toString()), event);
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        sendResult(args, event);
    }

    public IsShipTest(TestCommand testCommand){
        super(false);
        this.testCommand = testCommand;
    }

    @Override public String invoke() {
        return "isShip";
    }
}
