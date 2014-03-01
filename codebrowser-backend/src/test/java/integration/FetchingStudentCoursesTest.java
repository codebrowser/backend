package integration;

import com.fasterxml.jackson.databind.JsonNode;
import static integration.BaseFetchingTest.fetchJson;
import static integration.BaseFetchingTest.testBadRequest;
import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

public class FetchingStudentCoursesTest extends BaseFetchingTest {

    private static JsonNode courses;

    @BeforeClass
    public static void fetchPage() throws IOException {
        long studentId = findElementWithName(fetchJson("students"), "student_2").get("id").longValue();
        courses = fetchJson("students/" + studentId + "/courses");
    }

    @Test
    public void isArray() {
        assertTrue(courses.isArray());
    }

    @Test
    public void hasOneCourse() {
        assertEquals(1, courses.size());
    }

    @Test
    public void course1HasCorrectFields() {
        JsonNode course = findElementWithName(courses, "course_1");
        checkCourseFields(course, -1, "course_1", 2);
    }

    @Test
    public void course1HasCorrectExercises() {
        JsonNode course = findElementWithName(courses, "course_1");
        assertTrue(course.get("exercises").isArray());
        assertEquals(2, course.get("exercises").size());

        JsonNode exc1 = findElementWithName(course.get("exercises"), "exercise_1");
        checkExerciseFields(exc1, -1, "exercise_1");
        JsonNode exc2 = findElementWithName(course.get("exercises"), "exercise_2");
        checkExerciseFields(exc2, -1, "exercise_2");
    }

    @Test
    public void badRequestIfInvalidId() throws IOException {
        testBadRequest("students/-1/courses");
    }
}
