package rage.codebrowser.codeanalyzer.service;

import difflib.Chunk;
import difflib.Delta;
import difflib.DiffUtils;
import difflib.Patch;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rage.codebrowser.codeanalyzer.domain.Diff;
import rage.codebrowser.codeanalyzer.domain.DiffList;
import rage.codebrowser.repository.DiffListRepository;
import rage.codebrowser.repository.DiffRepository;

@Component
public class CountDiffs {

    @Autowired
    private DiffListRepository diffListRepository;
    @Autowired
    private DiffRepository diffRepository;
    /**
     * Keeps count of deleted lines
     */
    private int totalOffset;

    /**
     * @param previous filepath of previous file
     * @param current filepath of current file
     * @return List of Diffs containing differences between previous and current
     */
    public DiffList getDifferences(String previous, String current) {
        totalOffset = 0;
        DiffList diffs = new DiffList();
        List<String> currentLines = fileToLines(current);
        Patch patch = DiffUtils.diff(fileToLines(previous), currentLines);
        diffs.setDifferences(getDifferenceList(patch));
        diffs.setLines(currentLines.size());
        if (diffListRepository != null) {
            diffs = diffListRepository.save(diffs);
        }
        return diffs;
    }

    private List<String> fileToLines(String filename) {
        List<String> lines = new LinkedList<String>();
        if (filename == null) {
            return lines;
        }
        String line = "";
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(filename));
            while ((line = in.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return lines;
    }

    private String listToString(List<String> list) {
        String string = "";
        for (String string1 : list) {
            string += string1 + "\n";
        }
        return string;
    }

    private List<Diff> getDifferenceList(Patch patch) {
        List<Diff> diffs = new LinkedList<Diff>();

        for (Delta delta : patch.getDeltas()) {
            if ((delta.getType().toString()).equals("CHANGE")) {
                addChange(diffs, delta);
            } else if ((delta.getType().toString()).equals("DELETE")) {
                addDelete(diffs, delta);
            } else if ((delta.getType().toString()).equals("INSERT")) {
                addInsert(diffs, delta);
            }
        }
        return diffs;
    }

    private void addInsert(List<Diff> diffs, Delta delta) {
        Diff diff = new Diff();
        diff.setType("insert");
        diff.setRowStart(delta.getRevised().getPosition());
        diff.setRowEnd(delta.getRevised().getPosition() + delta.getRevised().size() - 1);
        diff.setOffset(totalOffset);
        if (diffRepository != null) {
            diff = diffRepository.save(diff);
        }
        diffs.add(diff);
        //totalOffset += (diff.getRowEnd()-diff.getRowStart()+1);
    }

    private void addDelete(List<Diff> diffs, Delta delta) {
        Diff diff = new Diff();
        diff.setType("delete");
        diff.setFromRowStart(delta.getOriginal().getPosition());
        diff.setFromRowEnd(delta.getOriginal().getPosition() + delta.getOriginal().size() - 1);
        diff.setRowStart(delta.getRevised().getPosition());
        diff.setRowEnd(delta.getRevised().getPosition() + delta.getOriginal().size() - 1);
        diff.setLines(listToString((List<String>) delta.getOriginal().getLines()));
        diff.setOffset(totalOffset);
        if (diffRepository != null) {
            diff = diffRepository.save(diff);
        }
        diffs.add(diff);
        totalOffset += (diff.getFromRowEnd() - diff.getFromRowStart() + 1);
    }

    private void addChange(List<Diff> diffs, Delta delta) {
        Chunk previous = delta.getOriginal();
        Chunk current = delta.getRevised();

        if (previous.size() == current.size()) {
            //Equal amount of lines replaced with equal amount of lines
            addReplace(previous, current, diffs);
        } else if (previous.size() > current.size()) {
            //Lines changed to lesser amount of lines -> replace & delete
            addReplaceAndDelete(previous, current, diffs);
        } else if (previous.size() < current.size()) {
            //Lines changed to more lines -> replace & insert
            addReplaceAndInsert(previous, current, diffs);
        }
    }

    private void addReplaceAndDelete(Chunk previous, Chunk current, List<Diff> diffs) {
        Diff diff = new Diff();
        diff.setType("replace");
        diff.setRowStart(current.getPosition());
        diff.setRowEnd(current.getPosition() + current.size() - 1);
        diff.setOffset(totalOffset);
        if (diffRepository != null) {
            diff = diffRepository.save(diff);
        }
        diffs.add(diff);

        diff = new Diff();
        diff.setType("delete");
        diff.setFromRowStart(previous.getPosition() + current.size());
        diff.setFromRowEnd(diff.getFromRowStart() + previous.size() - current.size() - 1);
        diff.setRowStart(current.getPosition() + current.size());
        diff.setRowEnd(current.getPosition() + previous.size() - 1);
        diff.setLines(listToString((List<String>) previous.getLines().subList(current.size() - 1, previous.size() - 1)));
        diff.setOffset(totalOffset);
        if (diffRepository != null) {
            diff = diffRepository.save(diff);
        }
        diffs.add(diff);
        totalOffset += (diff.getFromRowEnd() - diff.getFromRowStart() + 1);
    }

    private void addReplaceAndInsert(Chunk previous, Chunk current, List<Diff> diffs) {
        Diff diff = new Diff();
        diff.setType("replace");
        diff.setRowStart(current.getPosition());
        diff.setRowEnd(current.getPosition() + previous.size() - 1);
        diff.setOffset(totalOffset);
        if (diffRepository != null) {
            diff = diffRepository.save(diff);
        }
        diffs.add(diff);

        diff = new Diff();
        diff.setType("insert");
        diff.setRowStart(current.getPosition() + previous.size());
        diff.setRowEnd(current.getPosition() + current.size() - 1);
        diff.setOffset(totalOffset);
        if (diffRepository != null) {
            diff = diffRepository.save(diff);
        }
        diffs.add(diff);
    }

    private void addReplace(Chunk previous, Chunk current, List<Diff> diffs) {
        Diff diff = new Diff();
        diff.setType("replace");
        diff.setRowStart(current.getPosition());
        diff.setRowEnd(diff.getRowStart() + current.size() - 1);
        diff.setOffset(totalOffset);
        if (diffRepository != null) {
            diff = diffRepository.save(diff);
        }
        diffs.add(diff);
    }
}
