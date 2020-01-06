package com.mr.ott.upgrade.rest;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SessionID implements Serializable {
	
    private static final long serialVersionUID = 6826191735682596960L;

    private String id;
    
    public SessionID() {}

    public SessionID(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

