package org.groupscope.util;

public class FunctionInfo {

    public static String getCurrentFunctionName() {
        return new Throwable()
                .getStackTrace()[1]
                .getMethodName();
    }
}
