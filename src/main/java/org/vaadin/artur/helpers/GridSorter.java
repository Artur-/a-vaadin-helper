package org.vaadin.artur.helpers;

import java.util.Optional;

// From GridSorter in vaadin-grid
public class GridSorter {
    public Optional<String> direction;
    public String path;

    public Optional<String> getDirection() {
        return direction;
    }

    public void setDirection(Optional<String> direction) {
        this.direction = direction;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
