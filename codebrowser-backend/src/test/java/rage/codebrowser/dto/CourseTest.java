
package rage.codebrowser.dto;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


public class CourseTest {
    private Course course;
    private List<Student> students;
    
    public CourseTest() {
         course = new Course();
         students = new ArrayList();
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
    public void testSetAmounOfStudentsWhenNullStudentList() {
        assertEquals(course.getAmountOfStudents(), 0);
    }

    @Test
    public void testSetAmounOfStudentsWhenNoStudents() {
        course.setStudents(students);
        assertEquals(course.getAmountOfStudents(), 0);
    }
    
    @Test
    public void testSetAmounOfStudentsWhenStudents() {
        students.add(new Student());
        course.setStudents(students);
        assertEquals(course.getAmountOfStudents(), 1);
    }
}
