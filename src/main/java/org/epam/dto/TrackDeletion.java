package org.epam.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

import java.util.ArrayList;
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@Builder
@Getter
@Setter
public class TrackDeletion {
        private ArrayList<String> uris;
        private String snapshot_id;
}
