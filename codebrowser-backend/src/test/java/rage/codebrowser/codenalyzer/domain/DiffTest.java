package rage.codebrowser.codenalyzer.domain;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import rage.codebrowser.codeanalyzer.domain.Diff;

public class DiffTest {
    Diff diff;
    
    @Before
    public void setUp() {
        diff = new Diff();
        diff.setType("insert");
        diff.setRowStart(1);
        diff.setRowEnd(5);
        diff.setFromRowStart(0);
        diff.setLines("");
    }
    
    @Test
    public void testGetOffset() {
        assertEquals(0, diff.getOffset());
    }
    
    @Test
    public void testGetLines() {
        assertEquals(null, diff.getLines());
    }
    
    @Test
    public void testGetFromRowStart() {
        assertEquals(null, diff.getFromRowStart());
    }
    
    @Test
    public void testGetFromRowStartForDelete() {
        diff.setType("delete");
        assertTrue(diff.getFromRowStart().equals(0));
    }
    
    @Test
    public void testGetLinesForDelete() {
        diff.setType("delete");
        assertEquals("", diff.getLines());
    }
}
