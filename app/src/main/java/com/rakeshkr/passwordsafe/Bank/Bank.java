package com.rakeshkr.passwordsafe.Bank;


public class Bank {
    private long id;
    private String bank_name;
    private String account_num;
    private String atm_card_num ;
    private String atm_pin;
    private String dis_name;
    private String exp_date;
    private String ifsc;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    public String getBankName() {
        return bank_name;
    }
    public void setBankName(String bankName) {
        this.bank_name = bankName;
    }
    public String getAcNum() {
        return account_num;
    }

    public void setAcNum(String ac_num) {
        this.account_num = ac_num;
    }

    public String getAtmCardNum() {
        return atm_card_num;
    }
    public void setAtmCardNum(String atm_num) {
        this.atm_card_num = atm_num;
    }
    public String getAtmPin() {
        return atm_pin;
    }
    public void setAtmPin(String pin) {
        this.atm_pin = pin;
    }
    public String getDispName(){
        return dis_name;
    }

    public void setDispName(String dis_name){
        this.dis_name=dis_name;
    }

    public String getExpiryDate(){
        return exp_date;
    }

    public void setExpiryDate(String date){
        this.exp_date=date;
    }

    public String getIFSC(){
        return ifsc;
    }

    public void setIFSC(String ifsc_code){
        this.ifsc=ifsc_code;
    }

}
