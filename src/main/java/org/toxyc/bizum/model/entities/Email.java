package org.toxyc.bizum.model.entities;

import java.util.StringTokenizer;

public class Email implements Parseable {
    public final static String EMAIL_REGEX   = "^[A-Za-z0-9+_.-]+@(.+)$";
    public final static String EMAIL_DELIM   = "@";

    private String email;
    private String identifier;
    private String service;

    public Email(String email) {
        this.email = email;

        this.processMail(email);
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdentifier() {
        return identifier;
    }
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getService() {
        return service;
    }
    public void setService(String service) {
        this.service = service;
    }

    /**
     * Process the email to get the identifier and the service
     * @param email The email to process
     */
    private void processMail(String email) {
        StringTokenizer st = new StringTokenizer(email, EMAIL_DELIM);
        this.setIdentifier(st.nextToken());
        this.setService(st.nextToken());
    }

    @Override
    public String toString() {
        return this.toTXT();
    }

    @Override
    public String toJSON() {
        return String.format("{\"email\":\"%s\"}", this.getEmail());
    }

    @Override
    public String toXML() {
        return String.format("<email>%s</email>", this.toString());
    }

    @Override
    public String toTXT() {
        return String.format("%s%s%s", this.getIdentifier(), EMAIL_DELIM, this.getService());
    }
}