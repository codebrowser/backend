
package rage.codebrowser.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class TagCategory extends AbstractNamedPersistable implements Comparable<TagCategory> {
    
    /**
     * TagNames that are in this category.
     */
    @OneToMany
    @JsonIgnoreProperties({"tags"})
    private List<TagName> tagnames;

    @Override
    public int compareTo(TagCategory o) {
         if (getName() == null) {
            return -1;
        }
        return getName().compareTo(o.getName());
    }

    /**
     * @return the tagnames
     */
    public List<TagName> getTagnames() {
        if (tagnames == null) {
            tagnames = new ArrayList<TagName>();
        }
        return tagnames;
    }
    
    /**
     * Add Tagname to list
     * @param tag 
     */
    public void addTagName(TagName tagname) {
        if (!getTagnames().contains(tagname)) {
            getTagnames().add(tagname);
        }
    }
    
    public void removeTagName(TagName tagname) {
        tagnames.remove(tagname);
    }

    /**
     * @param tagnames the tagnames to set
     */
    public void setTagnames(List<TagName> tagnames) {
        this.tagnames = tagnames;
    }
    
}
