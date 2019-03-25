package daos;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

import models.Location;

/**
 * Created by eric on 2/22/19.
 */

public class ResourcePack {

    public static final ArrayList<Location> locations;
    public static ArrayList<String>   maleNames;
    public static ArrayList<String>   femaleNames;
    public static ArrayList<String>   lastNames;

    static {

        locations = readLocations();
        maleNames = readStringArray("server/src/main/resources/json/mnames.json");
        femaleNames = readStringArray("server/src/main/resources/json/fnames.json");
        lastNames = readStringArray("server/src/main/resources/json/snames.json");

    }

    /**
     * @return ArrayList of location objects from locations.json
     */
    private static ArrayList<Location> readLocations() {

        ArrayList<Location> locs = new ArrayList<>();

        try {

            // read json file as one string
            File locationsFile = new File("server/src/main/resources/json/locations.json");
            Scanner scanner = new Scanner(locationsFile);
            String locationsJson = scanner.useDelimiter("\\A").next();

            // convert string to json array
            JSONArray jsonArray = null;
            jsonArray = new JSONArray(locationsJson);

            // convert json array to array list
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject location = jsonArray.getJSONObject(i);
                String country = location.getString("country");
                String city = location.getString("city");
                double latitude = location.getDouble("latitude");
                double longitude = location.getDouble("longitude");

                locs.add( new Location(
                        country,
                        city,
                        latitude,
                        longitude
                ) );
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return locs;

    }

    // return ArrayList of all strings in a json array from a JSON file
    private static ArrayList<String> readStringArray(String filePath) {

        ArrayList<String> toReturn = new ArrayList<>();

        try {

            // read json file as one string
            File locationsFile = new File(filePath);
            Scanner scanner = new Scanner(locationsFile);
            String locationsJson = scanner.useDelimiter("\\A").next();

            // convert string to json array
            JSONArray jsonArray = new JSONArray(locationsJson);

            // convert json array to array list
            for (int i = 0; i < jsonArray.length(); i++) {
                toReturn.add(jsonArray.getString(i));
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return toReturn;

    }

    public static Location getRandomLocation() {

        Random rand = new Random();
        int randLocationIndex = rand.nextInt(locations.size());
        return locations.get(randLocationIndex);

    }

    public static String getRandomMaleName() {

        Random rand = new Random();
        int randIndex = rand.nextInt(maleNames.size());
        return maleNames.get(randIndex);

    }

    public static String getRandomFemaleName() {

        Random rand = new Random();
        int randIndex = rand.nextInt(femaleNames.size());
        return femaleNames.get(randIndex);

    }

    public static String getRandomLastName() {

        Random rand = new Random();
        int randIndex = rand.nextInt(lastNames.size());
        return lastNames.get(randIndex);

    }

}
