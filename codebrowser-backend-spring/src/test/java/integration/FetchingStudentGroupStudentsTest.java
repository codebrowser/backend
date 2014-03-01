package integration;

import com.fasterxml.jackson.databind.JsonNode;
import static integration.BaseFetchingTest.fetchJson;
import java.io.IOException;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

public class FetchingStudentGroupStudentsTest extends BaseFetchingTest {

    private static JsonNode students;

    @BeforeClass
    public static void fetchPage() throws IOException {
        long groupId = fetchJson("studentgroups").get(0).get("id").longValue();
        students = fetchJson("studentgroups/" + groupId + "/students");
    }

    @Test
    public void isArray() {
        assertTrue(students.isArray());
    }

    @Test
    public void hasValidStudents() {
        for (int j = 0; j < students.size(); j++) {
            checkStudentFields(students.get(j), -1, null);
        }
    }

    @Test
    public void badRequestIfInvalidId() throws IOException {
        testBadRequest("studentgroups/-1/students");
    }
}
