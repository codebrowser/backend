package rage.codebrowser.codenalyzer.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.junit.After;
import static org.junit.Assert.*;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import rage.codebrowser.codeanalyzer.domain.DiffList;
import rage.codebrowser.codeanalyzer.service.CountDiffs;

public class CountDiffsTest {
    File tempFile;
    File prevTempFile;
    DiffList diffList;
    CountDiffs counter;
    
    public CountDiffsTest() {
        counter = new CountDiffs();
    }
    
    @Before
    public void setUp() {
        
        try {
            tempFile = File.createTempFile("DiffTempData", "java");
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
                    + "        System.out.print(\"Next Line will be deleted: \");\n"
                    + "}");
            out.close();
            
        } catch (IOException ex) {
            fail("The test case can't proceed without in memory file.");
        }
        
        try {
            prevTempFile = File.createTempFile("DiffPrevTempData", "java");
            prevTempFile.deleteOnExit();

            BufferedWriter out = new BufferedWriter(new FileWriter(prevTempFile));
            out.write("import java.util.Scanner;\n"
                    + "\n"
                    + "public class LopetusMuistaminen {\n"
                    + "\n"
                    + "    public static void main(String[] args) {\n"
                    + "        System.out.print(\"Next Line will be deleted: \");\n"
                    + "        System.out.print(\"This Line will be deleted: \");\n"
                    + "}");
            out.close();
            
        } catch (IOException ex) {
            fail("The test case can't proceed without in memory file.");
        }
        
        diffList = counter.getDifferences(prevTempFile.getPath(), tempFile.getPath());
    }
     
    @After
    public void tearDown() {
        tempFile.delete();
        prevTempFile.delete();
    }
    
    @Test
    public void testCorrectAmountOfDiffs() {
        assertEquals(3, diffList.getDifferences().size());
    }
    
    @Test
    public void testCorrectTypesOfDiffs() {
        assertEquals("replace", diffList.getDifferences().get(0).getType());
        assertEquals("insert", diffList.getDifferences().get(1).getType());
        assertEquals("delete", diffList.getDifferences().get(2).getType());
    }
    
    @Test
    public void testCorrectAmountOfInsertedLines() {
        assertEquals(3, diffList.getInserted());
    }
    
    @Test
    public void testCorrectAmountOfModifiedLines() {
        assertEquals(1, diffList.getModified());
    }
    
    @Test
    public void testCorrectAmountOfDeletedLines() {
        assertEquals(1, diffList.getDeleted());
    }
    
    @Test
    public void testCorrectStartPosForInsert() {
        assertEquals(5, diffList.getDifferences().get(1).getRowStart());
    }
    
    @Test
    public void testCorrectEntPosForInsert() {
        assertEquals(7, diffList.getDifferences().get(1).getRowEnd());
    }
    
    @Test
    public void testCorrectStartPosForDelete() {
        assertEquals(9, diffList.getDifferences().get(2).getRowStart());
    }
    
    @Test
    public void testCorrectEntPosForDelete() {
        assertEquals(9, diffList.getDifferences().get(2).getRowEnd());
    }
    
    @Test
    public void testCorrectFromStartPosForDelete() {
        assertEquals(new Integer(6), diffList.getDifferences().get(2).getFromRowStart());
    }
    
    @Test
    public void testCorrectFromEndPosForDelete() {
        assertEquals(new Integer(6), diffList.getDifferences().get(2).getFromRowEnd());
    }
    
    @Test
    public void testOffsetForDelete() {
        assertEquals(0, diffList.getDifferences().get(2).getOffset());
    }
    
     @Test
    public void testLinesForDelete() {
        assertEquals("        System.out.print(\"This Line will be deleted: \");\n", diffList.getDifferences().get(2).getLines());
    }
    @Test
    public void testAllInsertsWhenNoPrevFile() {
        diffList = counter.getDifferences(null, tempFile.getPath());
        assertEquals(10, diffList.getInserted());
    }
    
    @Test
    public void testNoDiffsWhenTwoSameFiles() {
        diffList = counter.getDifferences(tempFile.getPath(), tempFile.getPath());
        assertEquals(0, diffList.getDifferences().size());
    }
   
    
}
