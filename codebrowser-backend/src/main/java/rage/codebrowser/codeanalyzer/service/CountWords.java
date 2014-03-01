package rage.codebrowser.codeanalyzer.service;

import rage.codebrowser.codeanalyzer.domain.Concept;
import rage.codebrowser.codeanalyzer.domain.ConceptCollection;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import rage.codebrowser.dto.SnapshotFile;

public class CountWords implements SnapshotConcepts {

    
    private final String[] INTERESTING_WORDS = {
        "private",
        "public",
        "=",
        "==",
        "if",
        "else",
        "for",
        "while",
        "break",
        "continue"
    };

    @Override
    public ConceptCollection getConcepts(SnapshotFile input) {

        ConceptCollection concepts = new ConceptCollection();

        try {
            countWordsInFile(input, concepts);
        } catch (Exception e) {
            concepts.add(new Concept("error", 1));
        }

        return concepts;
    }

    private void countWordsInFile(SnapshotFile input, ConceptCollection concepts) throws FileNotFoundException, IOException {

        BufferedReader reader = null;

        try {
            reader = openReader(input.getFilepath());

            String line;

            while ((line = reader.readLine()) != null) {
                for (String w : getWords(line)) {
                    processWord(w, concepts);
                }
            }
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private BufferedReader openReader(String filePath) throws FileNotFoundException {
        FileInputStream inputStream = new FileInputStream(filePath);
        return new BufferedReader(new InputStreamReader(inputStream));
    }

    private String[] getWords(String line) {
        return line.split("\\s");
    }

    private void processWord(String w, ConceptCollection concepts) {
        if (isInterestingWord(w)) {
            if (concepts.contains(w)) {
                Concept c = concepts.getByName(w);
                c.size += 1;
            } else {
                concepts.add( new Concept(w, 1) );
            }
        }
    }

    private boolean isInterestingWord(String word) {
        return Arrays.asList(INTERESTING_WORDS).contains(word);
    }

    
    
    @Override
    public ConceptCollection getConcepts(List<SnapshotFile> input) {
        ConceptCollection snapshotConcepts = new ConceptCollection();
        
        for (SnapshotFile file : input) {
            ConceptCollection fileConcepts = getConcepts(file);
            
            snapshotConcepts.combine(fileConcepts);
        }
        
        return snapshotConcepts;
    }
    

}
