package ocd;

import com.google.common.io.BaseEncoding;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class Main {
    private static final Path OUTPUT_PATH = Path.of("result");

    private static final List<Repository> REPOSITORIES = List.of(
            buildRepositoryInfo("win"),
            buildRepositoryInfo("mac")
    );

    public static void main(String[] args) throws IOException {
        for (var repository : REPOSITORIES) {
            var repositoryPath = OUTPUT_PATH.resolve(repository.getName());

            System.out.println("Downloading version data for " + repository.getName() + "...");
            var versionData = repository.getVersionData();
            var ids = new LinkedHashSet<String>();

            for (var entry : versionData.getEnvironments().entrySet()) {
                var name = entry.getKey();
                ids.add(entry.getValue().getId());
                System.out.println("  " + name + " -> " + entry.getValue().getId());
            }

            for (var id : ids) {
                System.out.println("Downloading " + id + "...");
                var catalogPath = repositoryPath.resolve(id);

                var catalog = repository.getCatalog(id);
                var remote = catalog.getConfig().getRemote();
                var baseUrl = remote.getBaseUrl();

                if (!remote.getPieceFormat().equals("pieces/{SubString:0,2,{TargetDigest}}/{TargetDigest}.solidpiece")) {
                    throw new IllegalStateException("piece format has changed, format is currently hardcoded in this program: " + remote.getPieceFormat());
                }

                var metafile = catalog.getMetafile();
                var totalSize = 0;

                for (var file : metafile.getFiles()) {
                    totalSize += file.getSize();
                }

                var buffer = ByteBuffer.allocate(totalSize);
                var digests = metafile.getPieces().getDigests();

                for (var i = 0; i < digests.size(); i++) {
                    var digest = digests.get(i);
                    System.out.println("  Downloading piece " + i + "/" + digests.size() + "...");
                    var hexDigest = BaseEncoding.base16().encode(BaseEncoding.base64().decode(digest)).toLowerCase();
                    var url = baseUrl + "pieces/" + hexDigest.substring(0, 2) + "/" + hexDigest + ".solidpiece";
                    var data = RepositoryDownloader.getData(url);

                    var unknownData = Arrays.copyOfRange(data, 0, 6); // todo: what's this?
                    var gzipData = Arrays.copyOfRange(data, 6, data.length);
                    var decompressedData = new GZIPInputStream(new ByteArrayInputStream(gzipData)).readAllBytes();
                    buffer.put(decompressedData);
                }

                buffer.flip();

                for (var file : metafile.getFiles()) {
                    System.out.println("  Saving file " + file.getName() + "...");
                    var filePath = catalogPath.resolve(file.getName());
                    var data = new byte[(int) file.getSize()];
                    buffer.get(data);
                    Files.createDirectories(filePath.getParent());
                    Files.write(filePath, data);
                }
            }
        }
    }

    private static Repository buildRepositoryInfo(String systemShortName) {
        return new Repository(
                "osrs-" + systemShortName,
                "https://jagex.akamaized.net/direct6/osrs-" + systemShortName + "/osrs-" + systemShortName + ".json",
                "https://jagex.akamaized.net/direct6/osrs-" + systemShortName + "/catalog/",
                "https://jagex.akamaized.net/direct6/osrs-" + systemShortName + "/alias.json"
        );
    }
}
