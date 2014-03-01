package integration;

import com.fasterxml.jackson.databind.JsonNode;
import static integration.BaseFetchingTest.fetchJson;
import static integration.BaseFetchingTest.findElementWithName;
import static integration.BaseFetchingTest.testBadRequest;
import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

public class FetchingStudentCourseExercisesTest extends BaseFetchingTest {

    private static JsonNode exercises;

    @BeforeClass
    public static void fetchPage() throws IOException {
        long courseId = findElementWithName(fetchJson("courses"), "course_2").get("id").longValue();
        long studentId = findElementWithName(fetchJson("students"), "student_3").get("id").longValue();
        exercises = fetchJson("students/" + studentId + "/courses/" + courseId + "/exercises");
    }

    @Test
    public void isArray() {
        assertTrue(exercises.isArray());
    }

    @Test
    public void hasOneExercise() {
        assertEquals(1, exercises.size());
    }

    @Test
    public void exercise3HasCorrectFields() {
        JsonNode exc = findElementWithName(exercises, "exercise_3");
        checkExerciseFields(exc, -1, "exercise_3");
    }

    @Test
    public void badRequestIfInvalidId() throws IOException {
        testBadRequest("students/-1/courses/-1/exercises");
    }
}