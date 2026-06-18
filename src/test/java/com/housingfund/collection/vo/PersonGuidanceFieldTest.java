package com.housingfund.collection.vo;

import com.housingfund.collection.entity.PersonBasicInfo;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PersonGuidanceFieldTest {

    @Test
    public void personFormsDoNotExposeNonGuideContactProperties() {
        assertNoBeanProperty(PersonOpenForm.class, "phone");
        assertNoBeanProperty(PersonOpenForm.class, "address");
        assertNoBeanProperty(PersonEditForm.class, "phone");
        assertNoBeanProperty(PersonEditForm.class, "address");
    }

    @Test
    public void personEntityUsesGuideSupplementFieldsOnly() {
        assertNoBeanProperty(PersonBasicInfo.class, "phone");
        assertNoBeanProperty(PersonBasicInfo.class, "address");
        assertNoBeanProperty(PersonBasicInfo.class, "updateTime");

        for (String property : new String[]{
                "lastPayDate", "ypayAmt", "ydrawAmt", "yinterestBal", "instCode", "op", "remark"}) {
            assertBeanProperty(PersonBasicInfo.class, property);
        }
    }

    private static void assertBeanProperty(Class<?> type, String property) {
        assertTrue(type.getSimpleName() + " should expose " + property,
                hasAccessor(type, "get" + capitalize(property)) || hasAccessor(type, "is" + capitalize(property)));
        assertTrue(type.getSimpleName() + " should accept " + property,
                Arrays.stream(type.getMethods()).anyMatch(method ->
                        method.getName().equals("set" + capitalize(property)) && method.getParameterCount() == 1));
    }

    private static void assertNoBeanProperty(Class<?> type, String property) {
        assertFalse(type.getSimpleName() + " should not expose " + property,
                hasAccessor(type, "get" + capitalize(property)) || hasAccessor(type, "is" + capitalize(property)));
        assertFalse(type.getSimpleName() + " should not accept " + property,
                Arrays.stream(type.getMethods()).anyMatch(method ->
                        method.getName().equals("set" + capitalize(property)) && method.getParameterCount() == 1));
    }

    private static boolean hasAccessor(Class<?> type, String methodName) {
        return Arrays.stream(type.getMethods()).anyMatch(method ->
                method.getName().equals(methodName) && method.getParameterCount() == 0);
    }

    private static String capitalize(String value) {
        return Character.toUpperCase(value.charAt(0)) + value.substring(1);
    }
}
