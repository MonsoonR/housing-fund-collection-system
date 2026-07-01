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

    @Test
    public void personOpenJspPagesExist() {
        assertTrue(Files.exists(Path.of("src", "main", "webapp", "WEB-INF", "jsp", "person", "open.jsp")));
        assertTrue(Files.exists(Path.of("src", "main", "webapp", "WEB-INF", "jsp", "person", "receipt.jsp")));
    }

    @Test
    public void personOpenPageContainsExcelBatchImportEntry() throws IOException {
        String content = Files.readString(Path.of("src", "main", "webapp", "WEB-INF", "jsp", "person", "open.jsp"),
                StandardCharsets.UTF_8);

        assertTrue(content.contains("/persons/open/import"));
        assertTrue(content.contains("enctype=\"multipart/form-data\""));
        assertTrue(content.contains("name=\"batchUnitAccNum\""));
        assertTrue(content.contains("序号、个人姓名、证件类型、证件号码、缴存基数"));
        assertTrue(!content.contains("单位账号、姓名、证件类型、证件号码、缴存基数、单位比例、个人比例"));
        assertTrue(content.contains("importResult.failures"));
    }

    @Test
    public void receiptTitlesUseCourseGuideWording() throws IOException {
        assertPageContains("unit", "receipt.jsp", "公积金开户回单");
        assertPageContains("person", "receipt.jsp", "个人住房公积金开户回单");
        assertPageContains("unit", "edit-receipt.jsp", "住房公积金账户资料变更回单");
        assertPageContains("person", "edit-receipt.jsp", "住房公积金个人变更回单");
    }

    @Test
    public void forceChangePagesDoNotPresentEightPrefixAsBusinessRule() throws IOException {
        String conflict = Files.readString(Path.of("src", "main", "webapp", "WEB-INF", "jsp", "person", "edit-conflict.jsp"),
                StandardCharsets.UTF_8);
        String receipt = Files.readString(Path.of("src", "main", "webapp", "WEB-INF", "jsp", "person", "edit-receipt.jsp"),
                StandardCharsets.UTF_8);

        assertTrue(conflict.contains("错误账户证件号码为 9"));
        assertTrue(!conflict.contains("首位释放为 8"));
        assertTrue(!receipt.contains("释放为 8"));
    }

    @Test
    public void queryJspPagesExist() {
        assertTrue(Files.exists(Path.of("src", "main", "webapp", "WEB-INF", "jsp", "unit", "query.jsp")));
        assertTrue(Files.exists(Path.of("src", "main", "webapp", "WEB-INF", "jsp", "person", "query.jsp")));
    }

    @Test
    public void unitEditJspPagesExist() {
        assertTrue(Files.exists(Path.of("src", "main", "webapp", "WEB-INF", "jsp", "unit", "edit.jsp")));
        assertTrue(Files.exists(Path.of("src", "main", "webapp", "WEB-INF", "jsp", "unit", "edit-receipt.jsp")));
    }

    @Test
    public void personEditJspPagesExist() {
        assertTrue(Files.exists(Path.of("src", "main", "webapp", "WEB-INF", "jsp", "person", "edit.jsp")));
        assertTrue(Files.exists(Path.of("src", "main", "webapp", "WEB-INF", "jsp", "person", "edit-conflict.jsp")));
        assertTrue(Files.exists(Path.of("src", "main", "webapp", "WEB-INF", "jsp", "person", "edit-receipt.jsp")));
    }

    @Test
    public void indexPageDoesNotUseInitialScaffoldDescription() throws IOException {
        String content = Files.readString(Path.of("src", "main", "webapp", "WEB-INF", "jsp", "index.jsp"),
                StandardCharsets.UTF_8);

        assertTrue(!content.contains("当前阶段仅提供首页入口和基础配置"));
    }

    @Test
    public void personOpenAndEditPagesDoNotExposeNonGuidanceContactFields() throws IOException {
        List<Path> pages = List.of(
                Path.of("src", "main", "webapp", "WEB-INF", "jsp", "person", "open.jsp"),
                Path.of("src", "main", "webapp", "WEB-INF", "jsp", "person", "edit.jsp"),
                Path.of("src", "main", "webapp", "WEB-INF", "jsp", "person", "edit-conflict.jsp"));

        for (Path page : pages) {
            String content = Files.readString(page, StandardCharsets.UTF_8);
            assertTrue(page + " should not submit phone", !content.contains("name=\"phone\""));
            assertTrue(page + " should not submit address", !content.contains("name=\"address\""));
        }
    }

    @Test
    public void unitQueryPageLetsUnitNameOpenUnitDetail() throws IOException {
        String content = Files.readString(Path.of("src", "main", "webapp", "WEB-INF", "jsp", "unit", "query.jsp"),
                StandardCharsets.UTF_8);

        assertTrue(content.contains("/units/query?unitAccNum="));
        assertTrue(content.contains("点击显示单位详情"));
        assertTrue(content.contains("查看详情"));
    }

    @Test
    public void unitQueryPageUsesDetailCardForExactQueryAndCompactListForNameQuery() throws IOException {
        String content = Files.readString(Path.of("src", "main", "webapp", "WEB-INF", "jsp", "unit", "query.jsp"),
                StandardCharsets.UTF_8);

        assertTrue(content.contains("not empty unitQueryForm.unitAccNum"));
        assertTrue(content.contains("class=\"result-card\""));
        assertTrue(content.contains("class=\"detail-grid\""));
        assertTrue(content.contains("缴存比例（合计）"));
        assertTrue(content.contains("月汇缴金额（合计）"));
        assertTrue(content.contains("empty unitQueryForm.unitAccNum and not empty unitQueryForm.unitName"));
        assertTrue(content.contains("查看详情"));
        assertTrue(!content.contains("<th class=\"num\">公积金余额</th>"));
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

    private static void assertPageContains(String folder, String fileName, String expected) throws IOException {
        String content = Files.readString(Path.of("src", "main", "webapp", "WEB-INF", "jsp", folder, fileName),
                StandardCharsets.UTF_8);
        assertTrue(folder + "/" + fileName + " should contain " + expected, content.contains(expected));
    }
}
