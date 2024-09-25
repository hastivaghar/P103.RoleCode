import java.io.IOException;
import java.util.List;

public class BackendTests {
    //test getRange without prior filtering
    public boolean roleTest1() {
        Tree_Placeholder tree = new Tree_Placeholder();
        Backend backend = new Backend(tree);
        List <String> titles = backend.getRange(100, 150);
        List <String> expectedTitles = List.of("A L I E N S", "BO$$", "Cake By The Ocean");
        if (!titles.equals(expectedTitles)) {
            return false;
        }

        //testing setFilter after getRange() works
        List <String> titles_moreFilter = backend.setFilter(2016);
        List <String> expectedTitles_moreFilter = List.of("A L I E N S");
        if (!titles_moreFilter.equals(expectedTitles_moreFilter)) {
            return false;
        }
        return true;
    }

    //tests setFilter without prior filtering
    public boolean roleTest2() {
        Tree_Placeholder tree = new Tree_Placeholder();
        Backend backend = new Backend(tree);
        List <String> titles = backend.setFilter(2015);
        List <String> expectedTitles = List.of("A L I E N S", "Cake By The Ocean");
        if (!titles.equals(expectedTitles)) {
            return false;
        }
        //testing if getRole works after setFilter
        List <String> titles_moreFilter = backend.getRange(100, 120);
        List <String> expectedTitles_moreFilter = List.of("Cake By The Ocean");
        if (!titles_moreFilter.equals(expectedTitles_moreFilter)) {
            return false;
        }

        return true;
    }

    //testing fiveMost() when neither filters are called
    public boolean roleTest3() {
        Tree_Placeholder tree = new Tree_Placeholder();
        Backend backend = new Backend(tree);
        List <String> titles = backend.fiveMost();
        List <String> expectedTitles = List.of("");
        if (!titles.isEmpty() && !titles.equals(expectedTitles)) {
            return false;
        }
        //testing fiveMost() after setFiler and getRange filters are called
        backend.getRange(100, 150);
        backend.setFilter(2015);
        List <String> titles_moreFilter = backend.fiveMost();
        List <String> expectedTitles_moreFilter = List.of("Cake By The Ocean", "A L I E N S");
        if (!titles_moreFilter.equals(expectedTitles_moreFilter)) {
            return false;
        }
        return true;
    }

    public boolean roleTest4() {
        Tree_Placeholder tree = new Tree_Placeholder();
        Backend backend = new Backend(tree);
        Song song_chainsmokers = new Song("Kills You Slowly","The Chainsmokers","electropop",2019,150,44,70,-9,13, null);
        try {
            backend.readData("Songs.csv");
            if (!tree.contains(song_chainsmokers)) {
                return false;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return true;
    }


    public static void main(String args[]) {
        BackendTests tests = new BackendTests();
        System.out.println("Test 1: " + tests.roleTest1());
        System.out.println("Test 2: " + tests.roleTest2());
        System.out.println("Test 3: " + tests.roleTest3());
        System.out.println("Test 4: " + tests.roleTest4());
    }

}
