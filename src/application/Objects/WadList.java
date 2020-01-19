package application.Objects;

import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.lang.reflect.Array;
import java.util.Arrays;

import static application.Util.FileUtils.findFilesWithExt;

public class WadList {

    private final String[] wadsFilenameDB = new String[] {"doom.wad", "doom1.wad", "doomu.wad", "doom2.wad", "plutonia.wad", "tnt.wad"};
    private final String[] wadsTitleDB = new String[] {"Doom / Ultimate Doom", "Doom", "The Ultimate Doom", "Doom II: Hell on Earth", "The Plutonia Experiment", "TNT: Evilution"};

    private String[] wadsFilename;
    private String[] wadsTitle;

    public WadList () {

    }

    public void detectWads (String path) {

        File[] wads = findFilesWithExt(path, ".wad");

        if (wads == null) {
            this.wadsFilename = new String[] {""};
            this.wadsTitle = new String[] {"No iwads"};
            System.out.println("No iwads found");
            return;
        }

        if (wads.length == 0) {
            this.wadsFilename = new String[] {""};
            this.wadsTitle = new String[] {"No iwads"};
            System.out.println("No iwads found");
            return;
        }

        String[] tempWadFN = new String[wads.length];
        String[] tempWadT = new String[wads.length];

        String[] tempStr = Arrays.stream(wads).map(File::getName).toArray(String[]::new);
        Arrays.sort(tempStr);

        for (int i = 0; i < tempStr.length; i++) {
            if (ArrayUtils.contains(wadsFilenameDB, tempStr[i].toLowerCase())) {
                int index = ArrayUtils.indexOf(wadsFilenameDB, tempStr[i].toLowerCase());
                tempWadFN[i] = wadsFilenameDB[index];
                tempWadT[i] = wadsTitleDB[index];
            } else {
                tempWadFN[i] = tempStr[i];
                tempWadT[i] = tempStr[i];
            }
        }

        this.setWadsFilename(tempWadFN);
        this.setWadsTitle(tempWadT);
    }

    // Get & Set methods
    public String[] getWadsFilename () {
        return this.wadsFilename;
    }

    public void setWadsFilename (String[] wfn) {
        this.wadsFilename = wfn;
    }

    public String[] getWadsTitle () {
        return this.wadsTitle;
    }

    public void setWadsTitle (String[] wt) {
        this.wadsTitle = wt;
    }

    public String getWadFilename (int index) {
        return this.wadsFilename[index];
    }

    public void setWadFilename (String wfn, int index) {
        this.wadsFilename[index] = wfn;
    }

    public String getWadTitle (int index) {
        return this.wadsTitle[index];
    }

    public void setWadTitle (String wt, int index) {
        this.wadsTitle[index] = wt;
    }

    public String[] getWadsFullTitle () {
        String[] str = new String[this.wadsFilename.length];

        for (int i = 0; i < str.length; i++)
            str[i] = this.wadsTitle[i] + " (" + this.wadsFilename[i] + ")";

        return str;
    }

    public String getWadFullTitle (int index) {
        return (this.wadsTitle[index] + " (" + this.wadsFilename[index] + ")");
    }

}
