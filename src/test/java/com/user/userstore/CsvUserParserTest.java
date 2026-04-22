package com.user.userstore;

import com.user.userstore.model.User;
import com.user.userstore.utility.CsvUserParser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CsvUserParserTest {

    private static CsvUserParser parser;

    @BeforeAll
    static void setup() {
        parser = new CsvUserParser();
    }

    @Test
    void shouldParseValidCsvWithSingleUser() throws Exception {
        String csv = "username,email,street,city,country\n" +
                "aditya,aditya@example.com,Main St,Mumbai,India";

        List<User> users = parser.parse(csv);

        assertEquals(1, users.size());
        assertEquals("aditya", users.get(0).getUsername());
        assertEquals("aditya@example.com", users.get(0).getEmail());
        assertEquals("Mumbai", users.get(0).getAddress().getCity());
        assertEquals("India", users.get(0).getAddress().getCountry());
    }

    @Test
    void shouldThrowExceptionForRowWithMissingFields() {
        String csv = "username,email,street,city,country\n" +
                "aditya,aditya@example.com";
        assertThrows(IllegalArgumentException.class, () -> parser.parse(csv));
    }

}
