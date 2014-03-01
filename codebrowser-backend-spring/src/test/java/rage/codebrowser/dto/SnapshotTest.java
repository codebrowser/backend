
package rage.codebrowser.dto;

import java.util.ArrayList;
import org.junit.Test;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.*;

public class SnapshotTest {
    private Snapshot ss;
    private List<Testresult> tests;
    
    public SnapshotTest() {
         ss = new Snapshot();
         tests = new ArrayList();
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testPercentageWhenNullStudentList() {
        assertEquals(ss.getPercentageOfTestsPassing(), null);
    }

    @Test
    public void testPercentageWhenNoStudents() {
        ss.setTests(tests);
        assertEquals(null, ss.getPercentageOfTestsPassing());
    }
    
    @Test
    public void testPercentageWhenStudentsAndOneTestPassing() {
        Testresult test = new Testresult();
        test.setName("test1");
        test.setMessage("Test1 output");
        test.setPassed(true);
        tests.add(test);
        Testresult test2 = new Testresult();
        test2.setName("test2");
        test2.setMessage("Test2 output");
        test2.setPassed(false);
        tests.add(test2);
        ss.setTests(tests);
        assertEquals(new Integer(50), ss.getPercentageOfTestsPassing());
    }
    
    @Test
    public void testPercentageWhenStudentsAndAllTestPAssing() {
        Testresult test = new Testresult();
        test.setName("test1");
        test.setMessage("Test1 output");
        test.setPassed(true);
        tests.add(test);
        Testresult test2 = new Testresult();
        test2.setName("test2");
        test2.setMessage("Test2 output");
        test2.setPassed(true);
        tests.add(test2);
        ss.setTests(tests);
        assertEquals(new Integer(100), ss.getPercentageOfTestsPassing());
    }
    
    @Test
    public void testPercentageWhenStudentsAndNoTestPassing() {
        Testresult test = new Testresult();
        test.setName("test1");
        test.setMessage("Test1 output");
        test.setPassed(false);
        tests.add(test);
        Testresult test2 = new Testresult();
        test2.setName("test2");
        test2.setMessage("Test2 output");
        test2.setPassed(false);
        tests.add(test2);
        ss.setTests(tests);
        assertEquals(new Integer(0), ss.getPercentageOfTestsPassing());
    }
    
    @Test
    public void testPercentageWhenStudentsAndOneThirdTestPassing() {
        Testresult test = new Testresult();
        test.setName("test1");
        test.setMessage("Test1 output");
        test.setPassed(false);
        tests.add(test);
        Testresult test2 = new Testresult();
        test2.setName("test2");
        test2.setMessage("Test2 output");
        test2.setPassed(false);
        tests.add(test2);
        test = new Testresult();
        test.setName("test1");
        test.setMessage("Test1 output");
        test.setPassed(true);
        tests.add(test);
        ss.setTests(tests);
        assertEquals(new Integer(33), ss.getPercentageOfTestsPassing());
    }
    
}
