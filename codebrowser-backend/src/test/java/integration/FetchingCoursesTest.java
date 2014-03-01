package integration;

import com.fasterxml.jackson.databind.JsonNode;
import static integration.BaseFetchingTest.checkCourseFields;
import static integration.BaseFetchingTest.checkExerciseFields;
import static integration.BaseFetchingTest.findElementWithName;
import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

public class FetchingCoursesTest extends BaseFetchingTest {

    private static JsonNode courses;

    @BeforeClass
    public static void fetchPage() throws IOException {
        courses = fetchJson("courses");
    }

    @Test
    public void isArray() {
        assertTrue(courses.isArray());
    }

    @Test
    public void hasTwoCourses() {
        assertEquals(2, courses.size());
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
    public void course2HasCorrectFields() {
        JsonNode course = findElementWithName(courses, "course_2");
        checkCourseFields(course, -1, "course_2", 2);
    }

    @Test
    public void course2HasCorrectExercises() {
        JsonNode course = findElementWithName(courses, "course_2");
        assertTrue(course.get("exercises").isArray());
        assertEquals(2, course.get("exercises").size());

        JsonNode exc2 = findElementWithName(course.get("exercises"), "exercise_2");
        checkExerciseFields(exc2, -1, "exercise_2");
        JsonNode exc3 = findElementWithName(course.get("exercises"), "exercise_3");
        checkExerciseFields(exc3, -1, "exercise_3");
    }
}