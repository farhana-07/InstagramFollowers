package Instagram;

import java.io.*;
import java.util.*; 
import org.json.simple.*;
import org.json.simple.parser.*;

public class InstaFollowers 
{
    public static void main(String[] args) 
    {
        try {
            
           JSONParser parser = new JSONParser();

           Object followers = parser.parse(new FileReader("C:\\Users\\farha\\OneDrive\\Documents\\SideProjects\\Instagram\\Info\\connections\\followers_and_following\\followers_1.json"));
           Object following = parser.parse(new FileReader("C:\\Users\\farha\\OneDrive\\Documents\\SideProjects\\Instagram\\Info\\connections\\followers_and_following\\following.json"));

           HashSet<String> userNamesFollowers = getUsers(unwrap(followers));
           HashSet<String> userNamesFollowing = getUsers(unwrap(following));
           
           HashSet<String> not = new HashSet<>(); 
           HashSet<String> fans = new HashSet<>();
           HashSet<String> mutuals = new HashSet<>();
            
           for(String user: userNamesFollowers)
            {
                if(userNamesFollowing.contains(user))
                {
                    mutuals.add(user);
                }
                else
                {
                    fans.add(user);
                }
            }
            for(String user: userNamesFollowing)
            {
                if(!userNamesFollowers.contains(user))
                {
                    not.add(user);
                }
            }
            System.out.println(fans);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("File not found");
        }
        ;
    
    }

    public static HashSet<String> getUsers(JSONArray f) 
    {
        HashSet<String> users = new HashSet<String>();
        for(JSONObject follower: (Iterable<JSONObject>) f)
        {
            JSONArray data = (JSONArray) follower.get("string_list_data");
            for(JSONObject d: (Iterable<JSONObject>) data)
            {
                    String username = (String) d.get("value");
                    users.add(username);
            }
        }
        return users;
    }

    private static JSONArray unwrap(Object root) 
    {
        if (root instanceof JSONArray) 
        {
            return (JSONArray) root;
        }

        if (root instanceof JSONObject) 
        {
            JSONObject jo = (JSONObject) root;

            // Try common keys first (harmless if they don't exist)
            String[] commonKeys = {
                "relationships_followers",
                "relationships_following",
                "followers",
                "following"
            };

            for (String key : commonKeys) 
            {
                Object val = jo.get(key);
                if (val instanceof JSONArray) return (JSONArray) val;
            }

            // Fallback: return the first JSONArray value we find
            for (Object keyObj : jo.keySet()) 
            {
                Object val = jo.get(keyObj);
                if (val instanceof JSONArray) return (JSONArray) val;
            }

            throw new IllegalArgumentException("JSONObject root did not contain a JSONArray. Keys: " + jo.keySet());
        }

        throw new IllegalArgumentException("Unexpected JSON root type: " + root.getClass());
    }
}