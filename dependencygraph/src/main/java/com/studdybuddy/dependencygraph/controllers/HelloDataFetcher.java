package com.studdybuddy.dependencygraph.controllers;


import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;

@DgsComponent
public class HelloDataFetcher {

    @DgsQuery
    public String hello() {
        return "Hello from DGS + Codegen Dependency Graph!";
    }

    @DgsQuery
    public String greet(String name) {
        return "Hello, " + name + " From Dependency Graph!";
    }
}
