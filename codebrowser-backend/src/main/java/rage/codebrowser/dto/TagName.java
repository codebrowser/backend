package rage.codebrowser.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

/**
 * Text that can be shared by multiple tag instances.
 */
@Entity
public class TagName extends AbstractNamedPersistable implements Comparable<TagName> {

    /**
     * Tags that are using this text.
     */
    @OneToMany(mappedBy = "tagName")
    private List<Tag> tags;
    
    /**
     * Categories that this tagName is listed in
     */
    @ManyToMany
    @JsonIgnoreProperties({"tagnames"})
    private List<TagCategory> tagCategories;

    public List<Tag> getTags() {
        if (tags == null) {
            tags = new ArrayList<Tag>();
        }
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public void addTag(Tag tag) {
        if (!getTags().contains(tag)) {
            getTags().add(tag);
        }
    }

    @Override
    public int compareTo(TagName o) {
        if (getName() == null) {
            return -1;
        }

        return getName().compareTo(o.getName());
    }

    /**
     * @return the tagCategories
     */
    public List<TagCategory> getTagCategories() {
        if (tagCategories == null) {
            return new ArrayList<TagCategory>();
        }
        return tagCategories;
    }

    /**
     * @param tagCategories the tagCategories to set
     */
    public void setTagCategories(List<TagCategory> tagCategories) {
        this.tagCategories = tagCategories;
    }
    
    /**
     * Adds a category for tagName if category has not been added yet
     * @param tagCategory category to be added
     * @return true if category was added, false otherwise
     */
    public boolean addTagCategory(TagCategory tagCategory) {
        if (!getTagCategories().contains(tagCategory)) {
            getTagCategories().add(tagCategory);
            return true;
        }
        return false;
    }
    
    /**
     * 
     * @param tagCategory to be removed
     */
    public void removeTagCategory(TagCategory tagCategory) {
        getTagCategories().remove(tagCategory);
    }
}
