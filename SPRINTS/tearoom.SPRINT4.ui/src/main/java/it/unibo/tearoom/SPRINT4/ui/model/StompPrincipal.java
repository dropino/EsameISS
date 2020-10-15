package it.unibo.tearoom.SPRINT4.ui.model;

import java.security.Principal;

public class StompPrincipal implements Principal {

    private String name;

    public StompPrincipal(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}