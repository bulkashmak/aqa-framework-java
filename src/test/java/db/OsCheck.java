package db;

import java.util.Locale;

public final class OsCheck {
    public enum OSType {
        Windows, Other
    };

    protected static OSType detectedOS;

    public static OSType getOperatingSystemType() {
        if (detectedOS == null) {
            String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
            if (OS.indexOf("win") >= 0) {
                detectedOS = OSType.Windows;
            } else {
                detectedOS = OSType.Other;
            }
        }
        return detectedOS;
    }
}