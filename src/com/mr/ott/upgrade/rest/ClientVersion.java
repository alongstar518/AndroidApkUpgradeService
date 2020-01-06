package com.mr.ott.upgrade.rest;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ClientVersion implements Serializable {
	
    private static final long serialVersionUID = 6826191735682596960L;

    private String ip;
    private String version;
    
    public ClientVersion() {}

    public ClientVersion(String ip, String version) {
        this.ip = ip;
        this.version = version;
    }

    public String getIp() {
        return ip;
   }

    public void setIp(String ip) {
       this.ip = ip;
    }

    public String getCategory() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
