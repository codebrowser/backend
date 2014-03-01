package integration;

import com.fasterxml.jackson.databind.JsonNode;
import static integration.BaseFetchingTest.checkCourseFields;
import static integration.BaseFetchingTest.checkExerciseFields;
import static integration.BaseFetchingTest.checkStudentFields;
import static integration.BaseFetchingTest.checkTagFields;
import static integration.BaseFetchingTest.fetchJson;
import static integration.BaseFetchingTest.findElementWithName;
import static integration.BaseFetchingTest.postJson;
import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

public class TagsTest extends BaseFetchingTest {

    private static long exerciseId, courseId, studentId, snapshotId;

    @BeforeClass
    public static void fetchPage() throws IOException {
        courseId = findElementWithName(fetchJson("courses"), "course_2").get("id").longValue();
        studentId = findElementWithName(fetchJson("students"), "student_3").get("id").longValue();
        JsonNode exercises = fetchJson("courses/" + courseId + "/exercises");
        exerciseId = findElementWithName(exercises, "exercise_3").get("id").longValue();
        JsonNode snapshots = fetchJson("students/" + studentId + "/courses/" + courseId + "/exercises/" + exerciseId + "/snapshots");
        snapshotId = findElementWithName(snapshots, "2012-12-31_23-59-01_2345678").get("id").longValue();
    }

    private static JsonNode findTag(JsonNode tags, String tagName) {
        for (JsonNode tag : tags) {
            JsonNode tn = tag.get("tagName");
            if (tn != null && tn.get("name") != null && tn.get("name").asText().equals(tagName)) {
                return tag;
            }
        }

        return null;
    }

    @Test
    public void testTagPosting() throws IOException {
        // Tag list is initially an empty array
        JsonNode tags = fetchJson("students/" + studentId + "/courses/" + courseId + "/exercises/" + exerciseId + "/tags");
        assertTrue(tags.isArray());
        assertEquals(0, tags.size());

        // Post tag for solution
        String reqBody = "{\"tagName\":{\"name\":\"tag1\"}}";
        JsonNode tag = postJson("students/" + studentId + "/courses/" + courseId + "/exercises/" + exerciseId + "/tags", reqBody);

        // Check returned tag
        checkTagFields(tag, -1);
        checkStudentFields(tag.get("student"), studentId, "student_3");
        checkCourseFields(tag.get("course"), courseId, "course_2", 2);
        checkExerciseFields(tag.get("exercise"), exerciseId, "exercise_3");
        assertNull(tag.get("snapshot"));
        checkTagNameFields(tag.get("tagName"), -1, "tag1");

        // Post tag for snapshot
        reqBody = "{\"tagName\":{\"name\":\"tag2\"}}";
        JsonNode tag2 = postJson("students/" + studentId + "/courses/" + courseId + "/exercises/" + exerciseId + "/snapshots/" + snapshotId + "/tags", reqBody);

        // Check returned tag
        checkTagFields(tag2, -1);
        checkStudentFields(tag2.get("student"), studentId, "student_3");
        checkCourseFields(tag2.get("course"), courseId, "course_2", 2);
        checkExerciseFields(tag2.get("exercise"), exerciseId, "exercise_3");
        checkSnapshotFields(tag2.get("snapshot"), snapshotId, "2012-12-31_23-59-01_2345678", 1356993486678L);
        checkTagNameFields(tag2.get("tagName"), -1, "tag2");

        // Tag list should now have two tags
        tags = fetchJson("students/" + studentId + "/courses/" + courseId + "/exercises/" + exerciseId + "/tags");
        assertEquals(2, tags.size());

        // Check tag1
        System.out.println(tags.toString());
        tag = findTag(tags, "tag1");
        checkTagFields(tag, -1);
        checkStudentFields(tag.get("student"), studentId, "student_3");
        checkCourseFields(tag.get("course"), courseId, "course_2", 2);
        checkExerciseFields(tag.get("exercise"), exerciseId, "exercise_3");
        assertNull(tag.get("snapshot"));
        checkTagNameFields(tag.get("tagName"), -1, "tag1");

        // Check tag2
        tag2 = findTag(tags, "tag2");
        checkTagFields(tag2, -1);
        checkStudentFields(tag2.get("student"), studentId, "student_3");
        checkCourseFields(tag2.get("course"), courseId, "course_2", 2);
        checkExerciseFields(tag2.get("exercise"), exerciseId, "exercise_3");
        checkSnapshotFields(tag2.get("snapshot"), snapshotId, "2012-12-31_23-59-01_2345678", 1356993486678L);
        checkTagNameFields(tag2.get("tagName"), -1, "tag2");

        // Delete tag1
        tag = sendDeleteRequest("students/" + studentId + "/courses/" + courseId + "/exercises/" + exerciseId + "/snapshots/" + snapshotId + "/tags/" + tag.get("id").asLong());
        checkTagFields(tag, -1);
        checkStudentFields(tag.get("student"), studentId, "student_3");
        checkCourseFields(tag.get("course"), courseId, "course_2", 2);
        checkExerciseFields(tag.get("exercise"), exerciseId, "exercise_3");
        assertNull(tag.get("snapshot"));
        checkTagNameFields(tag.get("tagName"), -1, "tag1");

        // Tag list should now have only one tag
        tags = fetchJson("students/" + studentId + "/courses/" + courseId + "/exercises/" + exerciseId + "/tags");
        assertEquals(1, tags.size());
        assertNull(findTag(tags, "tag1"));
        assertNotNull(findTag(tags, "tag2"));
    }
}