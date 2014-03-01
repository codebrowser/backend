package rage.codebrowser.codeanalyzer.service;

import rage.codebrowser.codeanalyzer.domain.ConceptCollection;
import java.util.List;
import rage.codebrowser.dto.SnapshotFile;

public interface SnapshotConcepts {

    ConceptCollection getConcepts(SnapshotFile input);
    ConceptCollection getConcepts(List<SnapshotFile> input);
}
