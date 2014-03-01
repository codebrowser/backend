/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rage.codebrowser.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import rage.codebrowser.codeanalyzer.domain.ConceptCollection;
import rage.codebrowser.codeanalyzer.service.SnapshotConcepts;
import rage.codebrowser.dto.Exercise;
import rage.codebrowser.dto.Snapshot;
import rage.codebrowser.dto.SnapshotFile;
import rage.codebrowser.dto.Student;
import rage.codebrowser.repository.ExerciseAnswerRepository;

/**
 *
 * @author Jami
 */
@Controller
public class ConceptRESTController {
    
    @Autowired
    private SnapshotConcepts snapshotConcepts;
    
    @Autowired
    private ExerciseAnswerRepository exerciseAnswerRepository;
    
    @RequestMapping(value = {"students/{studentId}/courses/{courseId}/exercises/{exerciseId}/snapshots/{snapshotId}/files/{snapshotFileId}/concepts"})
    @ResponseBody
    public ConceptCollection getSnapshotFileConcepts(@PathVariable("snapshotFileId") SnapshotFile snapshotFile) {
        
        return snapshotConcepts.getConcepts(snapshotFile);
    }
        
    @RequestMapping(value = {"students/{studentId}/courses/{courseId}/exercises/{exerciseId}/snapshots/{snapshotId}/concepts"})
    @ResponseBody
    public ConceptCollection getSnapshotConcepts(@PathVariable("snapshotId") Snapshot snapshot) {
        return snapshotConcepts.getConcepts(snapshot.getFiles());
    }
    
    @RequestMapping(value = {"students/{studentId}/courses/{courseId}/exercises/{exerciseId}/concepts"})
    @ResponseBody
    public Map<Long, ConceptCollection> getAllConceptsForExercise(@PathVariable("studentId") Student student, @PathVariable("exerciseId") Exercise exercise) {
        List<Snapshot> snapshots = exerciseAnswerRepository.findByStudentAndExercise(student, exercise).getSnapshots();
        Map<Long, ConceptCollection> snapshotConceptCollections = new HashMap<Long, ConceptCollection>();
        for (Snapshot snapshot : snapshots) {
            snapshotConceptCollections.put(snapshot.getId(), snapshotConcepts.getConcepts(snapshot.getFiles()));
        }
        return snapshotConceptCollections;
    }
}
