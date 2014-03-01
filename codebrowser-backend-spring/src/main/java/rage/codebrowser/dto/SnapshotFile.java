package rage.codebrowser.dto;

import rage.codebrowser.codeanalyzer.domain.DiffList;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.persistence.Entity;

@Entity
public class SnapshotFile extends AbstractNamedPersistable {

    @JsonIgnore
    private String filepath;
    private long filesize;
    private DiffList diffs;

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    @JsonIgnore
    public String getContent() throws FileNotFoundException {
        Scanner sc = new Scanner(new File(getFilepath()));
        StringBuilder res = new StringBuilder();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.toLowerCase().contains("author")) {
                continue;
            }
            res.append(line).append("\n");
        }

        return res.toString();
    }
    /**
     * @return the filesize
     */
    public long getFilesize() {
        return filesize;
    }

    /**
     * @param filesize the filesize to set
     */
    public void setFilesize(long filesize) {
        this.filesize = filesize;
    }

    /**
     * @return the diffs
     */
    public DiffList getDiffs() {
        return diffs;
    }

    /**
     * @param diffs the diffs to set
     */
    public void setDiffs(DiffList diffs) {
        this.diffs = diffs;
    }
}