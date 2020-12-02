package de.fxnm.web.grabber.access_token;

import com.google.gson.Gson;
import com.intellij.openapi.project.Project;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Objects;

import de.fxnm.exceptions.InternetConnectionException;
import de.fxnm.web.components.token.LoginToken;
import de.fxnm.web.grabber.CommonUrl;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public final class LoginTokenGrabber {

    private LoginTokenGrabber() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public static LoginToken getToken(final Project project,
                                      @NotNull final String username, @NotNull final String password) throws IOException,
            InternetConnectionException {
        final OkHttpClient httpClient = new OkHttpClient();

        final RequestBody formBody = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();

        final Request request = new Request.Builder()
                .url(CommonUrl.LOGIN.getUrl(project))
                .post(formBody)
                .build();

        final Response response = httpClient.newCall(request).execute();

        if (response.code() != HttpURLConnection.HTTP_OK) {
            throw new InternetConnectionException("Invalid response code:" + response.code());
        }

        return new Gson().fromJson(Objects.requireNonNull(response.body()).string(), LoginToken.class);
    }
}
