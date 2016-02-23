package com.tv.ui.metro.model;

/**
 * Created by liuhuadong on 11/17/14.
 */
public class LayoutConstant {

    public final static int single_view = 100;//-------plain view
    public final static int imageswitcher = 101;//-------广告可以-------------给广告切换image
    public final static int linearlayout_top = 201;//-------单行button----icon---图标在文本上边
    public final static int linearlayout_top_circle = 295; // -------单行button----圆形icon ---图标在文本上边
    public final static int linearlayout_left = 202;//-------单行Button----icon---图标在文本左边
    public final static int linearlayout_poster = 203;//-------单行Button----缺省,一行多个，以海报形式展示,电视剧：“韩剧专场”
    public final static int linearlayout_none = 204;//-------单行Button----文本， 缺省
    public final static int linearlayout_land = 205;//-------单行海报---一行多个，以海报形式展示,电影：“韩剧专场”
    public final static int linearlayout_title = 206;//-------标题
    public final static int linearlayout_single_poster     = 207;//-------单行海报 inside
    public final static int linearlayout_single_poster_out = 257;//-------单行海报,outside
    public final static int linearlayout_filter = 208;//-------过滤
    public final static int linearlayout_filter_item = 298;//-------过滤
    public final static int linearlayout_episode = 209;//-------剧集, 默认button风格
    public final static int linearlayout_episode_item = 299;//----子剧集 外部不直接使用
    public final static int linearlayout_category_filter_item = 296;//分类的过滤  外部不直接使用
    public final static int linearlayout_search_item = 297;//----search  外部不直接使用
    public final static int linearlayout_episode_list = 210;//-----剧集 list 风格
    public final static int linearlayout_episode_list_item = 211;//-----子剧集 外部不直接使用
    public final static int linearlayout_filter_select = 212;//过滤器UI使用
    public final static int linearlayout_filter_select_item = 213;//过滤器单个元素使用
    public final static int linearlayout_search = 214;//search UI
    public final static int linearlayout_single_desc = 215;//描述
    public final static int linearlayout_episode_select = 216;//-------episode enable select
    public final static int linearlayout_list_text = 218; // list text view
    public final static int linearlayout_search_bar = 219; //search bar for search specific keywords
    public final static int linearlayout_free_search_bar = 220; //search bar for search specific keywords
    public final static int linearlayout_top_icon = 221;   //-------单行button----自定义icon ---图标在文本上边


    public final static int block_land = 300;//------------展示块容器------------没有什么用 landscape layout
    public final static int block_port = 301;//------------展示块容器------------没有什么用 portrait layout

    public final static int tabs_horizontal = 311;//------------水平tabs------------水平展示tab的父Block
    public final static int tabs_vertical = 312;//------------垂直tabs------------垂直展示tab类型的Block，用于tablet，TV

    public final static int block_channel = 400;//------------多个频道块
    public final static int block_sub_channel = 401;//------------一个频道块

    public final static int block_tabs = 410;//------------
    public final static int grid_media_land = 411;//------------gridview------------水平展示媒体图片
    public final static int grid_media_port = 412;//------------gridview------------垂直展示媒体图片
    public final static int grid_media_land_title = 413;//------------gridview------------水平展示媒体图片,带标题
    public final static int grid_media_port_title = 414;//------------gridview------------垂直展示媒体图片，带标题

    public final static int list_media_land = 415;//------------listview------------水平展示媒体图片
    public final static int list_media_port = 416;//------------listview------------垂直展示媒体图片

    public final static int list_category_land = 421;//------------listview------------category的land layout
    public final static int list_category_port = 422;//------------listview------------category port layout
    public final static int grid_item_selection = 431;//------------grid----------------精选
    public final static int grid_block_selection = 432;//------------grid----------------精选
    public final static int list_rich_header = 441;//--------------------------------排行
    public final static int channel_list_long_hot = 451; //频道 列表 显示hot
    public final static int channel_list_long_rate = 452; //频道 列表 显示rate
    public final static int channel_list_short = 453; //频道 资讯
    public final static int channel_grid_long = 454; //favor, offline, history

    public final static int grid_small_icon      = 501; //app grid view
    public final static int list_small_icon      = 502; //app list view
    public final static int grid_small_icon_item = 511; //single app, not call from outside
    public final static int list_small_icon_item = 512; //single app, not call from outside
    public final static int grid_autospan_icon       = 601;//for live tv
    public final static int grid_autospan_icon_item  = 611;
    public final static int grid_autospan_port       = 602;//for port
    public final static int grid_autospan_land       = 603;//for land
    public final static int grid_autospan_square     = 604;//for square
    public final static int grid_middle_icon         = 503; //app grid middle size view
    public final static int grid_middle_icon_item    = 513; //app grid view

    public final static int single_poster_ad    = 10001; //ad view

    public final static int grid_circle_icon = 217;//-------actor block in mediadetailactivity
}
