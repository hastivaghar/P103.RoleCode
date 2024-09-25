import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.io.File;

public class Backend implements BackendInterface {
    //fields
    private IterableSortedCollection<Song> tree;
    private Integer threshold;
    private List<Song> filteredList_speed;
    private List<Song> filteredList_year;
    private List<Song> filteredList_both;



    //constructor
    /**
     * Constructs a Backend instance that uses the specified tree to manage a collection of songs.
     * This class implements an IterableSortedCollection<Song>, which allows a tree to store and
     * organize Song objects in natural order. All methods below use the provided tree to store,
     * sort, and iterate through songs. This will enable creating some tests that use the
     * placeholder tree, and others that make use of a working tree.
     * @param tree - an IterableSortedCollection<Song> that stores and manages Song objects.
     */
    public Backend(IterableSortedCollection<Song> tree) {
        this.tree = tree;
        this.threshold = null;
    }
    @Override
    /**
     * Loads data from the .csv file referenced by filename.  You can rely
     * on the exact headers found in the provided songs.csv, but you should
     * not rely on them always being presented in this order or on there
     * not being additional columns describing other song qualities.
     * After reading songs from the file, the songs are inserted into
     * the tree passed to the constructor.
     * @param filename is the name of the csv file to load data from
     * @throws IOException when there is trouble finding/reading file
     */
    public void readData(String filename) throws IOException {
        File file = new File(filename);
        Scanner songsInfo = null;
        try {
            //reading a file
            songsInfo = new Scanner(file);
            //read the header of the file and separate the variables for each song
            String[] songVariables = songsInfo.nextLine().split(",");

            //getting the variables for EACH song in the list
            while (songsInfo.hasNextLine()) {
                //String[] songVariables = songsInfo.nextLine().split(",");
                // to check for lines that there are commas
                String line = songsInfo.nextLine();
                String title = null;
                String artist = null;
                String genre = null;
                int year = 0;
                int bpm = 0;
                int energy = 0;
                int danceability = 0;
                int loudness = 0;
                int liveness = 0;
                String [] partsOfSong;
                //determines how to split the line
                if (line.contains("\"")) {
                    partsOfSong = handlingCommasinLine(line);
                } else {
                    partsOfSong = line.split(",");
                }

                for (int i = 0; i < 10; i++) {
                    String currentVariable = songVariables[i].trim(); //gets the variable from header
                    switch (currentVariable) {
                        case "title":
                            title = partsOfSong[i].trim();
                            break;
                        case "artist":
                            artist = partsOfSong[i].trim();
                            break;
                        case "top genre":
                            genre = partsOfSong[i].trim();
                            break;
                        case "year":
                            year = Integer.parseInt(partsOfSong[i].trim());
                            break;
                        case "bpm":
                            bpm = Integer.parseInt(partsOfSong[i].trim());
                            break;
                        case "nrgy":
                            energy = Integer.parseInt(partsOfSong[i].trim());
                            break;
                        case "dnce":
                            danceability = Integer.parseInt(partsOfSong[i].trim());
                            break;
                        case "dB":
                            loudness = Integer.parseInt(partsOfSong[i].trim());
                            break;
                        case "live":
                            liveness = Integer.parseInt(partsOfSong[i].trim());
                            break;
                    }
                }
                    //create a new song object and add it to the tree
                    Song newSong = new Song(title, artist, genre, year, bpm, energy, danceability, loudness, liveness, bpmComparator);
                    tree.insert(newSong);
                }
        }
        catch (FileNotFoundException e) {
            throw new IOException("Couldn't find or read the file: " + filename);
        }
        //this one is specifically for the part where we do parseInt and it tells us if
        //a line has incorrect ordering
        catch (NumberFormatException e) {
            throw new IOException("Format of the variables of the song is incorrect");
        }
    }

    private String [] handlingCommasinLine(String line) {
        String [] partsOfSong = new String[10];
        StringBuilder currentPart = new StringBuilder();
        int quoteCount = 0;
        int i = 0;

        for (char character : line.toCharArray()) {
            if (character == '\"') {
                quoteCount++;
            }
            else if (character == ',' && quoteCount % 2 == 0) {
                if (i < partsOfSong.length) {
                    partsOfSong[i++] = currentPart.toString().trim();
                    currentPart.setLength(0); //to start a new attribute
                }
            }
            else {
                currentPart.append(character);
            }
        }
        //last part of line where no comma left
        if (currentPart.length() > 0 && i < partsOfSong.length) {
            partsOfSong[i++] = currentPart.toString().trim();
        }
        return partsOfSong;
    }

    Comparator<Song> bpmComparator = new Comparator<Song>() {
        @Override
        public int compare(Song o1, Song o2) {
            return Integer.compare(o1.getBPM(), o2.getBPM());
        }
    };

    /**
     * Retrieves a list of song titles from the tree passed to the contructor.
     * The songs should be ordered by the songs' Speed, and that fall within
     * the specified range of Speed values.  This Speed range will
     * also be used by future calls to the setFilter and getmost Energetic methods.
     *
     * If a Year filter has been set using the setFilter method
     * below, then only songs that pass that filter should be included in the
     * list of titles returned by this method.
     *
     * When null is passed as either the low or high argument to this method,
     * that end of the range is understood to be unbounded.  For example, a null
     * high argument means that there is no maximum Speed to include in
     * the returned list.
     *
     * @param low is the minimum Speed of songs in the returned list
     * @param high is the maximum Speed of songs in the returned list
     * @return List of titles for all songs from low to high, or an empty
     *     list when no such songs have been loaded
     */
    @Override
    public List<String> getRange(Integer low, Integer high) {
        //DO I use the seIteratorMin and Max
        List<String> titles = new ArrayList<>();
        List<Song> tempFilteredList = new ArrayList<>();
        List<Song> tempFilteredList_both = new ArrayList<>();
        if (threshold != null) {
            for (Song song : filteredList_year) {
                if ((low == null || song.getBPM() >= low) && (high == null || song.getBPM() <= high)) {
                    tempFilteredList_both.add(song);
                    tempFilteredList_both.sort(bpmComparator);
                    titles.add((song.getTitle()));
                }
            }
        }
        else {
            for (Song song : tree) {
                if ((low == null || song.getBPM() >= low) && (high == null || song.getBPM() <= high)) {
                    tempFilteredList.add(song);
                    tempFilteredList.sort(bpmComparator);
                    titles.add(song.getTitle());
                }
            }
        }
        this.filteredList_both = tempFilteredList_both;
        this.filteredList_speed = tempFilteredList;
        return titles;
    }

    /**
     * Retrieves a list of song titles that have a Year that is
     * larger than the specified threshold.  Similar to the getRange
     * method: this list of song titles should be ordered by the songs'
     * Speed, and should only include songs that fall within the specified
     * range of Speed values that was established by the most recent call
     * to getRange.  If getRange has not previously been called, then no low
     * or high Speed bound should be used.  The filter set by this method
     * will be used by future calls to the getRange and getmost Energetic methods.
     *
     * When null is passed as the threshold to this method, then no Year
     * threshold should be used.  This effectively clears the filter.
     *
     * @param threshold filters returned song titles to only include songs that
     *     have a Year that is larger than this threshold.
     * @return List of titles for songs that meet this filter requirement, or
     *     an empty list when no such songs have been loaded
     */
    @Override
    public List<String> setFilter(Integer threshold) {
        this.threshold = threshold;
        List<String> titles = new ArrayList<>();
        List<Song> tempFilteredList = new ArrayList<>();
        List<Song> tempFilteredList_both = new ArrayList<>();
        if (filteredList_speed == null) {
            for (Song song : tree) {
                if ((threshold == null || song.getYear() > threshold)) {
                    tempFilteredList.add(song);
                    tempFilteredList.sort(bpmComparator);
                    titles.add(song.getTitle());
                }
            }
        }
        else {
            for (Song song : filteredList_speed) {
                if ((threshold == null || song.getYear() > threshold)) {
                    tempFilteredList_both.add(song);
                    tempFilteredList_both.sort(bpmComparator);
                    titles.add(song.getTitle());
                }
            }
        }
        this.filteredList_both = tempFilteredList_both;
        this.filteredList_year = tempFilteredList;
        return titles;
    }


    Comparator<Song> energyComparator = new Comparator<Song>() {
        @Override
        public int compare(Song o1, Song o2) {
            return Integer.compare(o1.getEnergy(), o2.getEnergy());
        }
    };

    /**
     * This method returns a list of song titles representing the five
     * most Energetic songs that both fall within any attribute range specified
     * by the most recent call to getRange, and conform to any filter set by
     * the most recent call to setFilter.  The order of the song titles in this
     * returned list is up to you.
     *
     * If fewer than five such songs exist, return all of them.  And return an
     * empty list when there are no such songs.
     *
     * @return List of five most Energetic song titles
     */
    @Override
    public List<String> fiveMost() {
        List<String> titles = new ArrayList<>();
        List<Song> tempFilteredList_both = filteredList_both;
        if (filteredList_year != null && filteredList_speed != null) {
            tempFilteredList_both.sort(energyComparator);
//            for (Song song : tempFilteredList_both) {
//                te
//                //filteredList_both.sort(energyComparator);
//            }
            this.filteredList_both = tempFilteredList_both;
            for (int i = 0; i < Math.min(5, filteredList_both.size()); i++) {
                titles.add(filteredList_both.get(i).getTitle());
            }
        }
        return titles;
    }
}
