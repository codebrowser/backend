package rage.codebrowser.init;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Config {
    // sshfs <username>@<ipaddress>:/remotepath ~/remoteserv

    public static final String[] POSSIBLE_DATA_PATHS = {"/home/group/codebro/data/mooc-en/events-decompressed/", "/home/avihavai/data/", "/home/avihavai/remotefs/"};
    public static String DATA_PATH = null;
    public static final DateFormat SNAPSHOT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_SSSSSS");
}
