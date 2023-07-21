package ocd;

import com.auth0.jwt.JWT;
import com.google.common.io.BaseEncoding;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RepositoryDownloader {
    private static final HttpClient HTTP = HttpClient.newBuilder().build();
    private static final Gson GSON = new Gson();

    public static <T> T getConfig(String url, Type type) throws IOException {
        try {
            var response = HTTP.send(HttpRequest.newBuilder(URI.create(url)).build(), HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new IOException("HTTP request resulted in an error: " + response.statusCode());
            }

            var decoded = JWT.decode(response.body()); // actually JWS, not JWT, but the library supports decoding any JWS data, not just tokens
            return GSON.fromJson(new String(BaseEncoding.base64().decode(decoded.getPayload())), type);
        } catch (InterruptedException e) {
            throw new IOException(e);
        }
    }

    public static byte[] getData(String url) throws IOException {
        try {
            var response = HTTP.send(HttpRequest.newBuilder(URI.create(url)).build(), HttpResponse.BodyHandlers.ofByteArray());

            if (response.statusCode() != 200) {
                throw new IOException("HTTP request resulted in an error: " + response.statusCode());
            }

            return response.body();
        } catch (InterruptedException e) {
            throw new IOException(e);
        }
    }
}
