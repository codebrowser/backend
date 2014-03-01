package integration;

import com.fasterxml.jackson.databind.JsonNode;
import static integration.BaseFetchingTest.findElementWithName;
import static integration.BaseFetchingTest.testBadRequest;
import java.io.IOException;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

public class FetchingSnapshotConceptsTest extends BaseFetchingTest {

    private static JsonNode concepts;

    @BeforeClass
    public static void fetchPage() throws IOException {
        long courseId = findElementWithName(fetchJson("courses"), "course_1").get("id").longValue();
        long studentId = findElementWithName(fetchJson("students"), "student_1").get("id").longValue();
        JsonNode exercises = fetchJson("courses/" + courseId + "/exercises");
        long exerciseId = findElementWithName(exercises, "exercise_1").get("id").longValue();
        JsonNode snapshots = fetchJson("students/" + studentId + "/courses/" + courseId + "/exercises/" + exerciseId + "/snapshots");
        long snapshotId = findElementWithName(snapshots, "2012-12-31_23-59-12_3456789").get("id").longValue();
        concepts = fetchJson("students/" + studentId + "/courses/" + courseId + "/exercises/" + exerciseId + "/snapshots/" + snapshotId + "/concepts");
    }

    @Test
    public void isArray() {
        assertTrue(concepts.isArray());
    }

    @Test
    public void hasValidConcepts() {
        for (int i = 0; i < concepts.size(); i++) {
            assertTrue(concepts.get(i).isObject());
            checkConceptFields(concepts.get(i));
        }
    }

    @Test
    public void badRequestIfInvalidId() throws IOException {
        testBadRequest("students/-1/courses/-1/exercises/-1/snapshots/-1/concepts");
    }
}
