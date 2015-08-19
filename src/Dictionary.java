import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by NevilleVH on 2015-08-11.
 */
public class Dictionary {
    private HashSet<String> dictionary = new HashSet<>();

    public Dictionary(){
        try {
            String dir = System.getProperty("user.dir");
            if (dir.indexOf("src") == -1)
                dir += "\\src";

            FileReader fileReader = new FileReader(dir+"\\sowpods.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            ArrayList<String> temp = new ArrayList<>();

            String word;
            while ((word = bufferedReader.readLine()) != null){
                dictionary.add(word);

            }

        } catch (FileNotFoundException ex){
            System.out.println("sowpods.txt not found.");
        } catch (IOException ex){
            //System.out.println("Error reading file.");
        }
    }


    public HashSet<String> getDictionary(){
        return dictionary;
    }



    public boolean wordExists(String word){
        return dictionary.contains(word);
    }
}
