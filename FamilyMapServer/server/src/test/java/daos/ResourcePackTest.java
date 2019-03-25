package daos;

import org.junit.Test;

/**
 * Created by eric on 2/22/19.
 */
public class ResourcePackTest {

    @Test
    public void printResources() {
//        System.out.println(ResourcePack.RES.femaleNames.toString());
//        System.out.println(ResourcePack.RES.maleNames.toString());
//        System.out.println(ResourcePack.lastNames.toString());
//        System.out.println(ResourcePack.locations.toString());
    }

    @Test
    public void printRandoms() {
        System.out.println("Female Name: " + ResourcePack.getRandomFemaleName());
        System.out.println("Male Name: " + ResourcePack.getRandomMaleName());
        System.out.println("Last Name: " + ResourcePack.getRandomLastName());
        System.out.println("Location: " + ResourcePack.getRandomLocation().toString());
    }

}