package integration;

import com.fasterxml.jackson.databind.JsonNode;
import static integration.BaseFetchingTest.checkSnapshotFileFields;
import static integration.BaseFetchingTest.fetchJson;
import static integration.BaseFetchingTest.findElementWithName;
import static integration.BaseFetchingTest.testBadRequest;
import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

public class FetchingSnapshotsTest extends BaseFetchingTest {

    private static JsonNode snapshots;

    @BeforeClass
    public static void fetchPage() throws IOException {
        long courseId = findElementWithName(fetchJson("courses"), "course_1").get("id").longValue();
        long studentId = findElementWithName(fetchJson("students"), "student_1").get("id").longValue();
        JsonNode exercises = fetchJson("courses/" + courseId + "/exercises");
        long exerciseId = findElementWithName(exercises, "exercise_1").get("id").longValue();
        snapshots = fetchJson("students/" + studentId + "/courses/" + courseId + "/exercises/" + exerciseId + "/snapshots");
    }

    @Test
    public void isArray() {
        assertTrue(snapshots.isArray());
    }

    @Test
    public void hasThreeSnapshots() {
        assertEquals(3, snapshots.size());
    }

    @Test
    public void snapshot1HasCorrectFields() {
        JsonNode snapshot = findElementWithName(snapshots, "2012-12-31_23-59-01_2345678");
        checkSnapshotFields(snapshot, -1, "2012-12-31_23-59-01_2345678", 1356993486678L);
    }

    @Test
    public void snapshot1HasCorrectCourse() {
        JsonNode snapshot = findElementWithName(snapshots, "2012-12-31_23-59-01_2345678");
        JsonNode course = snapshot.get("course");
        assertTrue(course.isObject());
        checkIdAndName(course, -1, "course_1"); // does not have amountOfStudents here!
    }

    @Test
    public void snapshot1HasCorrectExercise() {
        JsonNode snapshot = findElementWithName(snapshots, "2012-12-31_23-59-01_2345678");
        JsonNode exercise = snapshot.get("exercise");
        assertTrue(exercise.isObject());
        checkExerciseFields(exercise, -1, "exercise_1");
    }

    @Test
    public void snapshot1HasCorrectFiles() {
        JsonNode snapshot = findElementWithName(snapshots, "2012-12-31_23-59-01_2345678");
        assertTrue(snapshot.get("files").isArray());
        assertEquals(2, snapshot.get("files").size());

        JsonNode file1 = findElementWithName(snapshot.get("files"), "Main.java");
        checkSnapshotFileFields(file1, -1, "Main.java", 151);

        JsonNode file2 = findElementWithName(snapshot.get("files"), "Second.java");
        checkSnapshotFileFields(file2, -1, "Second.java", 59);
    }

    @Test
    public void snapshot2HasCorrectFields() {
        JsonNode snapshot = findElementWithName(snapshots, "2012-12-31_23-59-12_3456789");
        checkSnapshotFields(snapshot, -1, "2012-12-31_23-59-12_3456789", 1356994608789L);
    }

    @Test
    public void snapshot2HasCorrectCourse() {
        JsonNode snapshot = findElementWithName(snapshots, "2012-12-31_23-59-12_3456789");
        JsonNode course = snapshot.get("course");
        assertTrue(course.isObject());
        checkIdAndName(course, -1, "course_1"); // does not have amountOfStudents here!
    }

    @Test
    public void snapshot2HasCorrectExercise() {
        JsonNode snapshot = findElementWithName(snapshots, "2012-12-31_23-59-12_3456789");
        JsonNode exercise = snapshot.get("exercise");
        assertTrue(exercise.isObject());
        checkExerciseFields(exercise, -1, "exercise_1");
    }

    @Test
    public void snapshot2HasCorrectFiles() {
        JsonNode snapshot = findElementWithName(snapshots, "2012-12-31_23-59-12_3456789");
        assertTrue(snapshot.get("files").isArray());
        assertEquals(3, snapshot.get("files").size());

        JsonNode file1 = findElementWithName(snapshot.get("files"), "Main.java");
        checkSnapshotFileFields(file1, -1, "Main.java", 183);

        JsonNode file2 = findElementWithName(snapshot.get("files"), "Second.java");
        checkSnapshotFileFields(file2, -1, "Second.java", 59);

        JsonNode file3 = findElementWithName(snapshot.get("files"), "pkg/Third.java");
        checkSnapshotFileFields(file3, -1, "pkg/Third.java", 66);
    }

    @Test
    public void snapshot3HasCorrectFields() {
        JsonNode snapshot = findElementWithName(snapshots, "2012-12-31_23-59-23_4567890");
        checkSnapshotFields(snapshot, -1, "2012-12-31_23-59-23_4567890", 1356995730890L);
    }

    @Test
    public void snapshot3HasCorrectCourse() {
        JsonNode snapshot = findElementWithName(snapshots, "2012-12-31_23-59-23_4567890");
        JsonNode course = snapshot.get("course");
        assertTrue(course.isObject());
        checkIdAndName(course, -1, "course_1"); // does not have amountOfStudents here!
    }

    @Test
    public void snapshot3HasCorrectExercise() {
        JsonNode snapshot = findElementWithName(snapshots, "2012-12-31_23-59-23_4567890");
        JsonNode exercise = snapshot.get("exercise");
        assertTrue(exercise.isObject());
        checkExerciseFields(exercise, -1, "exercise_1");
    }

    @Test
    public void snapshot3HasCorrectFiles() {
        JsonNode snapshot = findElementWithName(snapshots, "2012-12-31_23-59-23_4567890");
        assertTrue(snapshot.get("files").isArray());
        assertEquals(2, snapshot.get("files").size());

        JsonNode file1 = findElementWithName(snapshot.get("files"), "Main.java");
        checkSnapshotFileFields(file1, -1, "Main.java", 151);

        JsonNode file3 = findElementWithName(snapshot.get("files"), "pkg/Third.java");
        checkSnapshotFileFields(file3, -1, "pkg/Third.java", 66);
    }

    @Test
    public void badRequestIfInvalidId() throws IOException {
        testBadRequest("students/-1/courses/-1/exercises/-1/snapshots");
    }
}