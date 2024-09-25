import java.util.List;
import java.io.IOException;

/**
 * BackendInterface - CS400 Project 1: iSongly
 */
public interface BackendInterface {

    //public Backend(IterableSortedCollection<Song> tree)
    // Your constructor must have the signature above. All methods below must
    // use the provided tree to store, sort, and iterate through songs. This
    // will enable you to create some tests that use the placeholder tree, and
    // others that make use of a working tree.

    public void readData(String filename) throws IOException;

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
    public List<String> getRange(Integer low, Integer high);


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
    public List<String> setFilter(Integer threshold);

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
    public List<String> fiveMost();
}

