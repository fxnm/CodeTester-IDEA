package de.fxnm.web.grabber.access_token;

import com.google.gson.Gson;
import com.intellij.openapi.project.Project;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.HttpURLConnection;

import de.fxnm.exceptions.InternetConnectionException;
import de.fxnm.web.components.token.LoginToken;
import de.fxnm.web.grabber.CommonUrl;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

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

        final int responseCode = response.code();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            response.close();
            throw new InternetConnectionException("Invalid response code:" + response.code());
        }

        final ResponseBody responseBody = response.body();
        if (responseBody == null) {
            response.close();
            throw new InternetConnectionException("null response body");
        }

        final String responseBodyAsString = responseBody.string();
        response.close();

        return new Gson().fromJson(responseBodyAsString, LoginToken.class);
    }
}
