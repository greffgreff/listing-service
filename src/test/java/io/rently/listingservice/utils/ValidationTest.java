package io.rently.listingservice.utils;

import io.rently.listingservice.configs.BugsnagTestConfigs;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.UUID;

@WebMvcTest(Validation.class)
@ContextConfiguration(classes = BugsnagTestConfigs.class)
class ValidationTest {

    @Test
    void canParseToTs_invalidTs_false() {
        String ts = "abc";
        assert !Validation.canParseToTs(ts);
    }

    @Test
    void canParseToTs_validTs_true() {
        String ts = String.valueOf(new Date().getTime());
        assert Validation.canParseToTs(ts);
    }

    @Test
    void tryParseUUID_invalidUUID_false() {
        String uuid = "abc";
        assert Validation.tryParseUUID(uuid) == null;
    }

    @Test
    void tryParseUUID_validUUID_true() {
        String uuid = UUID.randomUUID().toString();
        assert Validation.tryParseUUID(uuid) != null;
    }

    @Test
    void canParseInt_invalidInt_false() {
        String integer = "abc";
        assert !Validation.canParseInt(integer);
    }

    @Test
    void canParseInt_validInt_true() {
        String integer = "123";
        assert Validation.canParseInt(integer);
    }

    @Test
    void canParseFloat_invalidFloat_false() {
        String flt = "abc";
        assert !Validation.canParseFloat(flt);
    }

    @Test
    void canParseFloat_validFloat_true() {
        String flt = "123.123";
        assert Validation.canParseFloat(flt);
    }

    @Test
    void canParseNumeric_invalidNumeric_false() {
        String num = "abc";
        assert !Validation.canParseNumeric(num);
    }

    @Test
    void canParseNumeric_validFloat_true() {
        String num = "123.123";
        assert Validation.canParseNumeric(num);
    }

    @Test
    void canParseNumeric_validInt_true() {
        String num = "123";
        assert Validation.canParseNumeric(num);
    }
}