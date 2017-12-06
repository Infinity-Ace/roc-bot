package jn.rocbot.commands.devcommands.tests;

import jn.rocbot.utils.Search;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.StringJoiner;

class SearchTestSub extends TestSubCommand {

    private TestCommand testCommand;

    public SearchTestSub(TestCommand testCommand) {
        this.testCommand = testCommand;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        sendResult(args, event);
    }

    public void sendResult(String[] args, MessageReceivedEvent event) {
        StringJoiner search = new StringJoiner(" ");

        for (int i = 2; i < args.length; i++) {
            search.add(args[i]);
        } switch (args[1]) {
            case "ship":
                testCommand.sendMessage(new Search().testShipSearch(search.toString()), event);
                break;
            case "aura":
                testCommand.sendMessage(new Search().testAuraSearch(search.toString()), event);
        }
    }

    @Override public String invoke() {
        return "search";
    }
}
