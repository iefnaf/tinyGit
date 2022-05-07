package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import static gitlet.Utils.*;

/**
 *  Represents a gitlet commit object.
 *
 *  Every commit contains:
 *      A date.
 *      A commit message.
 *      A parent reference.
 *      A second parent reference. (for merges)
 *      A mapping of file names to blob references
 *
 * @author gfanfei@gmail.com
 * @date 2022/5/6 20:29
 */
public class Commit implements Serializable {

    /** The directory that contains all commit files. */
    static final File COMMIT_FOLDER = join(Repository.GITLET_DIR, "commits");

    /** The message of this Commit. */
    private final String message;

    /** The date of this Commit. */
    private final Date date;

    /** First parent's file name. */
    private final String firstParent;

    /**
     * Second parent's file name.
     * Used for merges, Gitlet only allow us to merge two branches.
     */
    private final String secondParent;

    /** A map contains all files and their versions. */
    private final Map<String, String> map;

    public Commit(String message, Date date, String firstParent,
                  String secondParent, Map<String, String> map) {
        this.message = message;
        this.date = date;
        this.firstParent = firstParent;
        this.secondParent = secondParent;
        this.map = map;
    }

    /** Return a copy of the map. */
    public Map<String, String> getMap() {
        return map;
    }

    public Date getDate() {
        return date;
    }

    public String getFirstParent() {
        return firstParent;
    }

    public String getSecondParent() {
        return secondParent;
    }

    public String getMessage() {
        return message;
    }

    /** Return whether the specified commit exists. */
    public static boolean commitExists(String commitId) {
        if (commitId == null || Utils.isBlankString(commitId)) {
            return false;
        }
        File commitF = join(COMMIT_FOLDER, commitId);
        return commitF.exists();
    }

    /** Return the specified commit object. */
    public static Commit fromFile(String commitId) {
        File commitF = join(COMMIT_FOLDER, commitId);
        return readObject(commitF, Commit.class);
    }

    /**
     * Serialize the commit and write it to a file.
     * @return the file's name.
     */
    public static String saveCommit(Commit commit) {
        String fileName = sha1(serialize(commit));
        File commitF = join(COMMIT_FOLDER, fileName);
        writeObject(commitF, commit);
        return fileName;
    }

    /** Return the id of all commits. */
    public static List<String> getAllCommitIds() {
        List<String> commitIds =  plainFilenamesIn(COMMIT_FOLDER);
        if (commitIds == null) {
            return new ArrayList<>();
        }
        return commitIds;
    }
}
