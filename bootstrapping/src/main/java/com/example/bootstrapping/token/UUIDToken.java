package com.example.bootstrapping.token;

import java.util.UUID;

public class UUIDToken implements Token{
    @Override
    public String generateNewToken() {
        return UUID.randomUUID().toString();
    }
}
