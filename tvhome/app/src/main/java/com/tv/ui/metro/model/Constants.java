package com.tv.ui.metro.model;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

/**
 * Created by liuhuadonbg on 3/2/15.
 */
public class Constants {
    public static boolean DEBUG = true;
    public static final String BENCHMARK = "benchmark";
    public static final String Local_Dir_Video = "local_dir";
    public static final String Local_Video = "local";
    public static final String Favor_Video = "favor";
    public static final String History_Video = "history";
    public static final String Offline_Video = "offline";
    public static final String DLNA_DIR     = "dlna_dir";
    public static final String DLNA_DEVICE   = "dlna_device";

    public static final String Video_ID_Favor   = "play_favor";
    public static final String Video_ID_History = "play_history";
    public static final String Video_ID_Offline = "play_offline";
    public static final String Video_ID_Local   = "play_local";
    public static final String Video_ID_Multi_Filter = "filter_select";

    public static final String Entiry_Long_Video       = "pvideo";
    public static final String Entity_LONG_Video_PLAY  = "pvideo_play";
    public static final String Entity_Short_Video = "svideo";

    public static final String Entity_Local_Video         = "local_video";
    public static final String Entity_Local_dir_Video     = "local_dir_video";
    public static final String Entity_Local_offline_Video = "local_offline_video";
    public static final String Entity_Download            = "download";
    public static final String Entity_Intent              = "intent";
    public static final String Entity_APP_UPGRADE         = "app_upgrade";
    public static final String Entity_Play                = "play";
    public static final String Entity_Play_Url            = "url_play";

    public static final String Entity_Player              = "player";
    public static final String Entity_Play_H5_Url         = "h5player";
    public static final String Entity_Search_Video        = "search";
    public static final String Entity_Search_Result_Video = "search_result";
    public static final String Entity_Album               = "album";
    public static final String Entity_Album_Collection    = "album_collection";
    public static final String Entity_DLNA_DIR            = "dlna_dir";
    public static final String Entity_DLNA_DIR_VIDEO      = "dlna_dir_video";
    public static final String Entity_Http_Play_Source    = "http_video";
    public static final String Entity_Channel             = "channel";
    public static final String Entity_Home                = "home";
    public static final String Entity_Normal              = "normal";

    public static final String Entity_People              = "people";
    public static final String Entity_Browser             = "browser_diversion";

    public static final String Miui_Video_Dir    = "MIUI/Video/files/";
    public static final String VIDEO_BOOKMARK_ID = "bookmark.r";

    public static final String VIDEO_SCHEMA       = "mvschema";
    public static final String VIDEO_HOST         = "video";
    public static final String VIDEO_PATH_ITEM    = "item";
    public static final String VIDEO_PATH_ALBUM   = "album";
    public static final String VIDEO_PATH_CHANNEL_FILTER  = "channelfilter";
    public static final String VIDEO_PATH_SEARCH  = "search";
    public static final String VIDEO_PATH_FILTER  = "filter";
    public static final String TV_HOST            = "tvlive";
    public static final String LIVESHOW_HOST      = "liveshow";
    public static final String VIDEO_PATH_DETAIL  = "detail";
    public static final String VIDEO_PATH_PEOPLE  = "people";
    public static final String VIDEO_ID           = "id";
    public static final String REF                = "ref";
    public static final String VIDEO_CP           = "cp";
    public static final String VIDEO_TITLE           = "title";
    public static final String APP_ID = "2882303761517147566";
    public static final String APP_KEY = "5481714735566";
    public static final String APP_Category = "21352B8F52038B188540F1909B32726E";

    public static final int MIVIDEO_SESSION_EXPIRED = 417;
    public static final int MIVIDEO_FORCE_UPGRADE   = 418;
    public static final String EXPIRED_BROADCAST            = "com.miui.video_login_expired";
    public static final String EXPIRED_UPGRADE_BROADCAST    = "com.miui.video_force_upgrade";

    public static final String Volley_Cache_Dir = "volley";
    public static final String Picasso_Cache_Dir = "picasso-cache";

    //begin for player
    public static final String ACTION_PLAY_BY_HTML5 = "duokan.intent.action.PLAY_BY_HTML5";
    //end for player

    public static final String SCHEMA_SEARCH = "mivideo://video/search";
    public static final String miwifiapk_url = "http://bigota.miwifi.com/xiaoqiang/client/xqapp_v2.apk";

    public static final Integer picasso_tag = new Integer(100);

    public static class  TestData{
        public static Block<DisplayItem> createTitleBlock(Context context, String title){
            Block<DisplayItem> item = new Block<DisplayItem>();
            item.title = title;
            item.ui_type = new DisplayItem.UI();
            item.ui_type.put("id", String.valueOf(LayoutConstant.linearlayout_title));
            return item;
        }

        public static Block<DisplayItem> createO2OLineBlock(Context context, String title) {
            Block<DisplayItem> item = new Block<DisplayItem>();
            item.title = title;
            item.ui_type = new DisplayItem.UI();
            item.ui_type.put("id", String.valueOf(LayoutConstant.linearlayout_none));

            item.target = new DisplayItem.Target();
            item.target.entity = Entity_Intent;
            item.target.url    = "o2o://movies/home";
            item.target.params.put("android_component", "com.xiaomi.o2o");
            //item.target.params.android_activty   = "com.xiaomi.o2o.activity.O2OTabActivity";
            return item;
        }

        public static Block<DisplayItem> createLineBlock(Context context, String title) {
            Block<DisplayItem> item = new Block<DisplayItem>();
            item.title = title;
            item.ui_type = new DisplayItem.UI();
            item.ui_type.put("id", String.valueOf(LayoutConstant.linearlayout_none));

            item.target = new DisplayItem.Target();
            item.target.entity = Entity_Intent;

            return item;
        }

        public static Block<DisplayItem> createCPLineBlock(Context context, String title) {
            Block<DisplayItem> item = new Block<DisplayItem>();
            item.title = title;
            item.ui_type = new DisplayItem.UI();
            item.ui_type.put("id", String.valueOf(LayoutConstant.linearlayout_none));

            item.target = new DisplayItem.Target();
            item.target.entity = Entity_Intent;
            item.target.url    = "http://cp.mi.com";

            return item;
        }

        public static Block<DisplayItem> createAppMarketLineBlock(Context context, String title) {
            Block<DisplayItem> item = new Block<DisplayItem>();
            item.title = title;
            item.ui_type = new DisplayItem.UI();
            item.ui_type.put("id" , String.valueOf(LayoutConstant.linearlayout_none));

            item.target = new DisplayItem.Target();
            item.target.entity = Entity_Intent;

            item.target.params.put("android_component", "com.xiaomi.market");
            item.target.params.put("android_activty", "com.xiaomi.market.ui.MarketTabActivity");

            return item;
        }

        public static Block<DisplayItem> createTVPromotionLineBlock( String title) {
            Block<DisplayItem> item = new Block<DisplayItem>();
            item.title = title;
            item.ui_type = new DisplayItem.UI();
            item.ui_type.put("id" , String.valueOf(LayoutConstant.linearlayout_single_poster_out));

            item.target = new DisplayItem.Target();
            item.target.entity = Entity_Intent;
            item.target.url = "http://www.mi.com/mitv40/";
            item.title = title;

            item.target.params.put(DisplayItem.Target.Params.android_action, Intent.ACTION_VIEW);
            item.target.params.put(DisplayItem.Target.Params.android_component, "com.android.browser");
            item.images = new ImageGroup();
            Image image = new Image();
            image.url = "http://image.box.xiaomi.com/mfsv2/download/s010/p011zBc2PmEB/FdRLhFCT1P3v8W.png";
            item.images.put("poster", image);

            item.hint = new DisplayItem.Hint();
            item.hint.put(DisplayItem.Hint.left, title);

            return item;
        }

        public static Block<DisplayItem> createSinglePosterBlock() {
            Block<DisplayItem> item = new Block<DisplayItem>();
            item.ui_type = new DisplayItem.UI();
            item.ui_type.put("id" , String.valueOf(LayoutConstant.single_poster_ad));
            return item;
        }

        public static Block<DisplayItem> createAppUpgradeLineBlock( String title) {
            Block<DisplayItem> item = new Block<DisplayItem>();
            item.title = title;
            item.ui_type = new DisplayItem.UI();
            item.ui_type.put("id" , String.valueOf(LayoutConstant.linearlayout_single_poster_out));


            item.target = new DisplayItem.Target();
            item.target.entity = Entity_APP_UPGRADE;
            item.target.url = "http://app.mi.com/download/27161";
            item.title = title;

            item.target.params.put(DisplayItem.Target.Params.android_action, Intent.ACTION_VIEW);
            item.target.params.put(DisplayItem.Target.Params.android_component, "com.android.browser");
            item.images = new ImageGroup();
            Image image = new Image();
            image.url = "http://image.box.xiaomi.com/mfsv2/download/s010/p011zBc2PmEB/FdRLhFCT1P3v8W.png";
            item.images.put("poster", image);

            item.hint = new DisplayItem.Hint();
            item.hint.put(DisplayItem.Hint.center, title);

            return item;
        }

        public static Block<DisplayItem> createGamesBlock() {
            Block<DisplayItem> item = new Block<DisplayItem>();
            item.title = "";
            item.ui_type = new DisplayItem.UI();
            item.ui_type.put("id" , String.valueOf(LayoutConstant.list_small_icon));
            item.ui_type.put("row_count", "1");

            item.items = new ArrayList<DisplayItem>();
            {
                {
                    DisplayItem child = new DisplayItem();
                    child.title = "《全民奇迹》";
                    child.sub_title = "公测庆典，一款以《奇迹MU》为蓝本的动作手游，游戏以Unity3D引擎打造.";
                    child.target = new DisplayItem.Target();
                    child.target.entity = Constants.Entity_Download;
                    child.target.url = "http://app.mi.com/download/79047";
                    child.images = new ImageGroup();
                    Image image = new Image();
                    image.url = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/0dd1b652ee178445210c95093ceb434a15b2b8983";
                    child.images.put("icon", image);

                    item.items.add(child);
                }
                {
                    DisplayItem child = new DisplayItem();
                    child.title = "卷皮9.9包邮";
                    child.sub_title = "武汉奇米网络科技有限公司: 一款9.9元包邮移动购物应用，商品主打低价超值，数千万淘宝用户的选择";
                    child.target = new DisplayItem.Target();
                    child.target.entity = Constants.Entity_Download;
                    child.target.url = "http://app.mi.com/download/29845";
                    child.images = new ImageGroup();
                    Image image = new Image();
                    image.url = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/00c613511b8ff4f633c969500784aa9d8d097e77d";
                    child.images.put("icon", image);
                    item.items.add(child);
                }
            }
            return  item;
        }

        public static Block<DisplayItem> createGridSquareBlock(){
            Block<DisplayItem> item = new Block<DisplayItem>();
            item.title = "";
            item.ui_type = new DisplayItem.UI();
            item.ui_type.put("id", String.valueOf(LayoutConstant.grid_autospan_square));
            item.ui_type.put("row_count", String.valueOf(3));
            item.ui_type.put("ratio", String.valueOf(1.0f));
            item.items = new ArrayList<DisplayItem>();
            {
                DisplayItem child = new DisplayItem();
                child.title = "武侠电影里的经典音乐";
                child.sub_title = "张栋梁";
                child.id = "http://www.letv.com/ptv/vplay/24093058.html";
                child.target = new DisplayItem.Target();
                child.target.entity = "intent";
                child.target.url = "http://www.letv.com/ptv/vplay/24093058.html";
                child.images = new ImageGroup();
                Image img = new Image();
                img.url = "http://img.taopic.com/uploads/allimg/130823/318760-130R320511178.jpg";
                child.images.put("poster", img);
                child.hint = new DisplayItem.Hint();
                child.hint.put("left", "推荐");
                item.items.add(child);
            }
            {
                DisplayItem child = new DisplayItem();
                child.title = "异想记";
                child.sub_title = "杨幂";
                child.id = "http://www.letv.com/ptv/vplay/24093058.html";
                child.target = new DisplayItem.Target();
                child.target.entity = "intent";
                child.target.url = "http://www.letv.com/ptv/vplay/24093058.html";
                child.images = new ImageGroup();
                Image img = new Image();
                img.url = "http://pic.7y7.com/201112/2011120941228493.jpg";
                child.images.put("poster", img);
                child.hint = new DisplayItem.Hint();
                child.hint.put("center", "2015-12-09");
                item.items.add(child);
            }
            {
                DisplayItem child = new DisplayItem();
                child.title = "假的";
                child.sub_title = "张超";
                child.id = "http://www.letv.com/ptv/vplay/24093058.html";
                child.target = new DisplayItem.Target();
                child.target.entity = "intent";
                child.target.url = "http://www.letv.com/ptv/vplay/24093058.html";
                child.images = new ImageGroup();
                Image img = new Image();
                img.url = "http://p5.img.cctvpic.com/20111117/images/1321516178121_1321516178121_r.jpg";
                child.images.put("poster", img);
                child.hint = new DisplayItem.Hint();
                child.hint.put("right", "评分:9.5");
                item.items.add(child);
            }
            return item;
        }

        public static Block<DisplayItem> createGridPortBlock(){
            Block<DisplayItem> item = new Block<DisplayItem>();
            item.title = "";
            item.ui_type = new DisplayItem.UI();
            item.ui_type.put("id", String.valueOf(LayoutConstant.grid_autospan_square));
            item.ui_type.put("row_count", String.valueOf(3));
            item.ui_type.put("ratio", String.valueOf(320/222.0f));
            item.items = new ArrayList<DisplayItem>();
            {
                DisplayItem child = new DisplayItem();
                child.title = "大秧歌";
                child.sub_title = "打狗棍原班人马再回归";
                child.id = "V000794422";
                child.target = new DisplayItem.Target();
                child.target.entity = "pvideo";
                child.target.url = "o/V000794422";
                child.images = new ImageGroup();
                Image img = new Image();
                img.url = "http://image.box.xiaomi.com/mfsv2/download/s010/p01r3k8u63k2/Oejahgznya1llM.jpg";
                child.images.put("poster", img);
                child.hint = new DisplayItem.Hint();
                child.hint.put("left", "79 集全");
                item.items.add(child);
            }
            {
                DisplayItem child = new DisplayItem();
                child.title = "秦时明月";
                child.sub_title = "蒋劲夫化身荆轲之子";
                child.id = "V000913653";
                child.target = new DisplayItem.Target();
                child.target.entity = "pvideo";
                child.target.url = "o/V000913653";
                child.images = new ImageGroup();
                Image img = new Image();
                img.url = "http://image.box.xiaomi.com/mfsv2/download/s010/p01Xu0lIO7ne/o6kgg3icUiJSbN.jpg";
                child.images.put("poster", img);
                child.hint = new DisplayItem.Hint();
                child.hint.put("left", "更新至 12 集");
                item.items.add(child);
            }
            {
                DisplayItem child = new DisplayItem();
                child.title = "北上广不相信眼泪";
                child.sub_title = "隐婚夫妻的奋斗故事";
                child.id = "V000839844";
                child.target = new DisplayItem.Target();
                child.target.entity = "pvideo";
                child.target.url = "o/V000839844";
                child.images = new ImageGroup();
                Image img = new Image();
                img.url = "http://image.box.xiaomi.com/mfsv2/download/s010/p01nPKt22AIw/5TRksbSL4uLKo9.jpg";
                child.images.put("poster", img);
                child.hint = new DisplayItem.Hint();
                child.hint.put("left", "44 集全");
                item.items.add(child);
            }
            return item;
        }

        public static Block<DisplayItem> createGridLandBlock(){
            Block<DisplayItem> item = new Block<DisplayItem>();
            item.title = "";
            item.ui_type = new DisplayItem.UI();
            item.ui_type.put("id", String.valueOf(LayoutConstant.grid_autospan_square));
            item.ui_type.put("row_count", String.valueOf(2));
            item.ui_type.put("ratio", String.valueOf(180/315.0f));
            item.items = new ArrayList<DisplayItem>();
            {
                DisplayItem child = new DisplayItem();
                child.title = "芈月传";
                child.sub_title = "芈姝秦王大婚！后宫即将开撕";
                child.id = "http://www.letv.com/ptv/vplay/24093058.html";
                child.target = new DisplayItem.Target();
                child.target.entity = "intent";
                child.target.url = "http://www.letv.com/ptv/vplay/24093058.html";
                child.images = new ImageGroup();
                Image img = new Image();
                img.url = "http://image.box.xiaomi.com/mfsv2/download/s010/p018ln8r5owe/fVxWjlSR7xBxtp.jpg";
                child.images.put("poster", img);
                child.hint = new DisplayItem.Hint();
                child.hint.put("left", "更新至 18 集");
                item.items.add(child);
            }
            {
                DisplayItem child = new DisplayItem();
                child.title = "长在面包树上的女人";
                child.sub_title= "黄宗泽抱怨唐嫣泄露床事";
                child.id = "V000924683";
                child.target = new DisplayItem.Target();
                child.target.entity = "pvideo";
                child.target.url = "o/V000924683";
                child.images = new ImageGroup();
                Image img = new Image();
                img.url = "http://image.box.xiaomi.com/mfsv2/download/s010/p014GiCSjbTV/pM5xRD6OTsN2ee.jpg";
                child.images.put("poster", img);
                child.hint = new DisplayItem.Hint();
                child.hint.put("left", "更新至 16 集");
                item.items.add(child);
            }
            return item;
        }

        public static Block<DisplayItem> createSearchBlock(){
            Block<DisplayItem> item = new Block<DisplayItem>();
            item.title = "";
            item.ui_type = new DisplayItem.UI();
            item.ui_type.put("id" , String.valueOf(LayoutConstant.linearlayout_list_text));
            item.items = new ArrayList<DisplayItem>();
            {
                DisplayItem child = new DisplayItem();
                child.title = "《我是证人》热映杨幂疑为 “只约” 做宣传";
                child.sub_title = "杨幂";
                child.target = new DisplayItem.Target();
                child.target.entity = Constants.Entity_Browser;
                child.target.url = "o/V000632940";
                child.id = "o/V000632940";

                child.images = new ImageGroup();
                Image image = new Image();
                image.url = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/0dd1b652ee178445210c95093ceb434a15b2b8983";
                child.images.put("icon", image);
                item.items.add(child);
            }
            {
                DisplayItem child = new DisplayItem();
                child.title = "一个从小到大照相时都是心机boy的男孩纸";
                child.target = new DisplayItem.Target();
                child.target.entity = Constants.Entity_Browser;
                child.target.url = "o/V000632940";
                child.id = "o/V000632940";

                child.images = new ImageGroup();
                Image image = new Image();
                image.url = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/0dd1b652ee178445210c95093ceb434a15b2b8983";
                child.images.put("icon", image);
                item.items.add(child);
            }
            {
                DisplayItem child = new DisplayItem();
                child.title = "嫩的出水！胡歌学生时代青涩照曝光";
                child.sub_title = "胡歌";
                child.target = new DisplayItem.Target();
                child.target.entity = Constants.Entity_Browser;
                child.target.url = "o/V000632940";
                child.id = "o/V000632940";

                child.images = new ImageGroup();
                Image image = new Image();
                image.url = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/0dd1b652ee178445210c95093ceb434a15b2b8983";
                child.images.put("icon", image);
                item.items.add(child);
            }

            return  item;
        }

        public static Block<DisplayItem> createActorsBlock() {
            Block<DisplayItem> item = new Block<DisplayItem>();
            item.title = "";
            item.ui_type = new DisplayItem.UI();
            item.ui_type.put("id" , String.valueOf(LayoutConstant.grid_circle_icon));
            item.ui_type.put("row_count", "4");

            item.items = new ArrayList<DisplayItem>();
            {
                for (int i = 0; i < 11; i++){
                    DisplayItem child = new DisplayItem();
                    child.title = "陈冠希";
                    child.target = new DisplayItem.Target();
                    child.target.entity = Constants.Entity_People;
                    child.target.url = "o/V000632940";
                    child.id = "o/V000632940";

                    child.images = new ImageGroup();
                    Image image = new Image();
                    image.url = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/0dd1b652ee178445210c95093ceb434a15b2b8983";
                    child.images.put("poster", image);
                    item.items.add(child);
                }
            }
            return  item;
        }

        public static Block<DisplayItem> createAppsBlock(){
            Block<DisplayItem> item = new Block<DisplayItem>();
            item.title = "";
            item.ui_type = new DisplayItem.UI();
            item.ui_type.put("id" , String.valueOf(LayoutConstant.grid_middle_icon));
            item.ui_type.put("row_count", "2");

            item.items = new ArrayList<DisplayItem>();
            //sohu
            {
                DisplayItem child = new DisplayItem();
                child.title = "美团团购-食影";
                child.sub_title = "北京三快科技有限公司";
                child.target = new DisplayItem.Target();
                child.target.entity = Entity_Download;
                child.target.url    = "http://app.mi.com/download/98";
                child.target.params.put(DisplayItem.Target.Params.android_component, "com.sankuai.meituan");
                child.target.params.put(DisplayItem.Target.Params.android_activity,   "com.sankuai.meituan.activity.Welcome");

                child.images = new ImageGroup();
                Image image = new Image();
                image.url = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/bcb99571-36db-4f87-b7e5-12cdbaa95766";
                child.images.put("icon", image);

                item.items.add(child);
            }
            //pptv
            {
                DisplayItem child = new DisplayItem();
                child.title = "违章查询助手";
                child.sub_title = "北京车之家信息技术有限公司";
                child.target = new DisplayItem.Target();
                child.target.entity = Entity_Download;
                child.target.url    = "http://app.mi.com/download/53591";
                child.images = new ImageGroup();
                Image image = new Image();
                image.url = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/a57c9228-208f-4c0f-950c-f45075d2c9a6";
                child.images.put("icon", image);

                item.items.add(child);
            }
            //iqiyi
            {
                DisplayItem child = new DisplayItem();
                child.title = "捕鱼达人3";
                child.sub_title = "北京触控科技有限公司";
                child.target = new DisplayItem.Target();
                child.target.entity = Entity_Download;
                child.target.url    = "http://app.mi.com/download/66889";
                child.images = new ImageGroup();
                Image image = new Image();
                image.url = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/0a5915443b7d241bd329d8490173fb5595327c294";
                child.images.put("icon", image);

                item.items.add(child);
            }
            //fengxing
            {
                DisplayItem child = new DisplayItem();
                child.title = "英语王国";
                child.sub_title = "深圳墨齐致知网络科技有限公司";
                child.target = new DisplayItem.Target();
                child.target.entity = Entity_Download;
                child.target.url    = "http://app.mi.com/download/77622";
                child.images = new ImageGroup();
                Image image = new Image();
                image.url = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/06b4c3530217b456c19a3a4bc728855732eee2271";
                child.images.put("icon", image);

                item.items.add(child);
            }
            //phenix
            {
                DisplayItem child = new DisplayItem();
                child.title = "股票视野";
                child.sub_title = "北京视野金服信息技术有限公司";
                child.target = new DisplayItem.Target();
                child.target.entity = Entity_Download;
                child.target.url    = "http://app.mi.com/download/87322";
                child.images = new ImageGroup();
                Image image = new Image();
                image.url = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/0e1fc948682e44d6a20a1f3161e62c5a5e411d670";
                child.images.put("icon", image);

                item.items.add(child);
            }
            //phenix
            {
                DisplayItem child = new DisplayItem();
                child.title = "微信";
                child.sub_title = "腾讯科技（深圳）有限公司广州分公司";
                child.target = new DisplayItem.Target();
                child.target.entity = Entity_Download;
                child.target.url    = "http://app.mi.com/download/1122";
                child.target.params.put(DisplayItem.Target.Params.android_component, "com.tencent.mm");
                child.target.params.put(DisplayItem.Target.Params.android_activity,   "com.tencent.mm.ui.LauncherUI");

                child.images = new ImageGroup();
                Image image = new Image();
                image.url = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/f4ec7499-a7bc-48af-813d-3aaf86c17e8a";
                child.images.put("icon", image);

                item.items.add(child);
            }

            return item;
        }

        public static Block<DisplayItem> createQuickTopBlock(){
            Block<DisplayItem> item = new Block<DisplayItem>();
            item.title = "";
            item.ui_type = new DisplayItem.UI();
            item.ui_type.put("id" , String.valueOf(LayoutConstant.linearlayout_top_icon));
            item.ui_type.put("row_count", "5");

            item.items = new ArrayList<DisplayItem>();
            {
                DisplayItem child = new DisplayItem();
                child.title = "电视剧";
                child.target = new DisplayItem.Target();
                child.target.entity = Entity_Intent;
                child.target.params.put("android_action", Intent.ACTION_VIEW);
                child.target.url    = "http://cp.mi.com";
                child.images = new ImageGroup();
                Image image = new Image();
                image.url = "http://pica.nipic.com/2007-10-07/2007107203958502_2.jpg";
                child.images.put("icon", image);

                item.items.add(child);
            }
            {
                DisplayItem child = new DisplayItem();
                child.title = "电影";
                child.target = new DisplayItem.Target();
                child.target.entity = Entity_Intent;
                child.target.params.put("android_action", Intent.ACTION_VIEW);
                child.target.url    = "http://cp.mi.com";
                child.images = new ImageGroup();
                Image image = new Image();
                image.url = "http://pica.nipic.com/2007-10-07/2007107203958502_2.jpg";
                //image.url = "http://pic.baike.soso.com/p/20100629/bki-20100629110833-109286196.jpg";
                child.images.put("icon", image);

                item.items.add(child);
            }
            {
                DisplayItem child = new DisplayItem();
                child.title = "新年快乐";
                child.target = new DisplayItem.Target();
                child.target.entity = Entity_Intent;
                child.target.params.put("android_action", Intent.ACTION_VIEW);
                child.target.url    = "http://cp.mi.com";
                child.images = new ImageGroup();
                Image image = new Image();
                image.url = "http://img.taopic.com/uploads/allimg/121218/234725-12121Q4452576.jpg";
                child.images.put("icon", image);

                item.items.add(child);
            }
            {
                DisplayItem child = new DisplayItem();
                child.title = "综艺";
                child.target = new DisplayItem.Target();
                child.target.entity = Entity_Intent;
                child.target.params.put("android_action", Intent.ACTION_VIEW);
                child.target.url    = "http://cp.mi.com";
                child.images = new ImageGroup();
                Image image = new Image();
                image.url = "http://pica.nipic.com/2007-10-07/2007107203958502_2.jpg";
                child.images.put("icon", image);

                item.items.add(child);
            }
            {
                DisplayItem child = new DisplayItem();
                child.title = "动漫";
                child.target = new DisplayItem.Target();
                child.target.entity = Entity_Intent;
                child.target.params.put("android_action", Intent.ACTION_VIEW);
                child.target.url    = "http://cp.mi.com";
                child.images = new ImageGroup();
                Image image = new Image();
                image.url = "http://pica.nipic.com/2007-10-07/2007107203958502_2.jpg";
                child.images.put("icon", image);

                item.items.add(child);
            }

            return item;
        }

        public static Block<DisplayItem> createCaiPiaoBlock(){
            Block<DisplayItem> item = new Block<DisplayItem>();
            item.title = "";
            item.ui_type = new DisplayItem.UI();
            item.ui_type.put("id" , String.valueOf(LayoutConstant.grid_small_icon));
            item.ui_type.put("row_count", "6");

            item.items = new ArrayList<DisplayItem>();
            //sohu
            {
                DisplayItem child = new DisplayItem();
                child.title = "双色球";
                child.target = new DisplayItem.Target();
                child.target.entity = Entity_Intent;
                child.target.params.put("android_action", Intent.ACTION_VIEW);
                child.target.url    = "http://cp.mi.com";
                child.images = new ImageGroup();
                Image image = new Image();
                image.url = "http://p7.sinaimg.cn/1641447470/180/23591251173452";
                child.images.put("icon", image);

                item.items.add(child);
            }
            //pptv
            {
                DisplayItem child = new DisplayItem();
                child.title = "大乐透";
                child.target = new DisplayItem.Target();
                child.target.entity = Entity_Intent;
                child.target.params.put("android_action", Intent.ACTION_VIEW);
                child.target.url    = "http://cp.mi.com";
                child.images = new ImageGroup();
                Image image = new Image();
                image.url = "http://photocdn.sohu.com/20120119/Img332675933.jpg";
                //image.url = "http://pic.baike.soso.com/p/20100629/bki-20100629110833-109286196.jpg";
                child.images.put("icon", image);

                item.items.add(child);
            }
            //iqiyi
            {
                DisplayItem child = new DisplayItem();
                child.title = "竞彩足球";
                child.target = new DisplayItem.Target();
                child.target.entity = Entity_Intent;
                child.target.params.put("android_action", Intent.ACTION_VIEW);
                child.target.url    = "http://cp.mi.com";
                child.images = new ImageGroup();
                Image image = new Image();
                image.url = "http://img4.imgtn.bdimg.com/it/u=4131549034,2694083374&fm=11&gp=0.jpg";
                child.images.put("icon", image);

                item.items.add(child);
            }
            //fengxing
            {
                DisplayItem child = new DisplayItem();
                child.title = "竞彩篮球";
                child.target = new DisplayItem.Target();
                child.target.entity = Entity_Intent;
                child.target.params.put("android_action", Intent.ACTION_VIEW);
                child.target.url    = "http://cp.mi.com";
                child.images = new ImageGroup();
                Image image = new Image();
                image.url = "http://www.v2gg.com/uploads/allimg/140419/0949131193-0.jpg";
                child.images.put("icon", image);

                item.items.add(child);
            }
            //phenix
            {
                DisplayItem child = new DisplayItem();
                child.title = "11选5";
                child.target = new DisplayItem.Target();
                child.target.entity = Entity_Intent;
                child.target.params.put("android_action", Intent.ACTION_VIEW);
                child.target.url    = "http://cp.mi.com";
                child.images = new ImageGroup();
                Image image = new Image();
                image.url = "http://img26.nipic.com/20110803/457269_094456472149_1.jpg";
                child.images.put("icon", image);

                item.items.add(child);
            }
            {
                DisplayItem child = new DisplayItem();
                child.title = "更多推荐";
                child.target = new DisplayItem.Target();
                child.target.entity = Entity_Intent;
                child.target.params.put("android_action", Intent.ACTION_VIEW);
                child.target.url    = "http://cp.mi.com";
                child.images = new ImageGroup();
                Image image = new Image();
                image.url = "http://pic.paopaoche.net/up/2014-4/201442594839.png";
                child.images.put("icon", image);

                item.items.add(child);
            }

            return item;
        }


        public static Block<DisplayItem> createMovieSinglePosterBlock() {
            Block<DisplayItem> item = new Block<DisplayItem>();
            item.title = "Sale Off 50%";
            item.ui_type = new DisplayItem.UI();
            item.ui_type.put("id" , String.valueOf(LayoutConstant.linearlayout_single_poster));
            item.target = new DisplayItem.Target();
            item.target.entity = Entity_Intent;
            item.target.url    = "o2o://movies/movie?id=742&from=video";
            item.target.params.put("android_component", "com.xiaomi.o2o");
            //item.target.params.android_activty   = "com.xiaomi.o2o.activity.O2OTabActivity";

            item.images = new ImageGroup();
            Image image = new Image();
            image.url = "http://t12.baidu.com/it/u=4075131680,1827553139&fm=58";
            image.url = "http://img.2258.com/d/file/yule/mingxing/tuwen/2015-01-21/41c36e08a00b46473f33e62dc91f1bcd.jpg";
            item.images.put("poster", image);
            item.hint = new DisplayItem.Hint();
            item.hint.put("center", "狼图腾(选座): 3折");
            return item;
        }
        public static Block<DisplayItem> createMovieBlock(){
            Block<DisplayItem> item = new Block<DisplayItem>();
            item.title = "";
            item.ui_type = new DisplayItem.UI();
            item.ui_type.put("id" , String.valueOf(LayoutConstant.grid_media_port));
            item.ui_type.put("row_count" , "3");

            item.items = new ArrayList<DisplayItem>();
            //sohu
            {
                DisplayItem child = new DisplayItem();
                child.title = "狼图腾";
                child.target = new DisplayItem.Target();
                child.target.entity = Entity_Intent;
                child.target.url    = "o2o://movies/movie?id=742&from=video";
                child.target.params.put("android_component", "com.xiaomi.o2o");
                //child.target.params.android_activty   = "com.xiaomi.o2o.activity.O2OTabActivity";
                child.images = new ImageGroup();
                Image image = new Image();
                image.url = "http://t2.baidu.com/it/u=2411093926,2475191373&fm=20";
                child.images.put("poster", image);
                child.hint = new DisplayItem.Hint();
                child.hint.put("center", "3 折");

                item.items.add(child);
            }
            //pptv
            {
                DisplayItem child = new DisplayItem();
                child.title = "天将雄师 国语 2015";
                child.target = new DisplayItem.Target();
                child.target.entity = Entity_Intent;
                child.target.url    = "o2o://movies/movie?id=806&from=video";
                child.target.params.put("android_component", "com.xiaomi.o2o");
                //child.target.params.android_activty   = "com.xiaomi.o2o.activity.O2OTabActivity";
                child.images = new ImageGroup();
                Image image = new Image();
                image.url = "http://t2.baidu.com/it/u=753869764,2801031141&fm=20";
                child.images.put("poster", image);
                child.hint = new DisplayItem.Hint();
                child.hint.put("center", "9.9元");

                item.items.add(child);
            }
            //iqiyi
            {
                DisplayItem child = new DisplayItem();
                child.title = "澳门风云2";
                child.target = new DisplayItem.Target();
                child.target.entity = Entity_Intent;
                child.target.url    = "o2o://movies/movie?id=808&from=video";
                child.target.params.put("android_component", "com.xiaomi.o2o");
                //child.target.params.android_activty   = "com.xiaomi.o2o.activity.O2OTabActivity";
                child.images = new ImageGroup();
                Image image = new Image();
                image.url = "http://t2.baidu.com/it/u=514158127,293282070&fm=20";
                child.images.put("poster", image);
                child.hint = new DisplayItem.Hint();
                child.hint.put("center", "3D 9.9元");

                item.items.add(child);
            }

            return item;
        }

        public static Block<DisplayItem> createAnotherMoviesBlock(int row){
            Block<DisplayItem> item = new Block<DisplayItem>();
            item.title = "";
            item.ui_type = new DisplayItem.UI();
            item.ui_type.put("id" , String.valueOf(LayoutConstant.grid_media_port));
            item.ui_type.put("row_count", String.valueOf(row));

            item.items = new ArrayList<DisplayItem>();
            //sohu
            {
                DisplayItem child = new DisplayItem();
                child.title = "狼图腾";
                child.sub_title= "<font color='#f95f22'>50</font> 起";
                child.target = new DisplayItem.Target();
                child.target.entity = Entity_Intent;
                child.target.url    = "o2o://movies/movie?id=742&from=video";
                child.target.params.put("android_component", "com.xiaomi.o2o");
                //child.target.params.android_activty   = "com.xiaomi.o2o.activity.O2OTabActivity";
                child.images = new ImageGroup();
                Image image = new Image();
                image.url = "http://t2.baidu.com/it/u=2411093926,2475191373&fm=20";
                child.images.put("poster", image);
                child.hint = new DisplayItem.Hint();
                child.hint.put("center", "3 折");

                item.items.add(child);
            }
            //pptv
            {
                DisplayItem child = new DisplayItem();
                child.title = "天将雄师 国语 2015";
                child.sub_title= "<font color='#f95f22'>40</font> 起";
                child.target = new DisplayItem.Target();
                child.target.entity = Entity_Intent;
                child.target.url    = "o2o://movies/movie?id=806&from=video";
                child.target.params.put("android_component", "com.xiaomi.o2o");
                //child.target.params.android_activty   = "com.xiaomi.o2o.activity.O2OTabActivity";
                child.images = new ImageGroup();
                Image image = new Image();
                image.url = "http://t2.baidu.com/it/u=753869764,2801031141&fm=20";
                child.images.put("poster", image);
                child.hint = new DisplayItem.Hint();
                child.hint.put("center", "9.9元");

                item.items.add(child);
            }
            //iqiyi
            {
                DisplayItem child = new DisplayItem();
                child.title = "澳门风云2";
                child.sub_title= "<font color='#f95f22'>30</font> 起";
                child.target = new DisplayItem.Target();
                child.target.entity = Entity_Intent;
                child.target.url    = "o2o://movies/movie?id=808&from=video";
                child.target.params.put("android_component", "com.xiaomi.o2o");
                //child.target.params.android_activty   = "com.xiaomi.o2o.activity.O2OTabActivity";
                child.images = new ImageGroup();
                Image image = new Image();
                image.url = "http://t2.baidu.com/it/u=514158127,293282070&fm=20";
                child.images.put("poster", image);
                child.hint = new DisplayItem.Hint();
                child.hint.put("center", "3D 9.9元");

                item.items.add(child);
            }

            return item;
        }

        public static Block<DisplayItem> createShowBlock(int row){
            Block<DisplayItem> item = new Block<DisplayItem>();
            item.title = "";
            item.ui_type = new DisplayItem.UI();
            item.ui_type.put("id" , String.valueOf(LayoutConstant.grid_media_land));
            item.ui_type.put("row_count", String.valueOf(row));
            item.items = new ArrayList<DisplayItem>();
            {
                DisplayItem child = new DisplayItem();
                child.title = "呱呱主播";
                child.sub_title= "主播大厅";
                child.target = new DisplayItem.Target();
                child.target.entity = Entity_Intent;
                child.target.url    = "guagua://show/home";
                child.target.params.put("android_component", "com.guagua.miguaguasdk");
                child.target.params.put(DisplayItem.Target.Params.android_action, "com.guagua.miguaguasdk");
                child.target.params.put(DisplayItem.Target.Params.apk_url,        "https://github.com/AiAndroid/mobilevideo/raw/master/out/production/video/guagua_sdk.apk");
                child.images = new ImageGroup();
                Image image = new Image();
                image.url = "http://a1.att.hudong.com/51/91/01300000254155125691911921623.jpg";
                child.images.put("poster", image);
                item.items.add(child);
            }
            //iqiyi
            {
                DisplayItem child = new DisplayItem();
                child.title = "呱呱主播";
                child.sub_title= "凤姐来啦";
                child.target = new DisplayItem.Target();
                child.target.entity = Entity_Intent;
                child.target.url    = "guagua://show/room?roomid=178888&roomname=¡¶±ŸÉ«ÓéÀÖ¡·&roomface=http://room.img.guagua.cn/roompage/0/0/150/220000.jpg";
                child.target.params.put("android_component", "com.guagua.miguaguasdk");
                child.target.params.put(DisplayItem.Target.Params.android_action, "com.guagua.miguaguasdk");
                child.target.params.put(DisplayItem.Target.Params.apk_url,        "https://github.com/AiAndroid/mobilevideo/raw/master/out/production/video/guagua_sdk.apk");
                child.images = new ImageGroup();
                Image image = new Image();
                image.url = "http://photo.scol.com.cn/mm/img/attachement/jpg/site2/20141015/00219b7b124615a817152e.jpg";
                child.images.put("poster", image);
                item.items.add(child);
            }

            return item;
        }
    }
}
