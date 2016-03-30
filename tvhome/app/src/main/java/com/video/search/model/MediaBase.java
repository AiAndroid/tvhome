package com.video.search.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.video.search.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhuzhenhua on 15-12-16.
 */
public class MediaBase implements Parcelable {

    public String mediaid;
    public String medianame;
    public String subtitle;
    public String category;// 分类名称,如电影/综艺
    public String posterurl;
    public List<List<Integer>> highlighting;
    public String md5;
    public String detail_title; // 标题
    public String source_icon_url;
    //public String source_icon_md5;
    public String premiere_date;

    public int midtype;// 内容类型， 0为节目，100为合集（专题是合集的一种），500为第三方应用类型，1000为API类型（比如个性化推荐），6000为普通节目列表，6001为二级栏目类型
    public int type;
    public int show_inner_icon = 1;
    public int price_icon_type = -1;
    public int title_icon_type = -1;
    public int year;

    public float douban_rating;
    public float xiaomi_rating;

    public CpExtra cp_extra_info;

    public MediaBase() {

    }

    protected MediaBase(Parcel in) {
        mediaid = in.readString();
        medianame = in.readString();
        category = in.readString();
        posterurl = in.readString();
        md5 = in.readString();
        midtype = in.readInt();
        douban_rating = in.readFloat();
        xiaomi_rating = in.readFloat();
        detail_title = in.readString();
        int arrL = in.readInt();
        highlighting = new ArrayList();
        for(int i = 0; i < arrL; ++ i)
        {
            int aL = in.readInt();
            List<Integer> pl = new ArrayList<Integer>();
            for(int j = 0; j < aL; ++ j)
            {
                pl.add(in.readInt());
            }
            highlighting.add(pl);
        }
    }

    public static final Creator<MediaBase> CREATOR = new Creator<MediaBase>() {
        @Override
        public MediaBase createFromParcel(Parcel in) {
            return new MediaBase(in);
        }

        @Override
        public MediaBase[] newArray(int size) {
            return new MediaBase[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mediaid);
        dest.writeString(medianame);
        dest.writeString(category);
        dest.writeString(posterurl);
        dest.writeString(md5);
        dest.writeInt(midtype);
        dest.writeFloat(douban_rating);
        dest.writeFloat(xiaomi_rating);
        dest.writeString(detail_title);
        dest.writeInt(highlighting.size());
        for(int i = 0; i < highlighting.size(); ++ i)
        {
            List<Integer> lst = highlighting.get(i);
            dest.writeInt(lst.size());
            for(Integer val : lst)
            {
                dest.writeInt(val);
            }
        }
    }

    public static class CpExtra {
        public String launch_info;
        public String mediaid;
        public String cp_id;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof MediaBase) {
            MediaBase m = (MediaBase) o;
            return mediaid.equals(m.mediaid);
        }
        return false;
    }

    public boolean isNormalItem() {
        return 0 == type;
    }

    public boolean isMoreItem() {
        return Constants.MEDIA_TYPE_MORE == type;
    }

}
