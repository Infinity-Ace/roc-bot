package jn.rocbot;

import jn.rocbot.commands.common.CommandType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;

public class RocParser {
    public CommandContainer parse(String rw, CommandType type, MessageReceivedEvent event){

        ArrayList<String> split = new ArrayList<>();
        String raw = rw;
        String beheaded = "";
        switch (type) {
            case NORMAL:
                beheaded = raw.replaceFirst("!", "");
                break;
            case MOD:
                beheaded = raw.replaceFirst("~!", "");
                break;
            case DEV:
                beheaded = raw.replaceFirst("!#", "");
                break;
        }

        String[] sb = beheaded.split(" ");
        for(String s : sb) split.add(s);




        String invoke = split.get(0);

        String args[] = new String[split.size() - 1];
        split.subList(1, split.size()).toArray(args);

        return new CommandContainer(raw, beheaded, sb, invoke, args, type, event);
    }

    public class CommandContainer{
        public final String raw;
        public final String beheaded;
        public final String[] splitBeheaded;
        public final String invoke;
        public final String[] args;
        public final CommandType type;
        public final MessageReceivedEvent event;

        public CommandContainer(String raw, String beheaded, String[] splitBeheaded,
                                String invoke, String[] args, CommandType type, MessageReceivedEvent event){
            this.raw = raw;
            this.beheaded = beheaded;
            this.splitBeheaded = splitBeheaded;
            this.invoke = invoke;
            this.args = args;
            this.type = type;
            this.event = event;
        }


        public String hrInfo(){
            return "Commandreceived:"
                    + "\n\tInvoke: " + invoke
                    + "\n\tRaw: " + raw
                    + "\n\tCommandType: " + type.stringName
                    + "\n\tBeheaded: " + beheaded
                    + "\n\tSplitBeheaded: " + Arrays.toString(splitBeheaded)
                    + "\n\targs: " + Arrays.toString(args);
        }
    }
}
