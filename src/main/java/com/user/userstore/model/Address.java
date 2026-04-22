package com.user.userstore.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "address")
public class Address implements Serializable {

    private String street;
    private String city;
    private String country;

    private Address(Builder builder) {
        this.street = builder.street;
        this.city = builder.city;
        this.country = builder.country;
    }

    public static class Builder {
        private String street;
        private String city;
        private String country;

        public Builder street(String street) {
            this.street = street;
            return this;
        }

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder country(String country) {
            this.country = country;
            return this;
        }

        public Address build() {
            return new Address(this);
        }
    }

}

