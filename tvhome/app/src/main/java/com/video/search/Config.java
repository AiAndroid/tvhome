package com.video.search;


public class Config {
    public static final boolean DEBUG = true;
    public static final boolean DEBUG_ASSISTANT_INFO = false;

    public static final String HOST_GITV = "http://media.ptmi.gitv.tv";
    public static final String HOST_GITV_TEST = "http://10.99.168.139:9101";
    public static final String HOST_ICNTV = "http://media.v2.t001.ottcn.com";

    public static boolean TEST_SERVER = false;
    public static String HOST = "http://10.99.168.139:9101"; // 默认使用的HOST

    public static final String URL_EPG_INDEX = "/v2/tv/bolt/aio/index";
    public static final String URL_EPG_LIST = "/v2/tv/bolt/aio/data";//获取展开的aio epg数据
    public static final String URL_MEDIA_DETAIL = "/tvservice/getmediadetail2";//获取某个节目的全部信息
    public static final String URL_GET_MEDIA_URL = "/tvservice/getmediaurl"; // 获取节目某一集的播放地址
    public static final String URL_FILTER_LIST = "/tvservice/filtermediainfo";//根据过滤条件获取影片列表
    public static final String URL_RELATIVE_MEDIA_LIST = "/tvservice/getrelativemedia";//获取相关推荐media list
    public static final String URL_ALBUM_LIST = "/tvservice/getmilistinfo";//根据米列／专题id获取米列／专题数据
    public static final String URL_CHANNEL_COLUMNS = "/tvservice/getchannelcolumns";// 获取某channel的所有栏目信息
    public static final String URL_CHANNEL_COL_MORE = "/tvservice/getcolumnmore";// 获取某个栏目的全部信息
    public static final String URL_RANK_LIST = "/v2/tvservice/getrankdata";// 根据排行榜id获取排行榜数据
    public static final String URL_GET_CINEASTE = "/tvservice/getcineaste";// 根据影人id获取其作品数据
    public static final String URL_REMOTE_ICON = "/tvservice/geticondata"; // 获取服务器端准备的icon
    public static final String URL_MEDIA_BATCH = "/tvservice/getmediabatch"; // 批量根据节目id获取节目信息
    public static final String URL_BUNDLE_VIP_DATA = "/tvservice/getbundledata"; // 获取捆绑会员包信息
    public static final String URL_SPECIAL_ZONE_LIST = "/tvservice/getboxspecialsubjectlist";// 专区集合
    public static final String URL_GET_APK_DATA = "/tvservice/getapkdata";
    public static final String URL_ID_MAP = "/tvservice/idmap";

    public static final String URL_SEARCH_MEDIA = "/tvservice/searchmediav2"; // 首字母搜索接口
    public static final String URL_SEARCH_SUGGEST = "/tvservice/suggest"; // 关键词搜索接口
    public static final String URL_SEARCH_CINEASTE = "/tvservice/searchcineaste"; // 影人搜索接口
    public static final String URL_GET_HOT_SEARCH = "/tvservice/gethotsearchmedias"; // 搜索排行榜
    public static final String URL_SEARCH_RECOMMENDATIONS = "/tvservice/getsearchrecommendations"; // 搜索推荐
    public static final String URL_SEARCH_HINT = "/tvservice/getsearchhints"; // 搜索提示接口
    public static final String URL_UPDATE = "/tvservice/getupgradeinfo";
    public static final String URL_GETSIRISEARCHMEDIA = "/tvservice/getsirisearchmedias";
    public static final String URL_PREPARE_DESKTOP_DATA = "/tv/bolt/desktop/data";// 为桌面程序准备入口数据
    public static final String URL_GET_MITV_NEWS = "/tvservice/gettopnews";// 视频新闻数据



    public static final String URL_USER_LOGIN = "/tvservice/login";
    public static final String URL_USER_BIND = "/security/bind";
    public static final String URL_USER_GETBOOKMARK = "/tvservice/getbookmark";
    public static final String URL_USER_SETBOOKMARK = "/tvservice/setbookmark";
    public static final String URL_USER_DELBOOKMARK = "/tvservice/deletebookmark";
    public static final String URL_USER_CHASEMEDIA = "/tvservice/getchasenewmedialist";
    public static final String URL_USER_HISTORY = "/tvservice/getplayhistory";
    public static final String URL_USER_SETHISTORY = "/tvservice/setplayhistory";
    public static final String URL_USER_DELHISTORY = "/tvservice/deleteplayhistory";


    public static void updateHost(String host) {
        HOST = host;
    }

}
