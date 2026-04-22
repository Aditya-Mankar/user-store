package com.user.userstore.utility;

import com.user.userstore.model.Address;
import com.user.userstore.model.User;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class CsvUserParser {

    public List<User> parse(String csvContent) throws IOException {
        List<User> users = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new StringReader(csvContent));

        String line;
        boolean firstLine = true;

        while ((line = reader.readLine()) != null) {
            if (firstLine) {
                firstLine = false;  // skip header row
                continue;
            }

            if (!line.trim().isEmpty()) {
                String[] fields = line.split(",");

                if (fields.length < 5) {
                    throw new IllegalArgumentException(
                            "Invalid CSV row: " + line + ". Expected: username,email,street,city,country"
                    );
                }

                User user = new User.Builder()
                        .username(fields[0].trim())
                        .email(fields[1].trim())
                        .build();

                Address address = new Address.Builder()
                        .street(fields[2].trim())
                        .city(fields[3].trim())
                        .country(fields[4].trim())
                        .build();

                user.setAddress(address);
                users.add(user);
            }
        }

        return users;
    }
}
