package rage.codebrowser.init;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Config {
    // sshfs <username>@<ipaddress>:/remotepath ~/remoteserv

    public static final String[] POSSIBLE_DATA_PATHS = {"/home/avihavai/data/", "/home/avihavai/remotefs/", "/cs/group/home/rage/MOOCDATA/k2013-ohpe/events-decompressed/", "/home/group/rage/MOOCDATA/k2013-ohpe/events-decompressed/", "/home/group/rage/MOOCDATA/k2013-ohpe-pre-summer2013/events-decompressed/", "/cs/group/home/rage/MOOCDATA/k2013-ohpe-pre-summer2013/events-decompressed/", "/cs/group/home/rage/MOOCDATA/s2012-ohja/events-decompressed/", "/cs/group/home/rage/MOOCDATA/k2013-mooc/events-decompressed/"};
    public static String DATA_PATH = null;
    public static final DateFormat SNAPSHOT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_SSSSSS");
}
