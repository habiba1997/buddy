package com.studdybuddy.core.controllers;


import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;

@DgsComponent
public class HelloDataFetcher {

    @DgsQuery
    public String hello() {
        return "Hello from DGS + Codegen Core!";
    }

    @DgsQuery
    public String greet(String name) {
        return "Hello, " + name + " From Core!";
    }
}
