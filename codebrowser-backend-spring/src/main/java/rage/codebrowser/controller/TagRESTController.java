package rage.codebrowser.controller;

import java.util.ArrayList;
import java.util.Collections;
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
import rage.codebrowser.dto.Course;
import rage.codebrowser.dto.Exercise;
import rage.codebrowser.dto.Snapshot;
import rage.codebrowser.dto.Student;
import rage.codebrowser.dto.Tag;
import rage.codebrowser.dto.TagCategory;
import rage.codebrowser.dto.TagName;
import rage.codebrowser.repository.TagCategoryRepository;
import rage.codebrowser.repository.TagNameRepository;
import rage.codebrowser.repository.TagRepository;

@Controller
public class TagRESTController {

    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private TagNameRepository tagNameRepository;
    @Autowired
    private TagCategoryRepository tagCategoryRepository;

    @RequestMapping(value = {"tagnames"})
    @ResponseBody
    public List<TagName> getTagNames() {
        return tagNameRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }

    @RequestMapping(value = {"tagnames/exerciseanswers"})
    @ResponseBody
    public List<TagName> getExerciseAnswerTagNames() {
        return tagNameRepository.findExerciseAnswerTagNames();
    }

    @RequestMapping(value = {"tagnames/snapshots"})
    @ResponseBody
    public List<TagName> getSnapshotTagNames() {
        return tagNameRepository.findSnapshotTagNames();
    }

    @RequestMapping(value = {"tagnames/{tagNameId}"})
    @ResponseBody
    public TagName getTagName(@PathVariable("tagNameId") TagName tagName) {
        return tagNameRepository.saveAndFlush(tagName);
    }

    @RequestMapping(value = {"tagnames/{tagNameId}/tags"})
    @ResponseBody
    public List<Tag> getTagNameTags(@PathVariable("tagNameId") TagName tagName) {
        return tagName.getTags();
    }

    @RequestMapping(value = {"tagnames"}, method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseBody
    public TagName postTagName(@RequestBody TagName tagName) {
        TagName existing = tagNameRepository.findByName(tagName.getName());
        if (existing != null) {
            return existing;
        }
        return tagNameRepository.saveAndFlush(tagName);
    }
    
    
    @RequestMapping(value = {"students/{studentId}/courses/{courseId}/exercises/{exerciseId}/tags"},
            method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<Tag> getTags(@PathVariable("studentId") Student student, @PathVariable("courseId") Course course, @PathVariable("exerciseId") Exercise exercise) {
        List<Tag> tags = tagRepository.findByStudentAndCourseAndExercise(student, course, exercise);
        if (tags == null) {
            tags = new ArrayList<Tag>();
        }

        Collections.sort(tags);
        return tags;
    }

    @RequestMapping(value = {"students/{studentId}/courses/{courseId}/exercises/{exerciseId}/tags"},
            method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseBody
    @Transactional
    public Tag postTag(@PathVariable("studentId") Student student, @PathVariable("courseId") Course course, @PathVariable("exerciseId") Exercise exercise, @RequestBody Tag tag) {
        return postTag(student, course, exercise, null, tag);
    }

    @RequestMapping(value = {"students/{studentId}/courses/{courseId}/exercises/{exerciseId}/snapshots/{snapshotId}/tags"},
            method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseBody
    @Transactional
    public Tag postTag(@PathVariable("studentId") Student student, @PathVariable("courseId") Course course, @PathVariable("exerciseId") Exercise exercise, @PathVariable("snapshotId") Snapshot snapshot, @RequestBody Tag tag) {
        tag.setCourse(course);
        tag.setStudent(student);
        tag.setExercise(exercise);
        tag.setSnapshot(snapshot);

        TagName tagName = tagNameRepository.findByName(tag.getTagName().getName());
        if (tagName == null) {
            tagName = tagNameRepository.saveAndFlush(tag.getTagName());
        }

        tag.setTagName(tagName);
        tagName.addTag(tag);

        return tagRepository.saveAndFlush(tag);
    }

    @RequestMapping(value = {"students/{studentId}/courses/{courseId}/exercises/{exerciseId}/tags/{tagId}"},
            method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Tag getTag(@PathVariable("studentId") Student student, @PathVariable("courseId") Course course, @PathVariable("exerciseId") Exercise exercise, @PathVariable("tagId") Long tagId) {
        return tagRepository.findOne(tagId);
    }

    @RequestMapping(value = {"students/{studentId}/courses/{courseId}/exercises/{exerciseId}/tags/{tagId}",
        "students/{studentId}/courses/{courseId}/exercises/{exerciseId}/snapshots/{snapshotId}/tags/{tagId}"},
            method = RequestMethod.DELETE)
    @ResponseBody
    @Transactional
    public Tag deleteTag(@PathVariable("tagId") Tag tag) {        
        TagName tagName = tag.getTagName();
        tagName.getTags().remove(tag);
        tagRepository.delete(tag);
        if (tagName.getTags().isEmpty()) {
            for (TagCategory tagcategory : tagName.getTagCategories()) {
                tagcategory.removeTagName(tagName);
                tagCategoryRepository.save(tagcategory);
            }
            tagNameRepository.delete(tagName);
        } else {
            tagNameRepository.save(tagName);
        }
        return tag;
    }

}
