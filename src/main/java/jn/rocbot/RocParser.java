package jn.rocbot;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;

public class RocParser {
    public CommandContainer parse(String rw, MessageReceivedEvent event){

        ArrayList<String> split = new ArrayList<>();
        String raw = rw;
        String beheaded = raw.replaceFirst("!", "");
        String[] sb = beheaded.split(" ");
        for(String s : sb) split.add(s);

        String invoke = split.get(0);
        String args[] = new String[split.size() - 1];
        split.subList(1, split.size()).toArray(args);

        return new CommandContainer(raw, beheaded, sb, invoke, args, event);
    }

    public class CommandContainer{
        public final String raw;
        public final String beheaded;
        public final String[] splitBeheaded;
        public final String invoke;
        public final String[] args;
        public final MessageReceivedEvent event;

        public CommandContainer(String raw, String beheaded, String[] splitBeheaded,
                                String invoke, String[] args, MessageReceivedEvent event){

            this.raw = raw;
            this.beheaded = beheaded;
            this.splitBeheaded = splitBeheaded;
            this.invoke = invoke;
            this.args = args;
            this.event = event;
        }


        public String hrInfo(){
            return "Commandreceived:"
                    + "\n\tInvoke: " + invoke
                    + "\n\tRaw: " + raw
                    + "\n\tBeheaded: " + beheaded
                    + "\n\tSplitBeheaded: " + Arrays.toString(splitBeheaded)
                    + "\n\targs: " + Arrays.toString(args);
        }
    }
}
