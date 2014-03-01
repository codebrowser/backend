package integration;

import com.fasterxml.jackson.databind.JsonNode;
import static integration.BaseFetchingTest.checkCourseFields;
import static integration.BaseFetchingTest.checkExerciseFields;
import static integration.BaseFetchingTest.fetchJson;
import static integration.BaseFetchingTest.findElementWithName;
import static integration.BaseFetchingTest.testBadRequest;
import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

public class FetchingCourseTest extends BaseFetchingTest {

    private static JsonNode course;
    private static long courseId;

    @BeforeClass
    public static void fetchPage() throws IOException {
        courseId = findElementWithName(fetchJson("courses"), "course_1").get("id").longValue();
        course = fetchJson("courses/" + courseId);
    }

    @Test
    public void isObject() {
        assertTrue(course.isObject());
    }

    @Test
    public void hasCorrectFields() {
        checkCourseFields(course, courseId, "course_1", 2);
    }

    @Test
    public void hasCorrectExercises() {
        assertTrue(course.get("exercises").isArray());
        assertEquals(2, course.get("exercises").size());

        JsonNode exc1 = findElementWithName(course.get("exercises"), "exercise_1");
        checkExerciseFields(exc1, -1, "exercise_1");
        JsonNode exc2 = findElementWithName(course.get("exercises"), "exercise_2");
        checkExerciseFields(exc2, -1, "exercise_2");
    }

    @Test
    public void badRequestIfInvalidId() throws IOException {
        testBadRequest("courses/-1");
    }
}