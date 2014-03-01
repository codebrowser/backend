/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rage.codebrowser.codenalyzer.service;

import rage.codebrowser.codeanalyzer.service.SnapshotConcepts;
import rage.codebrowser.codeanalyzer.domain.ConceptCollection;
import rage.codebrowser.codeanalyzer.service.CountWords;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import rage.codebrowser.dto.SnapshotFile;

public class CountWordsTest {

    private final double epsilon = 0.05;
    
    public CountWordsTest() {
    }
    File tempFile;
    SnapshotFile tempSnapshot;
    SnapshotConcepts countWords;

    @Before
    public void setUp() {
        tempSnapshot = new SnapshotFile();
        countWords = new CountWords();

        try {
            tempFile = File.createTempFile("SnapshotFileConceptsTestData", "java");
            tempFile.deleteOnExit();

            BufferedWriter out = new BufferedWriter(new FileWriter(tempFile));
            out.write("import java.util.Scanner;\n"
                    + "\n"
                    + "public class SilmukatLopetusMuistaminen {\n"
                    + "\n"
                    + "    public static void main(String[] args) {\n"
                    + "        Scanner lukija = new Scanner(System.in);\n"
                    + "\n"
                    + "        System.out.print(\"Syötä luvut: \");\n"
                    + "\n"
                    + "\n"
                    + "        int summa = 0;\n"
                    + "        int luku = Integer.parseInt(lukija.nextLine());\n"
                    + "\n"
                    + "        while (luku != -1) {\n"
                    + "            summa = summa + luku;\n"
                    + "\n"
                    + "\n"
                    + "            if (luku == - 1) {\n"
                    + "            }\n"
                    + "            break;\n"
                    + "        }\n"
                    + "\n"
                    + "        System.out.println(\"Kiitos ja näkemiin!\");\n"
                    + "        System.out.println(\"Summa: \" + summa);\n"
                    + "\n"
                    + "    }\n"
                    + "}");
            out.close();

            tempSnapshot.setFilepath(tempFile.getPath());
        } catch (IOException ex) {
            fail("The test case can't proceed without in memory file.");
        }
    }

    @After
    public void tearDown() {
        tempFile.delete();
    }

    @Test
    public void testDoesntFindPrivate() {
        ConceptCollection concepts = countWords.getConcepts(tempSnapshot);

        assertFalse(concepts.contains("private"));
    }

    @Test
    public void testFindsPublic() {
        ConceptCollection concepts = countWords.getConcepts(tempSnapshot);

        assertTrue(concepts.contains("public"));
        assertEquals(2.0, concepts.getByName("public").size, epsilon);
    }

    @Test
    public void testFindsAssignments() {
        ConceptCollection concepts = countWords.getConcepts(tempSnapshot);

        assertTrue(concepts.contains("="));
        assertEquals(4.0, concepts.getByName("=").size, epsilon);
    }

    @Test
    public void testReturnsErrorWithNonExistingFile() {
        tempFile.delete();
        ConceptCollection concepts = countWords.getConcepts(tempSnapshot);

        assertTrue(concepts.contains("error"));
    }
}