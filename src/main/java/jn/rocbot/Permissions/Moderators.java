package jn.rocbot.permissions;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import jn.rocbot.IDs;
import jn.rocbot.Main;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.utils.PermissionUtil;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.StringJoiner;

public class Moderators {
    public static class Moderator{
        public String name;
        public String greeting;
        public Guild guild;
        public long longID;

        public Moderator(String name, long longID, Guild guild) {
            this.name = name;
            this.greeting = "Hello Moderator " + name;
            this.longID = longID;
            this.guild = guild;
        }

        public Moderator(String name, String greeting, long longID, Guild guild) {
            this.name = name;
            this.greeting = greeting;
            this.longID = longID;
            this.guild = guild;
        }
    }

    public static ArrayList<Moderator> MODERATORS;

    public static void init() throws FileNotFoundException {
        MODERATORS = new ArrayList<>();

        JsonParser parser = new JsonParser();
        JsonObject moderatorsJson = parser.parse(new JsonReader(
            new FileReader("meta/permissions.json")
        )).getAsJsonObject();

        JsonArray moderators = (JsonArray) moderatorsJson.get("moderators");

        for (JsonElement jsonElementModerator : moderators){
            JsonObject jsonModerator = jsonElementModerator.getAsJsonObject();

            if(!jsonModerator.keySet().contains("greeting")) {
                MODERATORS.add(new Moderator(
                        jsonModerator.get("name").getAsString(),
                        jsonModerator.get("longID").getAsLong(),
                        Main.JDA.getGuildById(IDs.GUILDS.get(IDs.ID_KEY.GUILD_PHOENIX_II))
                ));
            } else {
                MODERATORS.add(new Moderator(
                        jsonModerator.get("name").getAsString(),
                        jsonModerator.get("greeting").getAsString(),
                        jsonModerator.get("longID").getAsLong(),
                        Main.JDA.getGuildById(IDs.GUILDS.get(IDs.ID_KEY.GUILD_PHOENIX_II))
                ));
            }
        }
    }

    public static void secondInit(JDA jda) {
        jda.getGuilds().forEach((Guild guild) -> {
            for (Member member : guild.getMembers()) {
                if (contains(member.getUser().getIdLong())) continue;

                if (member.isOwner())
                    MODERATORS.add(new Moderator(member.getUser().getName(), member.getUser().getIdLong(), guild));

                // Anyone in the Rocbot CC automatically is bot moderator
                else if ((guild.getIdLong() == IDs.GUILDS.get(IDs.ID_KEY.GUILD_BOT_CC)) && guild.isMember(member.getUser()))
                    MODERATORS.add(new Moderator(member.getUser().getName(), member.getUser().getIdLong(), guild));

                else {
                    // If user is phoenix server mod
                    if ((guild.getIdLong() == IDs.GUILDS.get(IDs.ID_KEY.GUILD_PHOENIX_II))
                            && member.getRoles().contains(guild.getRoleById(IDs.GP2_MOD_ROLE_IDLONG))) {
                        MODERATORS.add(new Moderator(member.getUser().getName(), member.getUser().getIdLong(), guild));
                    }
                    // Check if user is mug's hangout mod
                    if ((guild.getIdLong() == IDs.GUILDS.get(IDs.ID_KEY.GUILD_MUG_HANGOUT))
                            && member.getRoles().contains(guild.getRoleById(IDs.GMH_MOD_ROLE_IDLONG))) {
                        MODERATORS.add(new Moderator(member.getUser().getName(), member.getUser().getIdLong(), guild));
                    }
                }
            }
        });
    }

    public static boolean contains(long userLongId) {
        for (Moderator mod : MODERATORS)
            if(mod.longID == userLongId) return true;

        return false;
    }

    public static Moderator fromLongID(long id){
        for(Moderator moderator : MODERATORS){
            if(moderator.longID == id) return moderator;
        } return null;
    }

    public static boolean authorIsModerator(MessageReceivedEvent event){
        for(Moderator mod : MODERATORS){
            if((mod.longID == event.getAuthor().getIdLong()) && mod.guild == event.getGuild()) return true;
        } return false;
    }
}
