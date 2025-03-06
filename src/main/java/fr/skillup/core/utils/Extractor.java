package fr.skillup.core.utils;

import java.io.*;
import java.util.logging.Logger;

public class Extractor {
    public static String extractLibrary(String folder, String fileName) {
        try {
            String tempDir = System.getProperty("java.io.tmpdir");
            File tempFile = new File(tempDir, fileName);

            if (!tempFile.exists()) {
                try (InputStream in = Extractor.class.getResourceAsStream("/fr/skillup/" + folder + fileName);
                     OutputStream out = new FileOutputStream(tempFile)) {
                    if (in == null) {
                        throw new FileNotFoundException("Le fichier " + fileName + " n'a pas été trouvé dans le JAR.");
                    }
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0) {
                        out.write(buffer, 0, length);
                    }
                }
            }

            System.load(tempFile.getAbsolutePath());
            return tempFile.getAbsolutePath();
        } catch (Exception e) {
            Logger.getLogger(Extractor.class.getName()).severe(e.getMessage());
        }
        return null;
    }
}
