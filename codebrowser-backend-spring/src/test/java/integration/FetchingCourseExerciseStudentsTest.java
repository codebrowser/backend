package integration;

import com.fasterxml.jackson.databind.JsonNode;
import static integration.BaseFetchingTest.fetchJson;
import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

public class FetchingCourseExerciseStudentsTest extends BaseFetchingTest {

    private static JsonNode students;

    @BeforeClass
    public static void fetchPage() throws IOException {
        long courseId = findElementWithName(fetchJson("courses"), "course_1").get("id").longValue();
        JsonNode exercises = fetchJson("courses/" + courseId + "/exercises");
        long exerciseId = findElementWithName(exercises, "exercise_2").get("id").longValue();
        students = fetchJson("courses/" + courseId + "/exercises/" + exerciseId + "/students");
    }

    @Test
    public void isArray() {
        assertTrue(students.isArray());
    }

    @Test
    public void hasOneStudent() {
        assertEquals(1, students.size());
    }

    @Test
    public void student2HasCorrectFields() {
        JsonNode student = findElementWithName(students, "student_2");
        checkStudentFields(student, -1, "student_2");
    }

    @Test
    public void student2HasCorrectCourses() {
        JsonNode student = findElementWithName(students, "student_2");
        assertTrue(student.get("courses").isArray());
        assertEquals(1, student.get("courses").size());

        JsonNode course1 = findElementWithName(student.get("courses"), "course_1");
        checkCourseFields(course1, -1, "course_1", 2);
    }

    @Test
    public void badRequestIfInvalidId() throws IOException {
        testBadRequest("courses/-1/exercises/-1/students");
    }
}
