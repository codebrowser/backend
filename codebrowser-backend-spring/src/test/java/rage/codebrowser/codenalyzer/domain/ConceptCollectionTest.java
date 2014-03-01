
package rage.codebrowser.codenalyzer.domain;

import rage.codebrowser.codeanalyzer.domain.ConceptCollection;
import rage.codebrowser.codeanalyzer.domain.Concept;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class ConceptCollectionTest {
    
    
    
    ConceptCollection subject;
    Concept needle;
    
    
    public ConceptCollectionTest() {
    }
    
    @Before
    public void setUp() {
        subject = new ConceptCollection();
        
        subject.add( new Concept("foo", 1.0) );
        subject.add( new Concept("bar", 2.0) );
        subject.add( new Concept("baz", 3.0) );
        
        needle = new Concept("needle", 123.0);
        subject.add(needle);
    }
    


    @Test
    public void testContains() {
        assertTrue(subject.contains("baz"));
    }

    @Test
    public void testContainsCaseSensitive() {
        assertFalse(subject.contains("Foo"));
    }

    @Test
    public void testGetByName() {
        assertEquals(needle, subject.getByName("needle"));
    }

    @Test(expected=IllegalStateException.class)
    public void testGetByNameThrows() {
       subject.getByName("nowhere to be found!");
    }
    
    @Test
    public void testAdd() {
        String name = "all new foo";
        Concept concept = new Concept(name, 0);
        int expectedSize = subject.size() + 1;
        
        assertTrue( subject.add(concept) );
        assertEquals(expectedSize, subject.size());
        assertTrue( subject.contains(name) );
        assertEquals(concept, subject.getByName(name));
    }
    
    @Test
    public void testAddingConceptWithSameName() {
        int expected = subject.size();
        
        assertFalse( subject.add(new Concept("foo", 213213)) );
        assertEquals(expected, subject.size());
    }

    @Test
    public void testCombine() {
        ConceptCollection other = new ConceptCollection();
        other.add(new Concept("foo", 5.0));
        
        int expectedSize = subject.size();
        double expextedFooSize = subject.getByName("foo").size + 5.0;
        
        subject.combine(other);
        
        assertEquals(expectedSize, subject.size());
        assertEquals(expextedFooSize, subject.getByName("foo").size, 0.05);
    }
}

