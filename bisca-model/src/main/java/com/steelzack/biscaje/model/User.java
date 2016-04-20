package com.steelzack.biscaje.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by joaofilipesabinoesperancinha on 16-04-16.
 */

@Getter
@Setter
@Builder
public class User {

    private String name;

    private String passwordString;

    private String lastLoggedIn;
}

