package de.fxnm.runnable.scanner.service;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileVisitor;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.fxnm.exceptions.InternetConnectionException;
import de.fxnm.exceptions.PasswordSafeException;
import de.fxnm.runnable.BaseRunnable;
import de.fxnm.util.CodeTesterBundle;
import de.fxnm.web.components.submission.SubmissionResult;
import de.fxnm.web.grabber.SubmitionGrabber;
import de.fxnm.web.grabber.access_token.AccessTokenGrabber;

public class CheckFilesRunnable extends BaseRunnable {

    final int checkID;

    public CheckFilesRunnable(final Project project,
                              final int checkID) {

        super(project, CheckFilesRunnable.class);
        this.checkID = checkID;
    }

    @Override
    public void run() {
        try {
            this.startRunnable(CodeTesterBundle.message("plugin.runnable.checkFiles.start.loggerMessage"),
                    CodeTesterBundle.message("plugin.runnable.checkFiles.start.toolWindowMessage"),
                    CodeTesterBundle.message("plugin.runnable.checkFiles.start.backgroundProcessName"));

            final List<PsiFile> files = this.findAllPsiFilesFor(this.getChildrenFiles(this.getProjectRootFiles(this.project())));

            final SubmissionResult scanResult = this.processFilesForModuleInfoAndScan(files, this.checkID);

            this.finishedRunnable(CodeTesterBundle.message("plugin.runnable.checkFiles.finished.loggerMessage"),
                    CodeTesterBundle.message("plugin.runnable.checkFiles.finished.toolWindowMessage"),
                    scanResult);
        } catch (final Throwable e) {
            this.failedRunnable(CodeTesterBundle.message("plugin.runnable.checkFiles.failed.loggerMessage"),
                    CodeTesterBundle.message("plugin.runnable.checkFiles.failed.toolWindowMessage"),
                    e);
        }
    }


    private @NotNull VirtualFile[] getProjectRootFiles(final Project project) {
        return ProjectRootManager.getInstance(project).getContentRoots();

    }

    private List<VirtualFile> getChildrenFiles(final VirtualFile[] files) {
        final List<VirtualFile> flattened = new ArrayList<>();
        if (files != null) {
            for (final VirtualFile file : files) {
                flattened.add(file);
                VfsUtilCore.visitChildrenRecursively(file, new VirtualFileVisitor<Object>() {
                    @Override
                    @NotNull
                    public Result visitFileEx(@NotNull final VirtualFile file) {
                        flattened.add(file);
                        return CONTINUE;
                    }
                });
            }
        }
        return flattened;
    }

    private List<PsiFile> findAllPsiFilesFor(@NotNull final List<VirtualFile> virtualFiles) {
        final List<PsiFile> childFiles = new ArrayList<>();
        final PsiManager psiManager = PsiManager.getInstance(this.project());
        for (final VirtualFile virtualFile : virtualFiles) {
            childFiles.addAll(this.buildFilesList(psiManager, virtualFile));
        }
        return childFiles;
    }

    private List<PsiFile> buildFilesList(final PsiManager psiManager, final VirtualFile virtualFile) {
        final List<PsiFile> allChildFiles = new ArrayList<>();
        ApplicationManager.getApplication().runReadAction(() -> {
            final FindChildFiles visitor = new FindChildFiles(virtualFile, psiManager);
            VfsUtilCore.visitChildrenRecursively(virtualFile, visitor);
            allChildFiles.addAll(visitor.locatedFiles);
        });
        return allChildFiles;
    }

    private SubmissionResult processFilesForModuleInfoAndScan(final List<PsiFile> files, final int checkID)
            throws PasswordSafeException, IOException, InternetConnectionException {

        return SubmitionGrabber.submit(this.project(), AccessTokenGrabber.getToken(this.project()), checkID,
                files.stream()
                        .filter(s -> s.getFileType().getName().equals("JAVA"))
                        .collect(Collectors.toList()));
    }

    private static class FindChildFiles extends VirtualFileVisitor<PsiFile> {

        private final List<PsiFile> locatedFiles = new ArrayList<>();
        private final PsiManager psiManager;
        private final VirtualFile virtualFile;

        FindChildFiles(final VirtualFile virtualFile, final PsiManager psiManager) {
            this.virtualFile = virtualFile;
            this.psiManager = psiManager;
        }

        @Override
        public boolean visitFile(@NotNull final VirtualFile file) {
            if (!file.isDirectory()) {
                final PsiFile psiFile = this.psiManager.findFile(this.virtualFile);
                if (psiFile != null) {
                    this.locatedFiles.add(psiFile);
                }
            }
            return true;
        }
    }
}
