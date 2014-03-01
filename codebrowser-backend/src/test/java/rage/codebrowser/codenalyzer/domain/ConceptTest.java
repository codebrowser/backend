
package rage.codebrowser.codenalyzer.domain;

import rage.codebrowser.codeanalyzer.domain.Concept;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class ConceptTest {
    
    
    private Concept foo;
    private Concept fooOther;
    private Concept fuu;
    
    public ConceptTest() {
    }
    
    @Before
    public void setUp() {
        foo = new Concept("foo", 1.0);
        fooOther = new Concept("foo", 10.0);
        fuu = new Concept("fuu", 1.0);
    }


    @Test
    public void testEqualsWithSameName() {
        assertTrue(foo.equals(fooOther));
    }

    @Test
    public void testEqualsWithSelf() {
        assertTrue(foo.equals(foo));
    }

    @Test
    public void testNotEqualWithOther() {
        assertFalse(foo.equals(fuu));
    }

    @Test
    public void testNotEqualWithNull() {
        assertFalse(foo.equals(null));
    }

}