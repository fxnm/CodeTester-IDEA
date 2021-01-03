package de.fxnm.web.grabber.access_token;

import com.google.gson.Gson;
import com.intellij.credentialStore.Credentials;
import com.intellij.openapi.project.Project;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

import de.fxnm.config.settings.password_safe.PasswordManager;
import de.fxnm.exceptions.InternetConnectionException;
import de.fxnm.exceptions.PasswordSafeException;
import de.fxnm.web.components.token.AccessToken;
import de.fxnm.web.components.token.LoginToken;
import de.fxnm.web.grabber.CommonUrl;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static de.fxnm.config.settings.password_safe.PasswordManager.LOGIN_KEY;

public final class AccessTokenGrabber {

    private AccessTokenGrabber() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public static AccessToken getToken(final Project project) throws IOException, PasswordSafeException,
            InternetConnectionException {
        final Credentials credentials = PasswordManager.retrieve(LOGIN_KEY);

        return getAccessToken(project,
                LoginTokenGrabber.getToken(
                        project,
                        Objects.requireNonNull(credentials.getUserName()),
                        Objects.requireNonNull(credentials.getPasswordAsString())));
    }

    private static AccessToken getAccessToken(final Project project,
                                              @NotNull final LoginToken token) throws IOException {

        final OkHttpClient httpClient = new OkHttpClient();
        final RequestBody formBody = new FormBody.Builder()
                .add("refreshToken", token.getToken())
                .build();

        final Request request = new Request.Builder()
                .url(CommonUrl.ACCESS_TOKEN.getUrl(project))
                .post(formBody)
                .build();

        final Response response = httpClient.newCall(request).execute();
        return new Gson().fromJson(Objects.requireNonNull(response.body()).string(), AccessToken.class);
    }
}
