import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static List<String> extractLinks(String url){
        //System.out.println("inside extractLinks");
        List<String> links = new ArrayList<>();
        try {
            URL website = new URL(url);
            BufferedReader input = new BufferedReader(new InputStreamReader(website.openStream()));
            String inputLine;
            Pattern pattern = Pattern.compile("href=\"(http[s]?://[^\"]+)\"");
            while((inputLine = input.readLine())!=null){
                Matcher matcher = pattern.matcher(inputLine);
                while(matcher.find()){
                    links.add(matcher.group(1));
                }
            }
            input.close();
        } catch (Exception e){
            System.out.println("Failed to fetch links from "+url);
        }
        return links;
    }
}
