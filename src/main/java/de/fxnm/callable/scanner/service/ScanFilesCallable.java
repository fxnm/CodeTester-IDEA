package de.fxnm.callable.scanner.service;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
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

import de.fxnm.callable.BaseCallable;
import de.fxnm.exceptions.InternetConnectionException;
import de.fxnm.exceptions.PasswordSafeException;
import de.fxnm.web.components.submission.SubmissionResult;
import de.fxnm.web.grabber.SubmitionGrabber;
import de.fxnm.web.grabber.access_token.AccessTokenGrabber;

public class ScanFilesCallable extends BaseCallable<SubmissionResult> {

    final int checkID;
    private final List<PsiFile> files;

    public ScanFilesCallable(final Project project,
                             final List<VirtualFile> virtualFileList,
                             final int checkID) {
        super(project);

        this.files = this.findAllPsiFilesFor(virtualFileList);
        this.checkID = checkID;
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

    @Override
    public SubmissionResult call() {
        try {
            this.startCallable("Scanning", "Started scanning files");
            final SubmissionResult scanResult = this.processFilesForModuleInfoAndScan(this.files, this.checkID);
            this.finishedCallable(scanResult);
            return scanResult;
        } catch (final Throwable e) {
            this.failedCallable("Failed to call Scan Files");
            return null;
        }
    }

    private SubmissionResult processFilesForModuleInfoAndScan(final List<PsiFile> files, final int checkID)
            throws PasswordSafeException, IOException, InternetConnectionException {

        return SubmitionGrabber.submit(this.project(), AccessTokenGrabber.getToken(this.project()), checkID,
                files.stream()
                        .filter(s -> s.getFileType().getName().equals("JAVA"))
                        .collect(Collectors.toList()));
    }

    private static class FindChildFiles extends VirtualFileVisitor<PsiFile> {

        private final VirtualFile virtualFile;
        private final PsiManager psiManager;
        private final List<PsiFile> locatedFiles = new ArrayList<>();

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
