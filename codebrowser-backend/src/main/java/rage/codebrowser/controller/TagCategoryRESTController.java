
package rage.codebrowser.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import rage.codebrowser.dto.TagCategory;
import rage.codebrowser.dto.TagName;
import rage.codebrowser.repository.TagCategoryRepository;
import rage.codebrowser.repository.TagNameRepository;


@Controller
public class TagCategoryRESTController {
    
    @Autowired
    private TagCategoryRepository tagCategoryRepository;
    @Autowired
    private TagNameRepository tagNameRepository;
    
    
    @RequestMapping(value = {"tagcategories"})
    @ResponseBody
    public List<TagCategory> getTagCategories() {
        return tagCategoryRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }
    
    @RequestMapping(value = {"tagcategories/{tagCategoryId}"}, method = RequestMethod.GET)
    @ResponseBody
    public TagCategory getTagCategory(@PathVariable("tagCategoryId") TagCategory tagCategory) {
        return tagCategory;
    }
        
    @RequestMapping(value = {"tagcategories/{tagCategoryId}/tagnames"})
    @ResponseBody
    public List<TagName> getTagCategoryTagNames(@PathVariable("tagCategoryId") TagCategory tagCategory) {
        return tagCategory.getTagnames();
    }
    
    @RequestMapping(value = {"tagcategories/{tagCategoryId}/tagnames/snapshots"})
    @ResponseBody
    public List<TagName> getTagCategoryTagNamesForSnapshots(@PathVariable("tagCategoryId") TagCategory tagCategory) {
        List<TagName> snapshotTagNames = tagNameRepository.findSnapshotTagNames();
        List<TagName> categoryTags = tagCategory.getTagnames();
        List<TagName> categorySnapshotTags = new ArrayList();
        for (TagName tagName : categoryTags) {
            if (snapshotTagNames.contains(tagName)) {
                categorySnapshotTags.add(tagName);
            }
        }
        return categorySnapshotTags;
    }
    
    @RequestMapping(value = {"tagcategories/{tagCategoryId}/tagnames/exercises"})
    @ResponseBody
    public List<TagName> getTagCategoryTagNamesForExercises(@PathVariable("tagCategoryId") TagCategory tagCategory) {
        List<TagName> exerciseTagNames = tagNameRepository.findExerciseAnswerTagNames();
        List<TagName> categoryTags = tagCategory.getTagnames();
        List<TagName> categorySnapshotTags = new ArrayList();
        for (TagName tagName : categoryTags) {
            if (exerciseTagNames.contains(tagName)) {
                categorySnapshotTags.add(tagName);
            }
        }
        return categorySnapshotTags;
    }
    
    @RequestMapping(value = {"tagcategories/{tagCategoryId}/tagnames/unused"})
    @ResponseBody
    public List<TagName> getUnusedTagCategoryTagNames(@PathVariable("tagCategoryId") TagCategory tagCategory) {
        List<TagName> allTagNames = tagNameRepository.findAll();
        List<TagName> categoryTags = tagCategory.getTagnames();
        List<TagName> unusedTags = new ArrayList();
        for (TagName tagName : allTagNames) {
            if (!categoryTags.contains(tagName)) {
                unusedTags.add(tagName);
            }
        }
        return unusedTags;
    }
    
    @RequestMapping(value = {"tagcategories/{tagCategoryId}"}, method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    @ResponseBody
    public TagCategory postTagCategoryTagNames(@RequestBody TagCategory tagcategory) {
        TagCategory existing = tagCategoryRepository.findByName(tagcategory.getName());
        for (TagName tagname : tagcategory.getTagnames()) {
            if (tagname.addTagCategory(tagcategory)) {
                tagNameRepository.save(tagname);
                existing.addTagName(tagname);
            }
        }
        tagCategoryRepository.saveAndFlush(existing);
        return existing;
    }
        
    @RequestMapping(value = {"tagcategories"}, method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseBody
    public TagCategory postTagategory(@RequestBody TagCategory tagCategory) {
        TagCategory existing = tagCategoryRepository.findByName(tagCategory.getName());
        if (existing != null) {
            return existing;
        }
        return tagCategoryRepository.saveAndFlush(tagCategory);
    }
    
    @RequestMapping(value = {"tagcategories/{tagCategoryId}"}, method = RequestMethod.DELETE)
    @ResponseBody
    public TagCategory deleteTagCategory(@PathVariable("tagCategoryId") TagCategory tagCategory) {
        for (TagName tagname : tagCategory.getTagnames()) {
            tagname.removeTagCategory(tagCategory);
            tagNameRepository.save(tagname);
        }
        tagCategoryRepository.delete(tagCategory);
        return tagCategory;
    }
    
    @RequestMapping(value = {"tagcategories/{tagCategoryId}/tagnames/{tagNameId}"}, method = RequestMethod.PUT)
    @ResponseBody
    public TagCategory deleteTagFromCategory(@PathVariable("tagCategoryId") TagCategory tagCategory, @PathVariable("tagNameId") TagName tagName) {
        tagCategory.removeTagName(tagName);
        tagName.removeTagCategory(tagCategory);
        tagCategoryRepository.saveAndFlush(tagCategory);
        return tagCategory;
    }
}
