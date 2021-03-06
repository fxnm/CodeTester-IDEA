package de.fxnm.util;

import com.intellij.openapi.diagnostic.Logger;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public final class CopyUtil {

    private static final Logger LOG = Logger.getInstance(CopyUtil.class);

    private CopyUtil() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public static void copyToClipBoard(final String string) {
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Clipboard clipboard = toolkit.getSystemClipboard();
        final StringSelection strSel = new StringSelection(string);
        clipboard.setContents(strSel, null);
        LOG.info(String.format(CodeTesterBundle.message("plugin.util.copyUtil.copyToClipBoard.success"), string));
    }
}
