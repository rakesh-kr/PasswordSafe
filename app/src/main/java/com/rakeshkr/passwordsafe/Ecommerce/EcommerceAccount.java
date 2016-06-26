package com.rakeshkr.passwordsafe.Ecommerce;


public class EcommerceAccount {
    private long id;
    private String email;
    private String password;
    private String dis_name;
    private String seller;
    private String website;
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

    public String getSellerName(){
        return seller;
    }

    public void setSellerName(String mySeller){
        this.seller=mySeller;
    }

    public String getWebsiteName(){
        return website;
    }

    public void setWebsiteName(String site_address){
        this.website=site_address;
    }
}
