package integration;

import com.fasterxml.jackson.databind.JsonNode;
import static integration.BaseFetchingTest.fetchJson;
import static integration.BaseFetchingTest.testBadRequest;
import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

public class FetchingStudentCourseTest extends BaseFetchingTest {

    private static JsonNode course;
    private static long courseId;

    @BeforeClass
    public static void fetchPage() throws IOException {
        long studentId = findElementWithName(fetchJson("students"), "student_2").get("id").longValue();
        courseId = findElementWithName(fetchJson("courses"), "course_1").get("id").longValue();
        course = fetchJson("students/" + studentId + "/courses/" + courseId);
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
        assertEquals(1, course.get("exercises").size());

        JsonNode exc1 = findElementWithName(course.get("exercises"), "exercise_2");
        checkExerciseFields(exc1, -1, "exercise_2");
    }

    @Test
    public void badRequestIfInvalidId() throws IOException {
        testBadRequest("students/-1/courses/-1");
    }
}
