package integration;

import com.fasterxml.jackson.databind.JsonNode;
import static integration.BaseFetchingTest.findElementWithName;
import static integration.BaseFetchingTest.testBadRequest;
import java.io.IOException;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class FetchingFileContentTest extends BaseFetchingTest {

    private static String content;

    @BeforeClass
    public static void fetchPage() throws IOException {
        long courseId = findElementWithName(fetchJson("courses"), "course_1").get("id").longValue();
        long studentId = findElementWithName(fetchJson("students"), "student_1").get("id").longValue();
        JsonNode exercises = fetchJson("courses/" + courseId + "/exercises");
        long exerciseId = findElementWithName(exercises, "exercise_1").get("id").longValue();
        JsonNode snapshots = fetchJson("students/" + studentId + "/courses/" + courseId + "/exercises/" + exerciseId + "/snapshots");
        long snapshotId = findElementWithName(snapshots, "2012-12-31_23-59-01_2345678").get("id").longValue();
        JsonNode files = fetchJson("students/" + studentId + "/courses/" + courseId + "/exercises/" + exerciseId + "/snapshots/" + snapshotId + "/files");
        long fileId = findElementWithName(files, "Main.java").get("id").longValue();
        content = fetchText("students/" + studentId + "/courses/" + courseId + "/exercises/" + exerciseId + "/snapshots/" + snapshotId + "/files/" + fileId + "/content");
    }

    @Test
    public void fileContentIsCorrect() {
        assertEquals(
                "\n"
                + "import java.util.Scanner;\n"
                + "\n"
                + "public class Main {\n"
                + "\n"
                + "    public static void main(String[] args) {\n"
                + "        Scanner reader = new Scanner(System.in);\n"
                + "    }\n"
                + "}\n",
                content);
    }

    @Test
    public void badRequestIfInvalidId() throws IOException {
        testBadRequest("students/-1/courses/-1/exercises/-1/snapshots/-1/files/-1/content");
    }
}
