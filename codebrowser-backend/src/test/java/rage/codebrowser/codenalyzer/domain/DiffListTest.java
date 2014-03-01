package rage.codebrowser.codenalyzer.domain;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import rage.codebrowser.codeanalyzer.domain.Diff;
import rage.codebrowser.codeanalyzer.domain.DiffList;

public class DiffListTest {
    Diff diff;
    List<Diff> diffs;
    DiffList diffList;
    
    @Before
    public void setUp() {
        diff = new Diff();
        diff.setType("insert");
        diff.setRowStart(1);
        diff.setRowEnd(5);
        
        diffs = new ArrayList();
        diffs.add(diff);
        
        diffList = new DiffList();
        diffList.setDifferences(diffs);
    }
    
    @Test
    public void testGetDiffs() {
        assertEquals(1, diffList.getDifferences().size());
        assertEquals("insert", diffList.getDifferences().get(0).getType());
    }
    
    @Test
    public void testGetInserted() {
        assertEquals(5, diffList.getInserted());
    }
    
    @Test
    public void testGetDeletedWhenNoDeletions() {
        assertEquals(0, diffList.getDeleted());
    }
    
    @Test
    public void testGetModifiedWhenNoModifications() {
        assertEquals(0, diffList.getModified());
    }
    
    @Test
    public void testGetTotal() {
        assertEquals(5, diffList.getTotal());
    }
    
    @Test
    public void testGetDelted() {
        Diff diff2 = new Diff();
        diff2.setType("delete");
        diff2.setRowStart(5);
        diff2.setRowEnd(5);
        diffs.add(diff2);
        diffList.setDifferences(diffs);
        assertEquals(1, diffList.getDeleted());
    }
    
    @Test
    public void testGetModified() {
        diff = new Diff();
        diff.setType("replace");
        diff.setRowStart(6);
        diff.setRowEnd(7);
        diffs.add(diff);
        diffList.setDifferences(diffs);
        assertEquals(2, diffList.getModified());
    }
}
