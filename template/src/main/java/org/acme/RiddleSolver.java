package org.acme;

import java.util.List;

public class RiddleSolver {

    private static boolean logEnabled = true;

    private static void log(final String s) {
        if(isLogEnabled()){
            System.out.println(s);
        }
    }

    public static boolean isLogEnabled() {
        return logEnabled;
    }

    public int solve(List<String> strings){
        log(strings.toString());
        return 42;
    }
}
