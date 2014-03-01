package rage.codebrowser.init;

import java.io.File;
import java.io.FilenameFilter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rage.codebrowser.codeanalyzer.service.CountDiffs;
import rage.codebrowser.dto.Course;
import rage.codebrowser.dto.Exercise;
import rage.codebrowser.dto.ExerciseAnswer;
import rage.codebrowser.dto.Snapshot;
import rage.codebrowser.dto.SnapshotFile;
import rage.codebrowser.dto.Student;
import rage.codebrowser.dto.StudentGroup;
import rage.codebrowser.dto.Testresult;
import rage.codebrowser.repository.CourseRepository;
import rage.codebrowser.repository.DiffListRepository;
import rage.codebrowser.repository.DiffRepository;
import rage.codebrowser.repository.ExerciseAnswerRepository;
import rage.codebrowser.repository.ExerciseRepository;
import rage.codebrowser.repository.SnapshotFileRepository;
import rage.codebrowser.repository.SnapshotRepository;
import rage.codebrowser.repository.StudentGroupRepository;
import rage.codebrowser.repository.StudentRepository;
import rage.codebrowser.repository.TestRepository;

@Component
public class FileRepositoryInitService {

    @Autowired
    private StudentGroupRepository studentGroupRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private SnapshotRepository snapshotRepository;

    @Autowired
    private SnapshotFileRepository snapshotFileRepository;

    @Autowired
    private ExerciseAnswerRepository eaRepository;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private DiffListRepository diffsRepository;

    @Autowired
    private DiffRepository dr;

    @Autowired
    private CountDiffs counter;

    private Course course;

    private static class FileComparator implements Comparator<File> {

        @Override
        public int compare(File o1, File o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }

    public void initCourseData(String courseName, int maxStudentsToConsider, int minStudentSnapshots, String dataPath, String... exercisesToAccept) {
        readCourseDirectory(courseName, maxStudentsToConsider, minStudentSnapshots, dataPath, exercisesToAccept);
        countDifferencesForFiles();
    }

    private void countDifferencesForFiles() {
        for (Student student : course.getStudents()) {
            for (Exercise excercise : course.getExercises()) {
                ExerciseAnswer ea = eaRepository.findByStudentAndExercise(student, excercise);
                if (ea != null) {
                    List<Snapshot> snapshots = ea.getSnapshots();
                    Collections.sort(snapshots);
                    countDifferencesForSnapshots(snapshots);
                }
            }
        }
    }

    private void countDifferencesForSnapshots(List<Snapshot> snapshots) {
        for (int i = 0; i < snapshots.size(); i++) {
            List<SnapshotFile> files = snapshots.get(i).getFiles();

            for (int j = 0; j < files.size(); j++) {
                SnapshotFile current = files.get(j);
                //If index is 0, there is no previous snapshot or file
                if (i == 0) {
                    current.setDiffs(counter.getDifferences(null, current.getFilepath()));
                    snapshotFileRepository.save(current);
                } else {
                    List<SnapshotFile> previousFiles = snapshots.get(i - 1).getFiles();
                    //previous file is null unless a file with same name is found in previous snapshot
                    SnapshotFile previous = null;
                    for (SnapshotFile snapshotFile : previousFiles) {
                        if (snapshotFile.getName().equals(current.getName())) {
                            previous = snapshotFile;
                        }
                    }
                    current.setDiffs(counter.getDifferences(previous == null ? null : previous.getFilepath(), current.getFilepath()));
                    snapshotFileRepository.save(current);
                }
            }
        }
    }

    private void readCourseDirectory(String courseName, int maxStudentsToConsider, int minStudentSnapshots, String dataPath, String... exercisesToAccept) {
        course = new Course();
        course.setName(courseName);
        course = courseRepository.save(course);

        int studentCount = 0;

        File[] studentDirs = new File(dataPath).listFiles();
        Arrays.sort(studentDirs, new FileComparator());

        for (File studentDir : studentDirs) {
            if (studentCount >= maxStudentsToConsider) {
                break;
            }

            if (readStudentDirectory(studentDir, Arrays.asList(exercisesToAccept), minStudentSnapshots)) {
                studentCount++;
            }
        }
    }

    private boolean readStudentDirectory(File studentDir, List<String> exerciseList, int minStudentSnapshots) {
        if (!studentDir.isDirectory()) {
            return false;
        }

        // find exercises that this student has worked on
        // 1. snapshots
        File[] studentSnapshotDirs = studentDir.listFiles();

        Arrays.sort(studentSnapshotDirs, new FileComparator());

        Map<String, List<File>> snapshotDirs = getAcceptedSnapshotDirs(studentSnapshotDirs, exerciseList);

        // 2. exercises
        int acceptedSnapshotCount = 0;
        for (List<File> exerciseSnapshots : snapshotDirs.values()) {
            acceptedSnapshotCount += exerciseSnapshots.size();
        }

        if (acceptedSnapshotCount < minStudentSnapshots) {
            return false;
        }

        String studentName = "student_" + studentDir.getName();
        Student student = studentRepository.findByName(studentName);
        if (student == null) {
            student = new Student();
            student.setName(studentName);
            student = studentRepository.save(student);
            addStudentToRandomGroups(student);
        }

        if (!course.getStudents().contains(student)) {
            course.getStudents().add(student);
            course = courseRepository.save(course);
        }

        if (!student.getCourses().contains(course)) {
            student.getCourses().add(course);
            student = studentRepository.save(student);
        }

        for (String exerciseName : snapshotDirs.keySet()) {
            readSnapshotsForExercise(student, exerciseName, snapshotDirs);
        }

        return true;
    }

    private void addStudentToRandomGroups(Student student) {
        student.setGroups(new ArrayList<StudentGroup>());

        for (char i = 0; i < 5; i++) {
            if (Math.random() < 0.4) {
                continue;
            }

            String groupName = "Group " + (char) ('A' + i);

            StudentGroup group = studentGroupRepository.findByName(groupName);
            if (group == null) {
                group = new StudentGroup();
                group.setName(groupName);
                group.setStudents(new ArrayList<Student>());
            }

            group.getStudents().add(student);
            group = studentGroupRepository.save(group);

            student.getGroups().add(group);
            student = studentRepository.save(student);
        }
    }

    private Map<String, List<File>> getAcceptedSnapshotDirs(File[] studentSnapshotDirs, List<String> exerciseList) {
        Map<String, List<File>> snapshotDirs = new TreeMap<String, List<File>>();
        for (File snapshotDir : studentSnapshotDirs) {
            if (!snapshotDir.isDirectory()) {
                continue;
            }

            boolean found = false;
            String exerciseName = getExerciseName(snapshotDir);
            for (String acceptableExercise : exerciseList) {
                if (!exerciseName.contains(acceptableExercise)) {
                    continue;
                }

                found = true;
                break;
            }

            if (!found) {
                continue;
            }

            if (!snapshotDirs.containsKey(exerciseName)) {
                snapshotDirs.put(exerciseName, new ArrayList<File>());
            }

            snapshotDirs.get(exerciseName).add(snapshotDir);
        }

        return snapshotDirs;
    }

    private void readSnapshotsForExercise(Student student, String exerciseName, Map<String, List<File>> snapshotDirs) {
        Exercise exercise = exerciseRepository.findByCourseAndName(course, exerciseName);
        if (exercise == null) {
            exercise = new Exercise();
            exercise.setName(exerciseName);
            exercise = exerciseRepository.save(exercise);
        }

        if (exercise.getCourse() == null || !exercise.getCourse().equals(course)) {
            exercise.setCourse(course);
            exercise = exerciseRepository.save(exercise);
        }

        if (course.getExercises() == null || !course.getExercises().contains(exercise)) {
            course.addExercise(exercise);
            course = courseRepository.save(course);
        }

        for (File snapshotDir : snapshotDirs.get(exerciseName)) {
            readSnapshot(student, exercise, snapshotDir);
        }
    }

    /**
     * Creates dummy test data for snapshot.
     * 50% of snapshots will not have tests, others will have 2 tests.
     * @return
     */
    private List<Testresult> createDummyTests(){
        List<Testresult> tests = new ArrayList<Testresult>();
        int x=(Math.random()<0.5)?1:0;
        if (x == 1) {
            Testresult test = new Testresult();
            test.setName("test1");
            test.setMessage("Test1 output");
            test.setPassed((Math.random()<0.5)?true:false);
            test = testRepository.save(test);
            tests.add(test);
            Testresult test2 = new Testresult();
            test2.setName("test2");
            test2.setMessage("Test2 output");
            test2.setPassed((Math.random()<0.5)?true:false);
            test2 = testRepository.save(test2);
            tests.add(test2);
            test = new Testresult();
            test.setName("test3");
            test.setMessage("Test3 output");
            test.setPassed((Math.random()<0.5)?true:false);
            test = testRepository.save(test);
            tests.add(test);
        }

        return tests;
    }

    private void readSnapshot(Student student, Exercise exercise, File snapshotDir) {
        List<File> javaFiles = listJavaFiles(snapshotDir);
        if (javaFiles.isEmpty()) {
            return;
        }

        String location = snapshotDir.getName();
        location = location.trim();
        Snapshot ss = new Snapshot();

        //If there are more class files than java files, the code compiles
        int amountOfClassFiles = countClassFiles(snapshotDir).length;
        if(javaFiles.size() <= amountOfClassFiles) {
            ss.setCompiles(true);
        }
        else {
            ss.setCompiles(false);
        }

        //Create dummy test data for snapshot
        ss.setTests(createDummyTests());

        try {
            ss.setSnapshotTime(Config.SNAPSHOT_DATE_FORMAT.parse(location));
        } catch (ParseException ex) {
            Logger.getLogger(FileRepositoryInitService.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }

        ExerciseAnswer ea = eaRepository.findByStudentAndExercise(student, exercise);
        if (ea == null) {
            ea = new ExerciseAnswer();
            ea.setExercise(exercise);
            ea.setStudent(student);
            ea = eaRepository.save(ea);
        }

        ss.setName(location);
        ss.setType("EVENT");
        ss.setExerciseAnswer(ea);

        ss = snapshotRepository.save(ss);
        ea.addSnapshot(ss);
        ea = eaRepository.save(ea);

        Collections.sort(javaFiles, new FileComparator());

        for (File file : javaFiles) {
            String path = file.getAbsolutePath();
            SnapshotFile sf = new SnapshotFile();

            String filename = getFilenameWithPackage(snapshotDir.getAbsolutePath(), file.getAbsolutePath());
            Long filesize = file.length();

            sf.setName(filename);
            sf.setFilepath(path);
            sf.setFilesize(filesize);

            sf = snapshotFileRepository.save(sf);

            ss.getFiles().add(sf);
        }

        ss = snapshotRepository.save(ss);
    }

    /**
     *
     * @param dir root directory of a snapshot, contains .class-files
     * @return a File-array of .class-files. The existence of .class files is used
     *         to determine whether or not the code compiles.
     */
    public File[] countClassFiles(File dir){

        return dir.listFiles(new FilenameFilter() {
                 public boolean accept(File dir, String filename)
                      { return filename.endsWith(".class"); }
        } );

    }

    private List<File> listJavaFiles(File fromDir) {
        List<File> files = new ArrayList<File>();
        addFiles(fromDir, files);
        return files;
    }

    private void addFiles(File fromDir, List<File> toList) {
        if (fromDir == null || !fromDir.isDirectory()) {
            return;
        }

        for (File file : fromDir.listFiles()) {
            if (file.isDirectory()) {
                addFiles(file, toList);
                continue;
            }

            if (file.getName().endsWith(".java")) {
                toList.add(file);
                continue;
            }
        }
    }

    private String getExerciseName(File snapshotDir) {
        for (File file : snapshotDir.listFiles()) {
            String filename = file.getName();
            if (filename.toLowerCase().contains("viikko") || filename.toLowerCase().contains("week")) {
                return filename.substring(filename.indexOf("-") + 1);
            }
        }

        return "NA";
    }

    private static String getFilenameWithPackage(String exerciseFolder, String path) {
        try {
            path = path.substring(path.indexOf(exerciseFolder) + exerciseFolder.length());
        } catch (Exception e) {
        }
        try {
            path = path.substring(path.indexOf("/src/") + "/src/".length());
        } catch (Exception e) {
        }

        return path;
    }
}
