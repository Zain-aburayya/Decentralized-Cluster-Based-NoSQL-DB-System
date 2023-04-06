package com.example.bootstrapping.affinity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class AuthenticationModel {
    private String username;
    private String token;
    private String worker;
}
