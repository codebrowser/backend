package integration;

import com.fasterxml.jackson.databind.JsonNode;
import static integration.BaseFetchingTest.fetchJson;
import static integration.BaseFetchingTest.testBadRequest;
import java.io.IOException;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

public class FetchingStudentGroupTest extends BaseFetchingTest {

    private static JsonNode group;

    private static long groupId;

    @BeforeClass
    public static void fetchPage() throws IOException {
        groupId = fetchJson("studentgroups").get(0).get("id").longValue();
        group = fetchJson("studentgroups/" + groupId);
    }

    @Test
    public void isObject() {
        assertTrue(group.isObject());
    }

    @Test
    public void isValidGroup() {
        checkStudentGroupFields(group, groupId, null);
        JsonNode students = group.get("students");
        assertTrue(students.isArray());
        for (int j = 0; j < students.size(); j++) {
            checkStudentFields(students.get(j), -1, null);
        }
    }

    @Test
    public void badRequestIfInvalidId() throws IOException {
        testBadRequest("studentgroups/-1");
    }
}
