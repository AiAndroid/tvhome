package com.tv.ui.metro.model;

import com.google.gson.JsonObject;
import java.io.Serializable;

/**
 * Created by liuhuadonbg on 1/27/15.
 *
 *
 {
 "app_info": {},
 "cp": "iqiyi",
 "cp_id": "288655700",
 "h5_url": "http://m.iqiyi.com/v_19rrmjn3ug.html",
 "id": "v001804745",
 "pc_url": "http://www.iqiyi.com/v_19rrmjn3ug.html?src=3_69_145",
 "sdk_url": "",
 "vid": "V001123291"
 }
 */
public class PlaySource implements Serializable{
    private static final long serialVersionUID = 1L;
    public  String cp;
    public  int    cp_code;
    public  String cp_id;  // media ID from CP
    public  String h5_url;
    public  String id;
    public  String pc_url;
    public  String sdk_url;
    public  String vid;
    public  boolean h5_preferred;

    public  JsonObject app_info;
    public  JsonObject app_infos; //all the cps info
    public  boolean   offline;
    public DisplayItem.Settings extra;

    public String toString(){
        return "cp: "+cp + " cp_id:"+cp_id + " h5_url:"+h5_url + " id:"+id + " pc_url:"+pc_url + " sdk_url:"+sdk_url + " vid:"+vid + " app_info:"+app_info +" app_infos:"+app_infos;
    }
}
