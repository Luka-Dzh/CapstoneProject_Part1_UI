package org.epam.dto;

public class Track {
    private String uri;
    private int position;
    private String snapshot_id;

    public String getSnapshot_id() {
        return snapshot_id;
    }

    public void setSnapshot_id(String snapshot_id) {
        this.snapshot_id = snapshot_id;
    }

    public String getUris() {
        return uri;
    }

    public void setUris(String uri) {
        this.uri = uri;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
