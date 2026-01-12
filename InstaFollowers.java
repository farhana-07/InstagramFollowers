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

           JSONArray followers = (JSONArray)parser.parse(new FileReader("C:\\Users\\farha\\OneDrive\\Documents\\SideProjects\\Instagram\\Info\\connections\\followers_and_following\\followers_1.json"));
           Object following = parser.parse(new FileReader("C:\\Users\\farha\\OneDrive\\Documents\\SideProjects\\Instagram\\Info\\connections\\followers_and_following\\following.json"));

           HashSet<String> userNamesFollowers = getUsersFR(followers);
           HashSet<String> userNamesFollowing = getUsersFG(unwrap(following));
           
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
            writeToFile("C:\\Users\\farha\\OneDrive\\Documents\\SideProjects\\Instagram\\Result\\not_following_me.txt", not);
            writeToFile("C:\\Users\\farha\\OneDrive\\Documents\\SideProjects\\Instagram\\Result\\mutuals.txt", mutuals);
            writeToFile("C:\\Users\\farha\\OneDrive\\Documents\\SideProjects\\Instagram\\Result\\myfans.txt", fans);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("File not found");
        }
        ;
    
    }
    public static HashSet<String> getUsersFR(JSONArray f) 
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

    public static HashSet<String> getUsersFG(JSONArray f) 
    {
        HashSet<String> users = new HashSet<String>();
        for(JSONObject follower: (Iterable<JSONObject>) f)
        {
            String s = (String) follower.get("title");
            users.add(s);
        }
        return users;
    }

    private static JSONArray unwrap(Object root) 
    {
        return (JSONArray) ((JSONObject) root).get("relationships_following");
    }

    public static void writeToFile(String filename, HashSet<String> content) 
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) 
        {
            for(String line : content) 
            {
                writer.write(line);
                writer.newLine();
            }
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
}