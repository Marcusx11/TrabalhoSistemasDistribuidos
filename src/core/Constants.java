package core;

import java.io.File;

public class Constants {
    public static final String CHANNEL_CLUSTER_NAME = "bankGroupCluster";
    public static final String RMI_NAME = "bank";
    public static final String RMI_URL = "//localhost/bank";

    private static final File file = new File("bank.sqlite");
    public static final String DATABASE_URL = "jdbc:sqlite:" + file.getAbsolutePath();


}
