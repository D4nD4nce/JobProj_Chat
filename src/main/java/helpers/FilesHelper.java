package helpers;

import java.io.File;

public class FilesHelper {

    // check if file exists
    public static boolean CheckFileExists(String file) {
        File f = new File(file);
        return f.exists() && !f.isDirectory();
    }
}
