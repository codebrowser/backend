package rage.codebrowser.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import rage.codebrowser.dto.Student;
import rage.codebrowser.dto.StudentGroup;
import rage.codebrowser.repository.StudentGroupRepository;

@Controller
public class StudentGroupRESTController {

    @Autowired
    private StudentGroupRepository studentGroupRepository;

    @RequestMapping(value = {"studentgroups"})
    @ResponseBody
    public List<StudentGroup> getGroups() {
        return studentGroupRepository.findAll();
    }

    @RequestMapping(value = {"studentgroups/{studentGroupId}"})
    @ResponseBody
    public StudentGroup getCourse(@PathVariable("studentGroupId") StudentGroup studentGroup) {
        return studentGroup;
    }

    @RequestMapping(value = {"studentgroups/{studentGroupId}/students"})
    @ResponseBody
    public List<Student> getGroupStudents(@PathVariable("studentGroupId") StudentGroup studentGroup) {
        return studentGroup.getStudents();
    }

    @RequestMapping(value = {"studentgroups/{studentGroupId}/students/{studentId}"})
    @ResponseBody
    public Student getGroupStudent(@PathVariable("studentId") Student student) {
        return student;
    }
}
