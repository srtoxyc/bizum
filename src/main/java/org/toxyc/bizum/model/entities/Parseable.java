package org.toxyc.bizum.model.entities;

public interface Parseable {
    public final static String DELIM = ";";
    
    String toJSON();
    String toXML();
    String toTXT();
}
