package drivers;

import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
class LocalDriverTest {
    @TempDir Path directory;
    private HttpServer server;
    private String url;
    private final AtomicInteger requests = new AtomicInteger();
    private byte[] body;
    private int status = 200;

    @BeforeEach
    void startServer() throws Exception {
        try (var bytes = new ByteArrayOutputStream(); var zip = new ZipOutputStream(bytes)) {
            zip.putNextEntry(new ZipEntry("AndroidManifest.xml"));
            zip.write("test manifest".getBytes());
            zip.closeEntry();
            zip.finish();
            body = bytes.toByteArray();
        }
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/app.apk", exchange -> {
            requests.incrementAndGet();
            exchange.sendResponseHeaders(status, body.length);
            try (var output = exchange.getResponseBody()) { output.write(body); }
            exchange.close();
        });
        server.start();
        url = "http://127.0.0.1:" + server.getAddress().getPort() + "/app.apk";
    }

    @AfterEach
    void stopServer() { server.stop(0); }

    @Test
    void downloadsMissingApkAndReusesItWithoutNetwork() throws Exception {
        Path apk = directory.resolve("apps/wikipedia.apk");
        assertEquals(apk, LocalDriver.getAppPath(apk.toString(), url));
        assertArrayEquals(body, Files.readAllBytes(apk));
        assertEquals(apk, LocalDriver.getAppPath(apk.toString(), url));
        assertEquals(1, requests.get());
    }

    @Test
    void replacesEmptyFile() throws Exception {
        Path apk = Files.createFile(directory.resolve("empty.apk"));
        LocalDriver.getAppPath(apk.toString(), url);
        assertArrayEquals(body, Files.readAllBytes(apk));
    }

    @Test
    void failedDownloadLeavesNoCachedOrPartialFileAndCanBeRetried() throws Exception {
        status = 404;
        Path apk = directory.resolve("apps/wikipedia.apk");
        assertThrows(IllegalStateException.class, () -> LocalDriver.getAppPath(apk.toString(), url));
        try (var files = Files.list(apk.getParent())) { assertEquals(0, files.count()); }
        status = 200;
        LocalDriver.getAppPath(apk.toString(), url);
        assertArrayEquals(body, Files.readAllBytes(apk));
    }

    @Test
    void rejectsHtmlInsteadOfApk() throws Exception {
        body = "<html>Download error</html>".getBytes();
        Path apk = directory.resolve("apps/wikipedia.apk");
        assertThrows(IllegalStateException.class, () -> LocalDriver.getAppPath(apk.toString(), url));
        try (var files = Files.list(apk.getParent())) { assertEquals(0, files.count()); }
    }
}
