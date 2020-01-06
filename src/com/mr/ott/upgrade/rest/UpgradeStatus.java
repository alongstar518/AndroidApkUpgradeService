package com.mr.ott.upgrade.rest;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UpgradeStatus implements Serializable {
	
    private static final long serialVersionUID = 6826191735682596960L;

    private String id;
    private String status;
    
    public UpgradeStatus() {}

    public UpgradeStatus(String id, String status) {
        this.id = id;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}