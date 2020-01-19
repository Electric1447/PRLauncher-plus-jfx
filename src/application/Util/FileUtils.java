package application.Util;

import java.io.File;

public class FileUtils {

    public static File[] findFilesWithExt(String dirName, String extension){
        File dir = new File(dirName);
        return dir.listFiles((dir1, filename) -> filename.endsWith(extension));
    }

}
