package rage.codebrowser.init;

import java.io.File;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RepositoryInit {

    @Autowired
    FileRepositoryInitService service;
    @Autowired
    Boolean useTestRepository;

    @PostConstruct
    @Transactional
    public void init() {
        if (useTestRepository) {
            initTestRepository();
        } else {
            initRepository();
        }
    }

    private void initRepository() {
        for (String dataPath : Config.POSSIBLE_DATA_PATHS) {
            File dataLocation = new File(dataPath);
            if (dataLocation.exists() && dataLocation.isDirectory() && dataLocation.canRead()) {
                Config.DATA_PATH = dataPath;
                break;
            }
        }

//        readInExercises("k2013-ohpe", 100, "/home/group/rage/MOOCDATA/k2013-ohpe/events-decompressed/", "Karkausvuosi" , "Tietokanta", "JoukkueetJaPelaajat", "SilmukatLopetusMuistaminen", "suurempi_luku", "SuurempiLuku", "Lyyrakortti");
//        readInExercises("ohpe", 2, "/home/group/rage/MOOCDATA/s2012-ohpe/events-decompressed/", "Tietokanta", "Lyyrakortti");
//        readInExercises("mooc-ohja", 2, "/home/group/rage/MOOCDATA/k2013-mooc/events-decompressed/", "Matopeli", "Numerotiedustelu", "Sanakirja");

        service.initCourseData("mooc-en", 10, 0, Config.DATA_PATH, "Birdwatcher", "Divider", "EvenNumbers", "LoopsEndingRemembering", "PrintingOutText", "PrintingLikeboss", "HangmanLogic", "PhoneBook", "Tietokanta");
        service.initCourseData("mooc-fi", 2, 0, Config.DATA_PATH, "Birdwatcher", "Divider", "EvenNumbers", "LoopsEndingRemembering", "HangmanLogic", "PhoneBook", "LoopsInReverseOrder", "Tietokanta");
        System.out.println("**************** DONE");
    }

    private void initTestRepository() {
        service.initCourseData("course_1", 10, 0, "./test_data/course_1/", "exercise_");
        service.initCourseData("course_2", 10, 0, "./test_data/course_2/", "exercise_");
    }
}
