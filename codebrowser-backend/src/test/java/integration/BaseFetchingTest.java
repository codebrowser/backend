package integration;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import java.io.IOException;
import java.net.URL;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import static org.junit.Assert.*;

public class BaseFetchingTest {

    private static String URL_ROOT = "http://localhost:10377/app/";

    private static WebClient client;

    @BeforeClass
    public static void createClient() throws IOException {
        client = new WebClient();
        client.setThrowExceptionOnFailingStatusCode(false);
    }

    @AfterClass
    public static void closeBrowser() {
        client.closeAllWindows();
    }

    static JsonNode fetchJson(String urlSuffix) throws IOException {
        Page page = client.getPage(URL_ROOT + urlSuffix);
        WebResponse response = page.getWebResponse();

        try {
            assertEquals(200, response.getStatusCode());
            assertEquals("application/json", response.getContentType());
            assertEquals("UTF-8", response.getContentCharset());
            //System.out.println(response.getContentAsString());
            return new ObjectMapper().readTree(response.getContentAsString());
        } catch (JsonParseException ex) {
            fail("Response is not json");
            return null;
        }
    }

    static String fetchText(String urlSuffix) throws IOException {
        Page page = client.getPage(URL_ROOT + urlSuffix);
        WebResponse response = page.getWebResponse();

        assertEquals(200, response.getStatusCode());
        assertEquals("text/plain", response.getContentType());
        assertEquals("UTF-8", response.getContentCharset());
        //System.out.println(response.getContentAsString());
        return response.getContentAsString();
    }

    static JsonNode postJson(String urlSuffix, String requestBody) throws IOException {
        WebRequest request = new WebRequest(new URL(URL_ROOT + urlSuffix), HttpMethod.POST);
        request.setRequestBody(requestBody);
        request.setAdditionalHeader("Content-Type", "application/json");

        Page page = client.getPage(request);
        WebResponse response = page.getWebResponse();

        try {
            assertEquals(200, response.getStatusCode());
            assertEquals("application/json", response.getContentType());
            assertEquals("UTF-8", response.getContentCharset());
            //System.out.println(response.getContentAsString());
            return new ObjectMapper().readTree(response.getContentAsString());
        } catch (JsonParseException ex) {
            fail("Response is not json");
            return null;
        }
    }

    static JsonNode sendDeleteRequest(String urlSuffix) throws IOException {
        WebRequest request = new WebRequest(new URL(URL_ROOT + urlSuffix), HttpMethod.DELETE);

        Page page = client.getPage(request);
        WebResponse response = page.getWebResponse();

        try {
            assertEquals(200, response.getStatusCode());
            assertEquals("application/json", response.getContentType());
            assertEquals("UTF-8", response.getContentCharset());
            //System.out.println(response.getContentAsString());
            return new ObjectMapper().readTree(response.getContentAsString());
        } catch (JsonParseException ex) {
            fail("Response is not json");
            return null;
        }
    }

    static void testBadRequest(String urlSuffix) throws IOException {
        Page page = client.getPage(URL_ROOT + urlSuffix);
        assertEquals(400, page.getWebResponse().getStatusCode());
    }

    static JsonNode findElementWithName(JsonNode parent, String name) {
        for (JsonNode child : parent) {
            JsonNode field = child.get("name");
            assertNotNull(field);
            assertTrue(field.isTextual());
            if (name.equals(field.asText())) {
                return child;
            }
        }

        fail("element with name " + name + " was not found");
        return null;
    }

    static void checkId(JsonNode node, long id) {
        assertTrue(node.get("id").isIntegralNumber());
        if (id >= 0) {
            assertEquals(id, node.get("id").longValue());
        }
    }

    static void checkIdAndName(JsonNode node, long id, String name) {
        // id
        checkId(node, id);

        // name
        if (name != null) {
            assertEquals(name, node.get("name").textValue());
        } else {
            assertTrue(node.has("name"));
            assertTrue(node.get("name").isTextual());
        }
    }

    static void checkCourseFields(JsonNode course, long id, String name, int amountOfStudents) {
        checkIdAndName(course, id, name);

        // students field is ignored
        assertFalse(course.has("students"));

        // amountOfStudents
        assertTrue(course.get("amountOfStudents").isIntegralNumber());
        assertEquals(amountOfStudents, course.get("amountOfStudents").intValue());
    }

    static void checkExerciseFields(JsonNode exercise, long id, String name) {
        checkIdAndName(exercise, id, name);
    }

    static void checkStudentFields(JsonNode student, long id, String name) {
        checkIdAndName(student, id, name);
    }

    static void checkSnapshotFields(JsonNode snapshot, long id, String name, long snapshotTime) {
        checkIdAndName(snapshot, id, name);

        // type
        assertEquals("EVENT", snapshot.get("type").textValue());

        // exerciseAnswer is ignored
        assertFalse(snapshot.has("exerciseAnswer"));

        // snapshotTime
        assertTrue(snapshot.get("snapshotTime").isIntegralNumber());
        assertEquals(snapshotTime, snapshot.get("snapshotTime").longValue());

        // compiles
        assertTrue(snapshot.get("compiles").isBoolean());

        // percentageOfTestsPassing
        if (!snapshot.get("percentageOfTestsPassing").isNull()) {
            assertTrue(snapshot.get("percentageOfTestsPassing").isIntegralNumber());
            int value = snapshot.get("percentageOfTestsPassing").intValue();
            assertTrue(value >= 0);
            assertTrue(value <= 100);
        }
    }

    static void checkSnapshotFileFields(JsonNode file, long id, String name, long filesize) {
        checkIdAndName(file, id, name);

        // filepath is ignored
        assertFalse(file.has("filepath"));

        // filesize
        assertTrue(file.get("filesize").isIntegralNumber());
        assertEquals(filesize, file.get("filesize").longValue());
    }

    static void checkDiffListFields(JsonNode diffList, long id, int lines, int inserted, int modified, int deleted) {
        // id
        //checkId(diffList, id);

        // lines
        assertTrue(diffList.get("lines").isIntegralNumber());
        assertEquals(lines, diffList.get("lines").intValue());

        // inserted
        assertTrue(diffList.get("inserted").isIntegralNumber());
        assertEquals(inserted, diffList.get("inserted").intValue());

        // modified
        assertTrue(diffList.get("modified").isIntegralNumber());
        assertEquals(modified, diffList.get("modified").intValue());

        // deleted
        assertTrue(diffList.get("deleted").isIntegralNumber());
        assertEquals(deleted, diffList.get("deleted").intValue());

        // total
        assertTrue(diffList.get("total").isIntegralNumber());
        assertEquals(inserted + modified + deleted, diffList.get("total").intValue());
    }

    static void checkDiffFields(JsonNode diff, long id, String type, int rowStart, int rowEnd, int offset, int fromRowStart, int fromRowEnd, String lines) {
        // id
        //checkId(diff, id);

        // type
        assertEquals(type, diff.get("type").textValue());

        // rowStart
        assertTrue(diff.get("rowStart").isIntegralNumber());
        assertEquals(rowStart, diff.get("rowStart").intValue());

        // rowEnd
        assertTrue(diff.get("rowEnd").isIntegralNumber());
        assertEquals(rowEnd, diff.get("rowEnd").intValue());

        // offset
        assertTrue(diff.get("offset").isIntegralNumber());
        assertEquals(offset, diff.get("offset").intValue());

        if (type.equals("delete")) {
            // fromRowStart
            assertTrue(diff.get("fromRowStart").isIntegralNumber());
            assertEquals(fromRowStart, diff.get("fromRowStart").intValue());

            // fromRowEnd
            assertTrue(diff.get("fromRowEnd").isIntegralNumber());
            assertEquals(fromRowEnd, diff.get("fromRowEnd").intValue());

            // lines
            assertEquals(lines, diff.get("lines").textValue());
        } else {
            // fromRowStart
            assertTrue(!diff.has("fromRowStart") || diff.get("fromRowStart").isNull());

            // fromRowEnd
            assertTrue(!diff.has("fromRowEnd") || diff.get("fromRowEnd").isNull());

            // lines
            assertTrue(!diff.has("lines") || diff.get("lines").isNull());
        }
    }

    static void checkConceptFields(JsonNode concept) {
        // name
        assertTrue(concept.has("name"));
        assertTrue(concept.get("name").isTextual());

        // size
        assertTrue(concept.has("size"));
        assertTrue(concept.get("size").isNumber());
    }

    static void checkTagFields(JsonNode tag, long id) {
        checkId(tag, id);
    }

    static void checkTagNameFields(JsonNode tag, long id, String name) {
        checkIdAndName(tag, id, name);
    }

    static void checkStudentGroupFields(JsonNode studentGroup, long id, String name) {
        checkIdAndName(studentGroup, id, name);
    }
}
