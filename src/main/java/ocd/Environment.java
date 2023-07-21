package ocd;

public class Environment {
    private String id;
    private long promoteTime;
    private long scanTime;
    private String version;

    public String getId() {
        return id;
    }

    public long getPromoteTime() {
        return promoteTime;
    }

    public long getScanTime() {
        return scanTime;
    }

    public String getVersion() {
        return version;
    }
}
