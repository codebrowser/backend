package integration;

import com.fasterxml.jackson.databind.JsonNode;
import static integration.BaseFetchingTest.checkStudentFields;
import static integration.BaseFetchingTest.fetchJson;
import static integration.BaseFetchingTest.findElementWithName;
import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

public class FetchingStudentsTest extends BaseFetchingTest {

    private static JsonNode students;

    @BeforeClass
    public static void fetchPage() throws IOException {
        students = fetchJson("students");
    }

    @Test
    public void isArray() {
        assertTrue(students.isArray());
    }

    @Test
    public void hasThreeStudents() {
        assertEquals(3, students.size());
    }

    @Test
    public void student1HasCorrectFields() {
        JsonNode student = findElementWithName(students, "student_1");
        checkStudentFields(student, -1, "student_1");
    }

    @Test
    public void student1HasCorrectCourses() {
        JsonNode student = findElementWithName(students, "student_1");
        assertTrue(student.get("courses").isArray());
        assertEquals(2, student.get("courses").size());

        JsonNode course1 = findElementWithName(student.get("courses"), "course_1");
        checkCourseFields(course1, -1, "course_1", 2);

        JsonNode course2 = findElementWithName(student.get("courses"), "course_2");
        checkCourseFields(course2, -1, "course_2", 2);
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
    public void student3HasCorrectFields() {
        JsonNode student = findElementWithName(students, "student_3");
        checkStudentFields(student, -1, "student_3");
    }

    @Test
    public void student3HasCorrectCourses() {
        JsonNode student = findElementWithName(students, "student_3");
        assertTrue(student.get("courses").isArray());
        assertEquals(1, student.get("courses").size());

        JsonNode course1 = findElementWithName(student.get("courses"), "course_2");
        checkCourseFields(course1, -1, "course_2", 2);
    }
}