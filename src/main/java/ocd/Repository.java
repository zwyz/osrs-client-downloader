package ocd;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.Map;

public class Repository {
    private final String repository;
    private final String version;
    private final String catalog;
    private final String alias;

    public Repository(String repository, String version, String catalog, String alias) {
        this.repository = repository;
        this.version = version;
        this.catalog = catalog;
        this.alias = alias;
    }

    public String getName() {
        return repository;
    }

    public VersionData getVersionData() throws IOException {
        return RepositoryDownloader.getConfig(version, VersionData.class);
    }

    public Map<String, String> getAliases() throws IOException {
        return RepositoryDownloader.getConfig(alias, TypeToken.getParameterized(Map.class, String.class, String.class).getType());
    }

    public Catalog getCatalog(String id) throws IOException {
        return RepositoryDownloader.getConfig(catalog + id + "/catalog.json", Catalog.class);
    }
}
