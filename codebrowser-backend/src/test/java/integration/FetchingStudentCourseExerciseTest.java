package integration;

import com.fasterxml.jackson.databind.JsonNode;
import static integration.BaseFetchingTest.fetchJson;
import static integration.BaseFetchingTest.testBadRequest;
import java.io.IOException;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

public class FetchingStudentCourseExerciseTest extends BaseFetchingTest {

    private static JsonNode exercise;
    private static long exerciseId;

    @BeforeClass
    public static void fetchPage() throws IOException {
        long courseId = findElementWithName(fetchJson("courses"), "course_2").get("id").longValue();
        long studentId = findElementWithName(fetchJson("students"), "student_3").get("id").longValue();
        JsonNode exercises = fetchJson("courses/" + courseId + "/exercises");
        exerciseId = findElementWithName(exercises, "exercise_3").get("id").longValue();
        exercise = fetchJson("students/" + studentId + "/courses/" + courseId + "/exercises/" + exerciseId);
    }

    @Test
    public void isObject() {
        assertTrue(exercise.isObject());
    }

    @Test
    public void hasCorrectFields() {
        checkExerciseFields(exercise, exerciseId, "exercise_3");
    }

    @Test
    public void badRequestIfInvalidId() throws IOException {
        testBadRequest("students/-1/courses/-1/exercises/-1");
    }
}
