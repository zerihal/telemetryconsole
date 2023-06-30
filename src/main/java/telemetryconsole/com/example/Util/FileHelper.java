package telemetryconsole.com.example.Util;

import java.io.File;

public final class FileHelper {

    /**
     * Attempts to delete the specified file, returning true if deleted, otherwise false.
     * If the file does not exist or is not a file then there is nothing to delete and
     * this will just return true.
     */
    public static Boolean DeleteFile(String fileNameAndPath) {
        File file = new File(fileNameAndPath);

        if (!file.exists() || file.isDirectory())
            return true;

        if (file.delete()) {
            return true;
        } else {
            return false;
        }
    }
}
