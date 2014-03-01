package integration;

import com.fasterxml.jackson.databind.JsonNode;
import static integration.BaseFetchingTest.checkExerciseFields;
import static integration.BaseFetchingTest.fetchJson;
import static integration.BaseFetchingTest.testBadRequest;
import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

public class FetchingCourseExercisesTest extends BaseFetchingTest {

    private static JsonNode exercises;

    @BeforeClass
    public static void fetchPage() throws IOException {
        long courseId = findElementWithName(fetchJson("courses"), "course_2").get("id").longValue();
        exercises = fetchJson("courses/" + courseId + "/exercises");
    }

    @Test
    public void isArray() {
        assertTrue(exercises.isArray());
    }

    @Test
    public void hasTwoExercises() {
        assertEquals(2, exercises.size());
    }

    @Test
    public void exercise2HasCorrectFields() {
        JsonNode exc = findElementWithName(exercises, "exercise_2");
        checkExerciseFields(exc, -1, "exercise_2");
    }

    @Test
    public void exercise3HasCorrectFields() {
        JsonNode exc = findElementWithName(exercises, "exercise_3");
        checkExerciseFields(exc, -1, "exercise_3");
    }

    @Test
    public void badRequestIfInvalidId() throws IOException {
        testBadRequest("courses/-1/exercises");
    }
}
