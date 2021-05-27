package de.fxnm.web.grabber;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.psi.PsiFile;

import org.jetbrains.annotations.NotNull;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import de.fxnm.web.components.submission.SubmissionResult;
import de.fxnm.web.components.submission.error.Diagnostics;
import de.fxnm.web.components.submission.success.Check;
import de.fxnm.web.components.submission.success.Successful;
import de.fxnm.web.components.token.AccessToken;

public final class SubmitionGrabber {

    private SubmitionGrabber() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public static SubmissionResult submit(final Project project,
                                          @NotNull final AccessToken accessToken,
                                          final int checkID,
                                          @NotNull final List<PsiFile> files) throws IOException {

        if (checkID <= 0) {
            throw new IOException("Invalid check id:" + checkID);
        }


        final OkHttpClient httpClient = new OkHttpClient.Builder()
                .readTimeout(1, TimeUnit.MINUTES)
                .build();

        final MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        for (final PsiFile file : files) {
            builder.addFormDataPart(file.getName(),
                    file.getName(),
                    RequestBody.create(VfsUtil.loadText(file.getVirtualFile()), MediaType.parse("application/octet-stream")));
        }


        final Request request = new Request.Builder()
                .url(CommonUrl.RUN_CHECK.getUrl(project) + checkID)
                .header("Authorization", accessToken.getToken())
                .post(builder.build())
                .build();

        final Response response = httpClient.newCall(request).execute();

        final String responseString = Objects.requireNonNull(response.body()).string();

        JsonObject result = new Gson().fromJson(responseString, JsonObject.class);

        final Collection<String> keys = result.keySet();

        if (!keys.contains("fileResults")) {
            return new Diagnostics(result.toString());
        }

        final Set<String> packageName = result.get("fileResults").getAsJsonObject().keySet();
        result = result.get("fileResults").getAsJsonObject();


        final String className = packageName.stream().findFirst().orElseThrow(IOException::new);

        return new Successful(className, new Gson().fromJson(result.get(className), Check[].class));
    }
}
