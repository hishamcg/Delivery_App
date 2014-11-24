package com.strata.deliveryapp;


public class SharedValue{
    private volatile static SharedValue shareData;
    public static SharedValue data(){
    if(shareData == null){
        synchronized (SharedValue.class) {
            if (shareData == null) {
                shareData = new SharedValue();
            }
        }
    }
    return shareData;
    }  

    public String memb_no;
    public String branch_id;
    public String name;
    
}
