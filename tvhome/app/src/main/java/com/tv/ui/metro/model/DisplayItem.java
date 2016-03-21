package com.tv.ui.metro.model;

import android.text.TextUtils;

import com.google.gson.internal.LinkedTreeMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

public class DisplayItem implements Serializable{
	private static final long serialVersionUID = 4L;

    public DisplayItem(){
        images = new ImageGroup();
        hint   = new Hint();
        target = new Target();
    }

	public static class UI extends  HashMap<String, Object> implements Serializable {
		private static final long serialVersionUID = 4L;
        public String name(){return (String)get("name");}
        public int    id()  {return (int)getFloat(String.valueOf(get("id")), -1.0f);}
        public int    rows(){return  (int)getFloat(String.valueOf(get("rows")), 1.0f);}
        public float  ratio()    {return  getFloat(String.valueOf(get("ratio")), 1.0f);}
        public int    display_count(){return  (int)getFloat(String.valueOf(get("display_count")), 2.0f);}
        public int    show_value(){return  (int)getFloat(String.valueOf(get("show_value")), 1.0f);}
        public int    show_rank(){return  (int)getFloat(String.valueOf(get("show_rank")), 1.0f);}
        public int    show_title(){return  (int)getFloat(String.valueOf(get("show_title")), 1.0f);}
        public int    show_vip(){return  (int)getFloat(String.valueOf(get("show_vip")), 0.0f);}
        public String left() {return  (String)get("left");}
        public String right(){return  (String)get("right");}
        public int    columns(){return (int)getFloat(String.valueOf(get("columns")), 1.0f);}
        public int    columnspan(){return   (int)getFloat(String.valueOf(get("columnspan")), 1.0f);}
        public int    rowspan(){return   (int)getFloat(String.valueOf(get("rowspan")), 1.0f);}
        public boolean    unitary(){return  get("unitary")==null?false:(boolean)get("unitary");}
        public int    x(){return  (int)getFloat(String.valueOf(((LinkedTreeMap) get("pos")).get("x")),0.0f);}
        public int    y(){return  (int)getFloat(String.valueOf(((LinkedTreeMap) get("pos")).get("y")),0.0f);}
        public int    w(){return  (int)getFloat(String.valueOf(((LinkedTreeMap) get("pos")).get("w")),1.0f);}
        public int    h(){return  (int)getFloat(String.valueOf(((LinkedTreeMap) get("pos")).get("h")),1.0f);}

        public UI clone(){
            UI item = new UI();
            item.put("name", get("name"));
            item.put("id",   get("id"));
            item.put("row_count",   get("row_count"));
            item.put("show_rank",   get("show_rank"));
            item.put("show_value",   get("show_value"));
            item.put("show_title",   get("show_title"));
            item.put("columns",   get("columns"));
            return item;
        }
		public String toString() {
			return " type:" + name() + "  id:" + id();
		}

	}

    public static class Filter  implements Serializable{
        private static final long serialVersionUID = 1L;
        public ArrayList<FilterItem> filters(){return filters;}

        public ArrayList<FilterItem> filters;
        public ArrayList<FilterType> all;
        public String                custom_filter_id_format;

        public static class FilterType implements Serializable{
            private static final long serialVersionUID = 1L;

            public String type;
            public String title;
            public ArrayList<String> tags;
        }
    }

    public static class FilterItem implements Serializable{
        private static final long serialVersionUID = 1L;
        public String title;
        public Target target;
        public String type;

        public static String custom_filter = "custom_filter";

        @Override
        public boolean equals(Object obj) {
            if(obj == null)
                return false;

            if(obj instanceof FilterItem){
                return  ((FilterItem)obj).title.equals(title);
            }

            return  false;
        }
    }

	public static class Times implements Serializable {
		private static final long serialVersionUID = 1L;
		public long updated;
		public long created;

        public Times(){
            updated = 0;//System.currentTimeMillis();
            created = updated;
        }

        public Times clone(){
            Times item = new Times();
            item.created = created;
            item.updated = updated;
            return item;
        }
	}
    public static class Target implements Serializable{
        private static final long serialVersionUID = 1L;
        public String entity;
        public Params params;
        public String url;
        public String action;

        public Target(){
            params = new Params();
        }
        public String toString(){
            return "url: "+url + " entity:" +entity + " params:"+params ;
        }

        public static class Params extends HashMap<String, String>implements Serializable {
            private static final long serialVersionUID = 1L;

            public String  viid()     {return get(viid);}
            public String  cp()       {return get("cp");}
            public int     offset()   {return getInt(get("offset"), 0);}
            public String  entity()   {return get("entity");}
            public boolean remove_ad(){return getBoolean(get(remove_ad), false);}
            public boolean h5()       {return getBoolean(get(h5), false);}
            public boolean prompt()   {return getBoolean(get(prompt), true);}
            public boolean new_task() {return getBoolean(get(new_task), false);}
            public boolean inner_html()   {return getBoolean(get(inner_html), false);}

            public String  app_name()   {return get("app_name");}
            public String  app_icon()   {return get("app_icon");}

            public String  android_action() {return get(android_action);}
            public String  android_mime()   {return get(android_mime);}

            public String android_component() {return get(android_component);}
            public String android_activty()   {return get(android_activity);}
            public String android_extra()     {return get(android_extra);}
            public String android_intent()    {return get("android_intent");}
            public String apk_url()           {return get(apk_url);}
            public int    apk_version()       {return getInt(get(apk_version), 0);}
            public String tick_url()          {return get(tick_url);}
            public String tick_url_1()          {return get(tick_url_1);}
            public String tick_url_2()          {return get(tick_url_2);}
            public String tick_url_3()          {return get(tick_url_3);}
            public String tick_url_4()          {return get(tick_url_4);}

            public String present_url()       {return get(present_url);}
            public String present_url_1()       {return get(present_url_1);}
            public String present_url_2()       {return get(present_url_2);}
            public String present_url_3()       {return get(present_url_3);}
            public String present_url_4()       {return get(present_url_4);}

            public String action_url()          {return get(action_url);}
            public String action_url_1()          {return get(action_url_1);}
            public String action_url_2()          {return get(action_url_2);}
            public String action_url_3()          {return get(action_url_3);}
            public String action_url_4()          {return get(action_url_4);}

            public String install_url()          {return get(install_url);}
            public String install_url_1()          {return get(install_url_1);}
            public String install_url_2()          {return get(install_url_2);}
            public String install_url_3()          {return get(install_url_3);}
            public String install_url_4()          {return get(install_url_4);}

            public String launch_url()            {return get("launch_url");}
            public String launch_url_1()          {return get("launch_url_1");}
            public String launch_url_2()          {return get("launch_url_2");}
            public String launch_url_3()          {return get("launch_url_3");}
            public String launch_url_4()          {return get("launch_url_4");}

            public String miui_ads()               {return get("miui_ads");}
            public String appstore_h5_url()        {return get("appstore_h5_url");}

            public final static String android_component = "android_component";
            public final static String android_activity  = "android_activity";
            public final static String android_action    = "android_action";
            public final static String android_mime      = "android_mime";
            public final static String prompt            = "prompt";
            public final static String entity            = "entity";
            public final static String offset            = "offset";
            public final static String viid              = "viid";
            public final static String cp                = "cp";
            public final static String remove_ad         = "remove_ad";
            public final static String h5                = "h5";
            public final static String apk_url           = "apk_url";
            public final static String android_extra     = "android_extra";
            public final static String new_task          = "new_task";
            public final static String apk_version       = "apk_version";
            public final static String inner_html       = "inner_html";
            public final static String tick_url          = "tick_url";
            public final static String tick_url_1          = "tick_url_1";
            public final static String tick_url_2          = "tick_url_2";
            public final static String tick_url_3          = "tick_url_3";
            public final static String tick_url_4          = "tick_url_4";

            public final static String present_url         = "present_url";
            public final static String present_url_1       = "present_url_1";
            public final static String present_url_2       = "present_url_2";
            public final static String present_url_3       = "present_url_3";
            public final static String present_url_4       = "present_url_4";

            public final static String action_url            = "action_url";
            public final static String action_url_1          = "action_url_1";
            public final static String action_url_2          = "action_url_2";
            public final static String action_url_3          = "action_url_3";
            public final static String action_url_4          = "action_url_4";

            public final static String install_url              = "install_url";
            public final static String install_url_1            = "install_url_1";
            public final static String install_url_2            = "install_url_2";
            public final static String install_url_3            = "install_url_3";
            public final static String install_url_4            = "install_url_4";
        }
    }

    public static int getInt(String str, int def){
        if(TextUtils.isEmpty(str)){
            return def;
        }

        int res = def;
        try{
            res = Integer.parseInt(str);
        }catch (Exception ne){}
        return res;
    }

    public static float getFloat(String str, float def){
        if(TextUtils.isEmpty(str)){
            return def;
        }

        float res = def;
        try{
            res = Float.parseFloat(str);
        }catch (Exception ne){}
        return res;
    }
    public static long getLong(String str, long def){
        if(TextUtils.isEmpty(str)){
            return def;
        }

        long res = def;
        try{
            res = Long.parseLong(str);
        }catch (Exception ne){}
        return res;
    }

    public static boolean getBoolean(String str, boolean def){
        if(TextUtils.isEmpty(str)){
            return def;
        }

        boolean res = def;
        try{
            res = Boolean.parseBoolean(str);
        }catch (Exception ne){}
        return res;
    }


    public static class Hint extends LinkedHashMap<String, String> implements Serializable{
        public String left() {return  get(left);}
        public String mid()  {return  get(center);}
        public String right(){return  get(right);}

        public final static String left   = "left";
        public final static String center = "center";
        public final static String right  = "right";
        public String toString(){
            return  "hint left:"+left() + " mid:"+mid() + " right:"+right();
        }
    }


    public String id;
    public String title;
    public String sub_title;
    public Hint   hint;
    public String desc;
	public String ns;
	public String type;
    public Target     target;
	public ImageGroup images;
	public UI         ui_type;
	public Times      times;

    public String     value;
    public Media      media;//why put here, for media detail episode list UI create, actually we should put this in VideoItem
    public People     people;//why put here, for people detail view

    public Settings   settings;//for server key-value settings
    public Meta       meta;
    public DisplayItem float_ads;

    public static class Meta extends HashMap<String, String> implements Serializable{
        private static final long serialVersionUID = 1L;
        public String page(){String page = get("page"); if(page == null) page="0"; return page;}
    }

    public static class Settings  extends HashMap<String, String> implements Serializable{
        private static final long serialVersionUID = 1L;

        public static final String edit_mode = "edit_mode";
        public static final String selected  = "selected";
        public static final String offset    = "offset";
        public static final String position  = "position";
        public static final String play_id   = "play_id";
        public static final String offline   = "offline";
        public static final String adposition_id   = "adposition_id";
    }

    public static class People implements Serializable{
        private static final long serialVersionUID = 1L;
        public String name;
        public String description;
        public String birthday;
        public Tags   tags;
    }

    public static class Tags extends HashMap<String, ArrayList<String>> implements Serializable{
        private static final long serialVersionUID = 1L;
        public ArrayList<String> genre(){return  get("genre");}
        public ArrayList<String> year(){return get("year");}
        public ArrayList<String> language(){return get("language");}
        public ArrayList<String> area(){return get("area");}
        public ArrayList<String> others(){return get("others");}
        public ArrayList<String> profession(){return get("profession");}
    }

    public static class Media implements Serializable {
        private static final long serialVersionUID = 2L;

        public String description;
        public float  score;
        public Tags   tags;

        public String              episode_index;
        public ArrayList<Episode>  items;
        public ArrayList<DisplayItem>  stars;
        public Stuff               stuff;
        public String              poster;
        public String              date;
        public String              phrase;
        public ArrayList<CP>       cps;
        public String              id;
        public String              name;
        public DisplayLayout       display_layout;
        public String              category_name;
        public int                 episode_number=0;
        public PayLoad             payload;

        public String category;

        public static class DisplayLayout implements Serializable{
            private static final long serialVersionUID = 1L;
            public String type        = "tv";
            public int    max_display = 8;

            public static final String TYPE_TV      = "tv"; // multiset TV play
            public static final String TYPE_OFFLINE = "offline";
            public static final String TYPE_VARIETY = "variety";

            public DisplayLayout clone(){
                DisplayLayout dl = new DisplayLayout();
                dl.type = this.type;
                dl.max_display = this.max_display;
                return dl;
            }
        }

        public static class PayLoad extends HashMap<String, String> implements Serializable{
            public String ispurchase(){return  get("ispurchase");}
            public String pcode(){return  get("pcode");}
            public long clientid(){return  getLong(get("clientid"), 2882303761517310776L);}
            public String redirecturl(){return  get("redirecturl");}
        }

        public static class CP extends HashMap<String, String> implements Serializable{
            private static final long serialVersionUID = 2L;
            public String cp(){return  get("cp");}
            public String name(){return  get("name");}
            public String icon(){return  get("icon");}
            public String app_icon(){return get("app_icon");} //for download
            public String apk_url(){return get("apk_url");}  //for download
            public boolean vitem_offline(){return getBoolean(get("vitem_offline"), false);} //
            public int     offline_rank(){return getInt(get("offline_rank"), 0);} //
            public boolean vitem_downloadable(){return  getBoolean(get("vitem_downloadable"), true);}
            public String clientid(){return  get("clientid");}
        }

        public static class Episode /*extends HashMap<String, String> */implements Serializable, Comparable<Episode>{
            private static final long serialVersionUID = 2L;
            public String                  date;
            public int                     episode;
            public String                  id;
            public String                  name;
            public int                     download_trys=0;
            public Settings                settings;
            public DownCP                  cp;
            public PayLoad                 payload;

            public boolean haveDownloadSource(){
                if(cp != null && cp.haveDownloadSource()){
                    return true;
                }

                return false;
            }

            public static class DownCP extends HashMap<String, String>  implements Serializable{
                private static final long serialVersionUID = 1L;

                public String availableCP;
                public boolean haveDownloadSource(){
                    Set<String> sets = this.keySet();
                    if(sets != null)
                    for(String key: sets){
                        if(getBoolean(get(key), false)){
                            availableCP = key;
                            return true;
                        }
                    }

                    return false;
                }
            }

            @Override
            public boolean equals(Object obj) {
                if(obj == null)
                    return false;

                if(obj instanceof Episode){
                    return  ((Episode)obj).id.equals(id);
                }

                return  false;
            }

            public String toString(){
                return "episode:"+episode + " id:"+id + " name:"+name+ " date:"+date;
            }

            public int compareString(String s1, String s2){
                if (TextUtils.isEmpty(s1) && TextUtils.isEmpty(s2)){
                    return 0;
                }else if (TextUtils.isEmpty(s1) && !TextUtils.isEmpty(s2)){
                    return -1;
                }else if (!TextUtils.isEmpty(s1) && TextUtils.isEmpty(s2)){
                    return 1;
                }else {
                    return s1.compareToIgnoreCase(s2);
                }
            }

            @Override
            public int compareTo(Episode another) {
                if (another == null){
                    return 1;
                }else {
                    return this.episode - another.episode;
//                    int result = compareString(this.date, another.date);
//                    if (result == 0){
//                        return this.episode - another.episode;
//                    }else {
//                        return result;
//                    }
                }
            }
        }

        public static class Stuff extends HashMap<String, ArrayList<Stuff.Star>>  implements Serializable{
            private static final long serialVersionUID = 1L;
            public ArrayList<Star> director(){return  get("director");}
            public ArrayList<Star> writer()  {return get("writer");}
            public ArrayList<Star> actor()   {return get("actor");}

            public static class Star implements Serializable {
                private static final long serialVersionUID = 1L;
                public String id;
                public String name;
            }
        }
    }

	public String toString() {
		return " ns:"         + ns +
                " id:"        + id +
                " target="    + target  +
                " title:"     + title +
                " sub_title: "+ sub_title+
                " desc: "     + desc+
                " images:"    + images +
                " _ui:"       + ui_type +
                " hint: "     + hint +
                " settings:"  + settings ;
	}

    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;

        if(obj instanceof DisplayItem){
            return  ((DisplayItem)obj).id.equals(id);
        }

        return  false;
    }


}
