package jn.rocbot;

import jn.rocbot.commands.common.CommandConfig;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;

public class RocParser {
    public CommandContainer parse(String rw, CommandConfig config, MessageReceivedEvent event){
        ArrayList<String> split = new ArrayList<>();
        String raw;

        if(config.ignoreCase)
            raw = rw.toLowerCase();
        else
            raw = rw;

        String beheaded = "";

        switch (config.type) {
            case NORMAL:
                beheaded = raw.replaceFirst("!", "");
                break;
            case MOD:
                beheaded = raw.replaceFirst("~", "");
                beheaded = beheaded.replaceFirst("!", "");
                break;
            case DEV:
                beheaded = raw.replaceFirst("§", "");
                break;
        }

        String[] sb = beheaded.split(" ");
        for(String s : sb) split.add(s);

        String invoke = split.get(0);

        String args[] = new String[split.size() - 1];
        split.subList(1, split.size()).toArray(args);

        return new CommandContainer(raw, beheaded, sb, invoke, args, config, event);
    }

    public class CommandContainer{
        public final String raw;
        public final String beheaded;
        public final String[] splitBeheaded;
        public final String invoke;
        public final String[] args;
        public final CommandConfig config;
        public final MessageReceivedEvent event;

        public CommandContainer(String raw, String beheaded, String[] splitBeheaded,
                                String invoke, String[] args, CommandConfig config, MessageReceivedEvent event){
            this.raw = raw;
            this.beheaded = beheaded;
            this.splitBeheaded = splitBeheaded;
            this.invoke = invoke;
            this.args = args;
            this.config = config;
            this.event = event;
        }


        public String hrInfo(){
            return "Commandreceived:"
                    + "\n\tInvoke: " + invoke
                    + "\n\tRaw: " + raw
                    + "\n\tCommandType: " + config.type.stringName
                    + "\n\tBeheaded: " + beheaded
                    + "\n\tSplitBeheaded: " + Arrays.toString(splitBeheaded)
                    + "\n\targs: " + Arrays.toString(args);
        }
    }
}
