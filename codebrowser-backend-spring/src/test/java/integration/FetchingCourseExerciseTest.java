package integration;

import com.fasterxml.jackson.databind.JsonNode;
import static integration.BaseFetchingTest.fetchJson;
import static integration.BaseFetchingTest.findElementWithName;
import static integration.BaseFetchingTest.testBadRequest;
import java.io.IOException;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

public class FetchingCourseExerciseTest extends BaseFetchingTest {

    private static JsonNode exercise;
    private static long exerciseId;

    @BeforeClass
    public static void fetchPage() throws IOException {
        long courseId = findElementWithName(fetchJson("courses"), "course_2").get("id").longValue();
        JsonNode exercises = fetchJson("courses/" + courseId + "/exercises");
        exerciseId = findElementWithName(exercises, "exercise_2").get("id").longValue();
        exercise = fetchJson("courses/" + courseId + "/exercises/" + exerciseId);
    }

    @Test
    public void isObject() {
        assertTrue(exercise.isObject());
    }

    @Test
    public void hasCorrectFields() {
        checkExerciseFields(exercise, exerciseId, "exercise_2");
    }

    @Test
    public void badRequestIfInvalidId() throws IOException {
        testBadRequest("courses/-1/exercises/-1");
    }
}
