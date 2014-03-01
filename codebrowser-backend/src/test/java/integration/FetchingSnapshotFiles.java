package integration;

import com.fasterxml.jackson.databind.JsonNode;
import static integration.BaseFetchingTest.checkDiffFields;
import static integration.BaseFetchingTest.fetchJson;
import static integration.BaseFetchingTest.findElementWithName;
import static integration.BaseFetchingTest.testBadRequest;
import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

public class FetchingSnapshotFiles extends BaseFetchingTest {

    private static JsonNode files;

    @BeforeClass
    public static void fetchPage() throws IOException {
        long courseId = findElementWithName(fetchJson("courses"), "course_1").get("id").longValue();
        long studentId = findElementWithName(fetchJson("students"), "student_1").get("id").longValue();
        JsonNode exercises = fetchJson("courses/" + courseId + "/exercises");
        long exerciseId = findElementWithName(exercises, "exercise_1").get("id").longValue();
        JsonNode snapshots = fetchJson("students/" + studentId + "/courses/" + courseId + "/exercises/" + exerciseId + "/snapshots");
        long snapshotId = findElementWithName(snapshots, "2012-12-31_23-59-12_3456789").get("id").longValue();
        files = fetchJson("students/" + studentId + "/courses/" + courseId + "/exercises/" + exerciseId + "/snapshots/" + snapshotId + "/files");
    }

    @Test
    public void isArray() {
        assertTrue(files.isArray());
    }

    @Test
    public void hasThreeFiles() {
        assertEquals(3, files.size());
    }

    @Test
    public void file1HasCorrectFields() {
        JsonNode snapshot = findElementWithName(files, "Main.java");
        checkSnapshotFileFields(snapshot, -1, "Main.java", 183);
    }

    @Test
    public void file1HasCorrectDiffList() {
        JsonNode file = findElementWithName(files, "Main.java");
        JsonNode diffList = file.get("diffs");
        assertTrue(diffList.isObject());
        checkDiffListFields(diffList, -1, 11, 4, 1, 2);

        JsonNode differences = diffList.get("differences");
        assertTrue(differences.isArray());
        assertEquals(3, differences.size());

        checkDiffFields(differences.get(0), -1, "delete", 1, 2, 0, 1, 2, "import java.util.Scanner;\n\n");
        checkDiffFields(differences.get(1), -1, "replace", 4, 4, 2, -1, -1, null);
        checkDiffFields(differences.get(2), -1, "insert", 6, 9, 2, -1, -1, null);
    }

    @Test
    public void file2HasCorrectFields() {
        JsonNode snapshot = findElementWithName(files, "Second.java");
        checkSnapshotFileFields(snapshot, -1, "Second.java", 59);
    }

    @Test
    public void file2HasCorrectDiffList() {
        JsonNode file = findElementWithName(files, "Second.java");
        JsonNode diffList = file.get("diffs");
        assertTrue(diffList.isObject());
        checkDiffListFields(diffList, -1, 7, 0, 0, 0);

        JsonNode differences = diffList.get("differences");
        assertTrue(differences.isArray());
        assertEquals(0, differences.size());
    }

    @Test
    public void file3HasCorrectFields() {
        JsonNode snapshot = findElementWithName(files, "pkg/Third.java");
        checkSnapshotFileFields(snapshot, -1, "pkg/Third.java", 66);
    }

    @Test
    public void file3HasCorrectDiffList() {
        JsonNode file = findElementWithName(files, "pkg/Third.java");
        JsonNode diffList = file.get("diffs");
        assertTrue(diffList.isObject());
        checkDiffListFields(diffList, -1, 7, 7, 0, 0);

        JsonNode differences = diffList.get("differences");
        assertTrue(differences.isArray());
        assertEquals(1, differences.size());

        checkDiffFields(differences.get(0), -1, "insert", 0, 6, 0, -1, -1, null);
    }

    @Test
    public void badRequestIfInvalidId() throws IOException {
        testBadRequest("students/-1/courses/-1/exercises/-1/snapshots/-1/files");
    }
}
