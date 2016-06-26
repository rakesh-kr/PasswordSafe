package com.rakeshkr.passwordsafe.Utility;



public class DataObject {
    private String mText1;
    private int imgId;
    private String count;

    public DataObject (String text1,int imageId,String counts){
        mText1 = text1;
        imgId=imageId;
        count=counts;
        //mText2=text2;
    }

    public String getmText1() {
        return mText1;
    }

    public void setmText1(String mText1) {
        this.mText1 = mText1;
    }

    public int getImgId(){
        return imgId;
    }

    public void setImgId(int id){
        this.imgId=id;
    }
    public String getAllCount(){
        return count;
    }

    public void setAllCount(String counts){
        this.count=counts;
    }

}
