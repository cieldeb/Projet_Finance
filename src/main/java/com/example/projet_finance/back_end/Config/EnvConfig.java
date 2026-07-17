package com.example.projet_finance.back_end.Config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Minimal .env file loader.
 *
 * Reads KEY=VALUE pairs from a ".env" file located in the project's working
 * directory (the project root, when the app is launched via `mvnw javafx:run`
 * or from an IDE run configuration with the working directory set to the
 * project root). Falls back to real environment variables if a key isn't
 * found in the .env file, so this also works with variables exported in CI
 * or the shell.
 *
 * No third-party dependency required, and no key is ever hardcoded in code.
 */
public class EnvConfig {

    private static final Map<String, String> values = new HashMap<>();
    private static boolean loaded = false;

    private EnvConfig() {
    }

    private static synchronized void load() {
        if (loaded) {
            return;
        }
        loaded = true;

        try (BufferedReader reader = new BufferedReader(new FileReader(".env"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                int separatorIndex = line.indexOf('=');
                if (separatorIndex == -1) {
                    continue;
                }

                String key = line.substring(0, separatorIndex).trim();
                String value = line.substring(separatorIndex + 1).trim();

                // Strip surrounding quotes, if present (e.g. KEY="value")
                if (value.length() >= 2) {
                    boolean doubleQuoted = value.startsWith("\"") && value.endsWith("\"");
                    boolean singleQuoted = value.startsWith("'") && value.endsWith("'");
                    if (doubleQuoted || singleQuoted) {
                        value = value.substring(1, value.length() - 1);
                    }
                }

                values.put(key, value);
            }
        } catch (IOException e) {
            System.err.println("[EnvConfig] No .env file found in the working directory. " +
                    "Copy .env.example to .env and fill in your API keys. " +
                    "Falling back to system environment variables in the meantime.");
        }
    }

    /**
     * Returns the value for the given key, checked first in the .env file
     * then in the system environment variables.
     *
     * @throws IllegalStateException if the key is missing from both sources.
     */
    public static String get(String key) {
        load();

        String value = values.get(key);
        if (value == null || value.isEmpty()) {
            value = System.getenv(key);
        }
        if (value == null || value.isEmpty()) {
            throw new IllegalStateException(
                    "Missing required configuration value: " + key +
                            ". Add it to your .env file (see .env.example) or export it as an environment variable."
            );
        }
        return value;
    }
}
