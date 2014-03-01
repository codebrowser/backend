package integration;

import com.fasterxml.jackson.databind.JsonNode;
import static integration.BaseFetchingTest.fetchJson;
import java.io.IOException;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

public class FetchingStudentGroupStudentTest extends BaseFetchingTest {

    private static JsonNode student;

    private static long studentId;

    @BeforeClass
    public static void fetchPage() throws IOException {
        long groupId = fetchJson("studentgroups").get(0).get("id").longValue();
        studentId = fetchJson("studentgroups/" + groupId + "/students").get(0).get("id").asLong();
        student = fetchJson("studentgroups/" + groupId + "/students/" + studentId);
    }

    @Test
    public void isObject() {
        assertTrue(student.isObject());
    }

    @Test
    public void isValidStudent() {
        checkStudentFields(student, studentId, null);
    }

    @Test
    public void badRequestIfInvalidId() throws IOException {
        testBadRequest("studentgroups/-1/students/-1");
    }
}
