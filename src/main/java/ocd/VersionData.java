package ocd;

import java.util.Map;

public class VersionData {
    private final Map<String, Environment> environments;

    public VersionData(Map<String, Environment> environments) {
        this.environments = environments;
    }

    public Map<String, Environment> getEnvironments() {
        return environments;
    }
}
