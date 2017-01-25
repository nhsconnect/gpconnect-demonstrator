package uk.gov.hscic.common.util;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

public final class NhsCodeValidatorTest {

    @Test
    public void nhsCodeValidatorTest() {
        assertTrue(NhsCodeValidator.nhsNumberValid("0123456789"));
    }
}
