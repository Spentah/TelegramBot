package com.example.demo.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LaunchEnvironment {

    @JsonProperty("name")
    private String name;

    @JsonProperty("variable")
    private Variable variable;

    @JsonProperty("id")
    private int id;

    public String getName() {
        return name;
    }

    public Variable getVariable() {
        return variable;
    }

    public int getId() {
        return id;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Variable {

        @JsonProperty("createdDate")
        private int createdDate;

        @JsonProperty("lastModifiedDate")
        private int lastModifiedDate;

        @JsonProperty("createdBy")
        private String createdBy;

        @JsonProperty("lastModifiedBy")
        private String lastModifiedBy;

        @JsonProperty("name")
        private String name;

        @JsonProperty("id")
        private int id;

        public int getCreatedDate() {
            return createdDate;
        }

        public int getLastModifiedDate() {
            return lastModifiedDate;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public String getLastModifiedBy() {
            return lastModifiedBy;
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }
    }
}