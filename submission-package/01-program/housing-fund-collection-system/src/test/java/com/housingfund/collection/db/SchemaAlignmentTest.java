package com.housingfund.collection.db;

import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SchemaAlignmentTest {

    @Test
    public void schemaUsesCourseGuideTableAndColumnNames() throws IOException {
        String schema = read("db/schema.sql");

        assertTrue(schema.contains("CREATE TABLE TB001"));
        assertTrue(schema.contains("CREATE TABLE TB002"));
        assertTrue(schema.contains("CREATE TABLE TB003"));

        String tb001 = tableBlock(schema, "TB001");
        assertColumn(tb001, "SEQNAME");
        assertColumnType(tb001, "SEQNAME", "CHAR(20)");
        assertColumn(tb001, "SEQ");
        assertColumn(tb001, "MAXSEQ");
        assertColumn(tb001, "DESC");
        assertColumn(tb001, "FREEUSE1");
        assertNoColumn(tb001, "SEQ_DESC");

        String tb002 = tableBlock(schema, "TB002");
        for (String column : new String[]{
                "UNITACCNUM", "UNITACCNAME", "UNITADDR", "ORGCODE", "UNITCHAR", "UNITKIND",
                "SALARYDATE", "UNITPHONE", "UNITLINKMAN", "UNITAGENTPAPNO", "ACCSTATE",
                "BALANCE", "BASENUMBER", "UNITPROP", "PERPROP", "UNITPAYSUM", "PERPAYSUM",
                "PERSNUM", "LASTPAYDATE", "INSTCODE", "OP", "CREATDATE", "REMARK"}) {
            assertColumn(tb002, column);
        }
        for (String oldColumn : new String[]{
                "UNITNAME", "UNITTYPE", "PHONE", "AGENTNAME", "AGENTIDCARD", "UNITRATIO", "PERRATIO"}) {
            assertNoColumn(tb002, oldColumn);
        }
        assertColumnType(tb002, "ORGCODE", "CHAR(20)");
        assertColumnType(tb002, "INSTCODE", "CHAR(8)");
        for (String amountColumn : new String[]{"BALANCE", "BASENUMBER", "UNITPAYSUM", "PERPAYSUM"}) {
            assertColumnType(tb002, amountColumn, "DECIMAL(16,2)");
        }

        String tb003 = tableBlock(schema, "TB003");
        for (String column : new String[]{
                "ACCNUM", "UNITACCNUM", "PERNAME", "IDTYPE", "IDCARD", "OPENDATE",
                "BALANCE", "PERACCSTATE", "BASENUMBER", "UNITPROP", "INDIPROP", "LASTPAYDATE",
                "UNITMONPAYSUM", "PERMONPAYSUM", "YPAYAMT", "YDRAWAMT", "YINTERESTBAL",
                "INSTCODE", "OP", "REMARK"}) {
            assertColumn(tb003, column);
        }
        for (String oldColumn : new String[]{
                "PERACCNUM", "PHONE", "ADDRESS", "BASENUM", "UNITRATIO", "PERRATIO",
                "UNITMONTHPAY", "PERMONTHPAY", "PERBALANCE", "STATUS", "CREATE_TIME", "UPDATE_TIME"}) {
            assertNoColumn(tb003, oldColumn);
        }
        assertColumnType(tb003, "INSTCODE", "CHAR(8)");
        for (String amountColumn : new String[]{
                "BALANCE", "BASENUMBER", "UNITMONPAYSUM", "PERMONPAYSUM",
                "YPAYAMT", "YDRAWAMT", "YINTERESTBAL"}) {
            assertColumnType(tb003, amountColumn, "DECIMAL(16,2)");
        }
    }

    @Test
    public void seedDataUsesCourseGuideSequenceParameters() throws IOException {
        String data = read("db/data.sql");

        assertTrue(data.contains("INSERT INTO TB001"));
        assertTrue(data.contains("`DESC`"));
        assertTrue(data.contains("'UNITACCNUM', 1, 999999999, '公积金单位账号序号'"));
        assertTrue(data.contains("'PERACCNUM', 1, 999999999, '公积金个人账号序号'"));
        assertFalse(data.contains("SEQ_DESC"));
    }

    @Test
    public void mappersUseCourseGuideDatabaseColumns() throws IOException {
        String paramMapper = read("src/main/resources/mapper/ParamMapper.xml");
        assertTrue(paramMapper.contains("`DESC`"));
        assertFalse(paramMapper.contains("SEQ_DESC"));

        String unitMapper = read("src/main/resources/mapper/UnitMapper.xml");
        for (String column : new String[]{
                "`UNITACCNAME`", "`UNITCHAR`", "`UNITKIND`", "`UNITPHONE`", "`UNITLINKMAN`",
                "`UNITAGENTPAPNO`", "`UNITPROP`", "`PERPROP`"}) {
            assertTrue(unitMapper.contains(column));
        }
        for (String oldColumn : new String[]{
                "`UNITNAME`", "`UNITTYPE`", "`PHONE`", "`AGENTNAME`", "`AGENTIDCARD`",
                "`UNITRATIO`", "`PERRATIO`"}) {
            assertFalse(unitMapper.contains(oldColumn));
        }

        String personMapper = read("src/main/resources/mapper/PersonMapper.xml");
        for (String column : new String[]{
                "`ACCNUM`", "`OPENDATE`", "`BALANCE`", "`PERACCSTATE`", "`BASENUMBER`",
                "`UNITPROP`", "`INDIPROP`", "`LASTPAYDATE`", "`UNITMONPAYSUM`",
                "`PERMONPAYSUM`", "`YPAYAMT`", "`YDRAWAMT`", "`YINTERESTBAL`",
                "`INSTCODE`", "`OP`", "`REMARK`"}) {
            assertTrue(personMapper.contains(column));
        }
        for (String oldColumn : new String[]{
                "`PERACCNUM`", "`PHONE`", "`ADDRESS`", "`BASENUM`", "`UNITRATIO`",
                "`PERRATIO`", "`UNITMONTHPAY`", "`PERMONTHPAY`", "`PERBALANCE`",
                "`STATUS`", "`CREATE_TIME`", "`UPDATE_TIME`"}) {
            assertFalse(personMapper.contains(oldColumn));
        }
    }

    private static String read(String path) throws IOException {
        return Files.readString(Path.of(path), StandardCharsets.UTF_8);
    }

    private static String tableBlock(String schema, String tableName) {
        Pattern pattern = Pattern.compile("CREATE TABLE\\s+" + tableName + "\\s*\\((.*?)\\)\\s*ENGINE",
                Pattern.DOTALL);
        Matcher matcher = pattern.matcher(schema);
        assertTrue("Missing table block for " + tableName, matcher.find());
        return matcher.group(1);
    }

    private static void assertColumn(String tableBlock, String column) {
        assertTrue("Missing column " + column, columnPattern(column).matcher(tableBlock).find());
    }

    private static void assertNoColumn(String tableBlock, String column) {
        assertFalse("Unexpected column " + column, columnPattern(column).matcher(tableBlock).find());
    }

    private static void assertColumnType(String tableBlock, String column, String expectedType) {
        Pattern pattern = Pattern.compile("(?m)^\\s*`?" + Pattern.quote(column)
                + "`?\\s+" + Pattern.quote(expectedType) + "(\\s|$)", Pattern.CASE_INSENSITIVE);
        assertTrue("Column " + column + " should use " + expectedType, pattern.matcher(tableBlock).find());
    }

    private static Pattern columnPattern(String column) {
        return Pattern.compile("(?m)^\\s*`?" + Pattern.quote(column) + "`?\\s+");
    }
}
