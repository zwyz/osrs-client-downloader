package ocd;

import java.util.List;

public class Metafile {
    private String id;
    private List<File> files;
    private List<Pad> pad;
    private Pieces pieces;
    private String version;
    private int scanTime;
    private String algorithm;

    public String getId() {
        return id;
    }

    public List<File> getFiles() {
        return files;
    }

    public List<Pad> getPad() {
        return pad;
    }

    public Pieces getPieces() {
        return pieces;
    }

    public String getVersion() {
        return version;
    }

    public int getScanTime() {
        return scanTime;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public static class File {
        private int attr;
        private String name;
        private long size;

        public int getAttr() {
            return attr;
        }

        public String getName() {
            return name;
        }

        public long getSize() {
            return size;
        }
    }

    public static class Pad {
        private long offset;
        private long size;

        public long getOffset() {
            return offset;
        }

        public long getSize() {
            return size;
        }
    }

    public static class Pieces {
        private String algorithm;
        private List<String> digests;
        private boolean hashPadding;

        public String getAlgorithm() {
            return algorithm;
        }

        public List<String> getDigests() {
            return digests;
        }

        public boolean isHashPadding() {
            return hashPadding;
        }
    }
}
