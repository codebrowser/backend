package integration;

import com.fasterxml.jackson.databind.JsonNode;
import static integration.BaseFetchingTest.fetchJson;
import java.io.IOException;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

public class FetchingStudentGroupsTest extends BaseFetchingTest {

    private static JsonNode groups;

    @BeforeClass
    public static void fetchPage() throws IOException {
        groups = fetchJson("studentgroups");
    }

    @Test
    public void isArray() {
        assertTrue(groups.isArray());
    }

    @Test
    public void hasValidGroups() {
        for (int i = 0; i < groups.size(); i++) {
            assertTrue(groups.get(i).isObject());
            checkStudentGroupFields(groups.get(i), -1, null);
            JsonNode students = groups.get(i).get("students");
            assertTrue(students.isArray());
            for (int j = 0; j < students.size(); j++) {
                checkStudentFields(students.get(j), -1, null);
            }
        }
    }
}
