package org.acme;

import lombok.Data;

@Data
public class Fold {
    private String direction;
    private int position;
    public Fold(final String direction, final String position) {
        this.direction = direction;
        this.position = Integer.parseInt(position);
    }
}
