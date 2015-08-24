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

    public Dictionary(String filename){
        try {
            String dir = System.getProperty("user.dir");
            if (dir.indexOf("out") == -1)
                dir += "\\src";

            FileReader fileReader = new FileReader(dir+"\\" + filename);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            ArrayList<String> temp = new ArrayList<>();

            String word;
            while ((word = bufferedReader.readLine()) != null){
                dictionary.add(word);

            }

        } catch (FileNotFoundException ex){
            System.out.println("file not found.");
        } catch (IOException ex){
            //System.out.println("Error reading file.");
        }
    }




    public boolean wordExists(String word){
        return dictionary.contains(word);
    }
}
