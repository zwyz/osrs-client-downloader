package ocd;

import java.io.IOException;

public class Catalog {
    private CatalogConfig config;
    private String id;
    private String metafile;

    public CatalogConfig getConfig() {
        return config;
    }

    public String getId() {
        return id;
    }

    public Metafile getMetafile() throws IOException {
        return RepositoryDownloader.getConfig(metafile, Metafile.class);
    }
}
