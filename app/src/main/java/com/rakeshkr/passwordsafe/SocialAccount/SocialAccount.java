package com.rakeshkr.passwordsafe.SocialAccount;


public class SocialAccount {
    private long id;
    private String email;
    private String password;
    private String dis_name;
    private String domain_name;
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String emailAddress) {
        this.email = emailAddress;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String myPassword) {
        this.password = myPassword;
    }

    public String getDispName(){
        return dis_name;
    }

    public void setDispName(String dis_name){
        this.dis_name=dis_name;
    }

    public String getDomainName(){
        return domain_name;
    }

    public void setDomainName(String domain){
        this.domain_name=domain;
    }
}
