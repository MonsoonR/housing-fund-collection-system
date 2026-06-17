package com.housingfund.collection.web;

import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.Assert.assertTrue;

public class JspSyntaxTest {

    private static final Pattern CUSTOM_TAG_IN_HTML_ATTRIBUTE =
            Pattern.compile("\\w+\\s*=\\s*\"[^\"]*<c:out\\b");

    @Test
    public void jspPagesDoNotPlaceCustomTagsInsideHtmlAttributes() throws IOException {
        Path jspRoot = Path.of("src", "main", "webapp", "WEB-INF", "jsp");
        List<String> invalidUsages = new ArrayList<>();

        try (var paths = Files.walk(jspRoot)) {
            paths.filter(path -> path.toString().endsWith(".jsp"))
                    .forEach(path -> collectInvalidUsages(path, invalidUsages));
        }

        assertTrue("JSP custom tags cannot be nested inside HTML attributes: " + invalidUsages,
                invalidUsages.isEmpty());
    }

    private static void collectInvalidUsages(Path path, List<String> invalidUsages) {
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            for (int i = 0; i < lines.size(); i++) {
                if (CUSTOM_TAG_IN_HTML_ATTRIBUTE.matcher(lines.get(i)).find()) {
                    invalidUsages.add(path + ":" + (i + 1));
                }
            }
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to read JSP file: " + path, ex);
        }
    }
}
