
package rage.codebrowser.dto;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TagCategoryTest {
    private TagCategory tagCategory;
    private TagName tagname;
    
     public TagCategoryTest() {
         tagCategory = new TagCategory();
         tagCategory.setName("aab");
         tagname = new TagName();
         tagname.setName("first");
         tagCategory.addTagName(tagname);
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
    public void compareSortsAlphabetically() {
        TagCategory another = new TagCategory();
        another.setName("abs");
        assertEquals(-1, tagCategory.compareTo(another));
    }

    @Test
    public void testAddTagName() {
        assertEquals(1, tagCategory.getTagnames().size());
    }
    
    @Test
    public void testAddTagNameWithSameName() {
        TagName another = new TagName();
        another.setName("first");
        tagCategory.addTagName(tagname);
        assertEquals(1, tagCategory.getTagnames().size());
    }
    
    @Test
    public void testAddTagNameWithDifferentName() {
        TagName another = new TagName();
        another.setName("second");
        tagCategory.addTagName(another);
        assertEquals(2, tagCategory.getTagnames().size());
    }
    
    @Test
    public void removeTagName() {
        tagCategory.removeTagName(tagname);
        assertTrue(!tagCategory.getTagnames().contains(tagname));
    }
}


