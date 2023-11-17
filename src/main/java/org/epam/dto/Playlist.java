package org.epam.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Playlist {
    private String name;
    private String description;
    @JsonProperty("public")
    private boolean isPublic;
}
