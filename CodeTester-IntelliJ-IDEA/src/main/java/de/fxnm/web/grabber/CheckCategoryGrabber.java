package de.fxnm.web.grabber;

import com.google.gson.Gson;
import com.intellij.openapi.project.Project;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Objects;

import de.fxnm.exceptions.InternetConnectionException;
import de.fxnm.web.components.category.Category;
import de.fxnm.web.components.token.AccessToken;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public final class CheckCategoryGrabber {

    private CheckCategoryGrabber() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public static Category[] getCategories(final Project project, @NotNull final AccessToken accessToken) throws IOException,
            InternetConnectionException {


        final OkHttpClient httpClient = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(CommonUrl.CHECK_CATEGORY.getUrl(project))
                .header("Authorization", accessToken.getToken())
                .get()
                .build();

        final Response response = httpClient.newCall(request).execute();

        if (response.code() != HttpURLConnection.HTTP_OK) {
            throw new InternetConnectionException("Invalid answer form the server, AccessToken was provided");
        }

        return new Gson().fromJson(Objects.requireNonNull(response.body()).string(), Category[].class);
    }
}
