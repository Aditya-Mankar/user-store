package com.user.userstore.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.gemfire.mapping.annotation.Region;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Region("Users")
@JacksonXmlRootElement(localName = "user")
public class User implements Serializable {

    @Id
    @NotBlank(message = "Username cannot be blank")
    private String username;
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be blank")
    private String email;
    private Address address;

    private User(Builder builder) {
        this.username = builder.username;
        this.email = builder.email;
        this.address = builder.address;
    }

    public static class Builder {
        private String username;
        private String email;
        private Address address;

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder address(Address address) {
            this.address = address;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }

}