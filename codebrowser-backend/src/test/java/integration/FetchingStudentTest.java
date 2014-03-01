package integration;

import com.fasterxml.jackson.databind.JsonNode;
import static integration.BaseFetchingTest.checkStudentFields;
import static integration.BaseFetchingTest.fetchJson;
import static integration.BaseFetchingTest.findElementWithName;
import static integration.BaseFetchingTest.testBadRequest;
import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

public class FetchingStudentTest extends BaseFetchingTest {

    private static JsonNode student;
    private static long studentId;

    @BeforeClass
    public static void fetchPage() throws IOException {
        studentId = findElementWithName(fetchJson("students"), "student_1").get("id").longValue();
        student = fetchJson("students/" + studentId);
    }

    @Test
    public void isObject() {
        assertTrue(student.isObject());
    }

    @Test
    public void hasCorrectFields() {
        checkStudentFields(student, studentId, "student_1");
    }

    @Test
    public void hasCorrectCourses() {
        assertTrue(student.get("courses").isArray());
        assertEquals(2, student.get("courses").size());

        JsonNode course1 = findElementWithName(student.get("courses"), "course_1");
        checkCourseFields(course1, -1, "course_1", 2);

        JsonNode course2 = findElementWithName(student.get("courses"), "course_2");
        checkCourseFields(course2, -1, "course_2", 2);
    }

    @Test
    public void badRequestIfInvalidId() throws IOException {
        testBadRequest("students/-1");
    }
}