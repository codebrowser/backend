
package rage.codebrowser.codeanalyzer.service;

import rage.codebrowser.codeanalyzer.domain.Concept;
import rage.codebrowser.codeanalyzer.domain.ConceptCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import rage.codebrowser.dto.SnapshotFile;


public class DummyConcepts implements SnapshotConcepts {
    
    private final int MIN_VALUE = 1;
    private final int MAX_VALUE = 10;
    
    private final int MIN_CONCEPTS_IN_FILE = 2;
    private final int MAX_CONCEPTS_IN_FILE = 7;
    
    private final String[] CONCEPT_NAMES = new String[]{
        "tempAssignments",
        "nestingDepth",
        "nonSpecificCatch",
        "redundantComment",
        "codeCommentOut",
        "argumentFlock",
        "tooManyArgumetns",
        "deadFunction",
        "duplication",
        "multipleSwitch",
        "unnamedNumberConstants",
        "negativeConditional",
        "wildcardImport",
        "methodsNotTested",
        "shortNames",
        "depenciesToImplementations",
        "depencies",
        "tooLongMethods",
        "largeClass"
    };
    
    private HashMap<String, ConceptCollection> conceptCache;

    public DummyConcepts() {
        this.conceptCache = new HashMap<String, ConceptCollection>();
    }
    

    @Override
    public ConceptCollection getConcepts(SnapshotFile input) {
        String fileKey = input.getFilepath();
        
        if ( ! this.conceptCache.containsKey(fileKey) ) {
            this.conceptCache.put(fileKey, randomConcepts());
        }
        
        return conceptCache.get(fileKey);
    }

    @Override
    public ConceptCollection getConcepts(List<SnapshotFile> input) {
        ConceptCollection result = new ConceptCollection();
        
        for (SnapshotFile f : input) {
            result.combine( this.getConcepts(f) );
        } 
        
        return result;
    }
    
    private ConceptCollection randomConcepts() {
        int howMany = randomBetween(MIN_CONCEPTS_IN_FILE, maxConceptsInFile());
        
        ConceptCollection concepts = new ConceptCollection();
        
        for (String name : randomConceptNames(howMany)) {
            int value = randomBetween(MIN_VALUE, MAX_VALUE);
            concepts.add( new Concept(name, value));
        }

        return concepts;
    }
    
    private int randomBetween(int min, int max) {
        return min + (int)(Math.random() * ((max - min) + 1));
    }
        
    private int maxConceptsInFile() {
        return Math.min(MAX_CONCEPTS_IN_FILE, CONCEPT_NAMES.length);
    }
    
    private ArrayList<String> randomConceptNames(int howMany) {
        ArrayList<String> names = shuffledNames();
        
        while (names.size() > howMany) {
            names.remove(0);
        }
        
        return names;
    }

    private ArrayList<String> shuffledNames() {
        ArrayList<String> list = new ArrayList<String>();
        list.addAll( Arrays.asList(CONCEPT_NAMES) );
        Collections.shuffle(list);
        return list;
    }
}

