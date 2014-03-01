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

public class FetchingCourseStudentTest extends BaseFetchingTest {

    private static long studentId;
    private static JsonNode student;

    @BeforeClass
    public static void fetchPage() throws IOException {
        long courseId = findElementWithName(fetchJson("courses"), "course_2").get("id").longValue();
        studentId = findElementWithName(fetchJson("students"), "student_3").get("id").longValue();
        student = fetchJson("courses/" + courseId + "/students/" + studentId);
    }

    @Test
    public void isObject() {
        assertTrue(student.isObject());
    }

    @Test
    public void hasCorrectFields() {
        checkStudentFields(student, studentId, "student_3");
    }

    @Test
    public void hasCorrectCourses() {
        assertTrue(student.get("courses").isArray());
        assertEquals(1, student.get("courses").size());

        JsonNode course1 = findElementWithName(student.get("courses"), "course_2");
        checkCourseFields(course1, -1, "course_2", 2);
    }

    @Test
    public void badRequestIfInvalidId() throws IOException {
        testBadRequest("courses/-1/students/-1");
    }
}
