package de.fxnm.util;

import com.intellij.AbstractBundle;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.PropertyKey;

public final class CodeTesterBundle extends AbstractBundle {

    @NonNls
    private static final String BUNDLE = "de.fxnm.CodeTesterBundle";
    private static final CodeTesterBundle INSTANCE = new CodeTesterBundle();

    private CodeTesterBundle() {
        super(BUNDLE);
    }

    public static String message(@PropertyKey(resourceBundle = BUNDLE) final String key,
                                 final Object... params) {
        return INSTANCE.getMessage(key, params);
    }

}
