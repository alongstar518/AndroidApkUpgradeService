package com.mr.ott.upgrade.rest;


import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("api")
public class UpgradeService extends ResourceConfig {

    public UpgradeService() {
        packages("com.fasterxml.jackson.jaxrs.json");
        packages("com.mr.ott.upgrade.rest");
    }
}
