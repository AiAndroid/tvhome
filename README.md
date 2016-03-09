核心逻辑是展示与业务的分离，增加运营的灵活性

# 数据结构
<img src="https://raw.githubusercontent.com/AiAndroid/tvhome/master/tvhome/design/TV01.jpg"/>
<img src="https://raw.githubusercontent.com/AiAndroid/tvhome/master/tvhome/design/TV02.jpg"/></br>

数据以块为单位组织

                             Block
                               |
             __________________|___________________
             |    ...  |           |       ...    |
           Block      Block    Display Item     Display Item
           
           

#  展示块
<img src="https://raw.githubusercontent.com/AiAndroid/tvhome/master/tvhome/design/block.png"/></br>

UI 组织和展示块一一对应</br>
```
  block首页
    block(推荐)    block(影视大全)    block(游戏应用)   block(个人中心)   block(设置)
    block轮播图
       电视剧
       电影
       ...
       
    block 电影
        电影入口
        电影单元
        ...
```

#  展示单元定义 DisplayItem
展示数据和业务无关，展示数据能展示，视频，游戏，应用，广告</br>
满足以下条件：</br>
1. 展示文本，包括title, sub_title, description, hint(left, center, right), 角标，</br>
2. 展示图片，包括图标，背景，动画，海报</br>
3. 点击跳转，数据可以得到最终跳转的Intent，URI，包括为跳转准备应用下载，安装</br>
4. 商业化打点，包括公司的Post打点和第三方的GET打点</br>

 ##"长视频点击"
 <img src="https://raw.githubusercontent.com/AiAndroid/tvhome/master/tvhome/design/pvideo.png"/></br>
 ```json
 {
    "target": {
        "url": "o/V000913653",
        "params": {
            "entity": "pvideo"
        },
        "entity": "pvideo"
    },
    "title": "秦时明月",
    "hint": {
        "left": "58 集全"
    },
    "sub_title": "羽练夫妇甜蜜大升级",
    "times": {
        "updated": 1454513273,
        "created": 1430924462
    },
    "images": {
        "poster": {
            "url": "http://image.box.xiaomi.com/mfsv2/download/fdsc3/p01UCPBoFQBc/ZonxJhukRlUsWy.jpg"
        },
        "icon": {
            "url": "http://image.box.xiaomi.com/mfsv2/download/s010/p01EI5zF4ESu/3G48UmxYSnr0Yj.jpg"
        }
    },
    "ns": "video",
    "id": "V000913653"
}
```
#API定义
尽量少的API，API尽量服务器用数据的方式返回，客户端简单拼装出来，而不是hard code

#点击跳转
支持android的一切跳转，形式最好是deep link模式。

长视频
```json
"target": {
  "url": "o/V001059222?position=hot.r%7C9.0%7CRichEntrance.TabImageGrid.O",
  "params": {
    "entity": "pvideo"
  },
  "entity": "pvideo"
}
```
应用跳转,需要android component, intent构建单元
```json
"target": {
  "url": "",
  "params": {
    "apk_url": "http://m.app.mi.com/download/2032",
    "android_mime": "",
    "app_name": "艺龙旅行-订酒店",
    "appstore_h5_url": "",
    "android_extra": "",
    "apk_version": "",
    "new_task": false,
    "miui_ads": "ads://video?mimarket=0&payload=usiLOSvtw4pYVjgtU6qYoVsEb6ZVCJwm8zhGDsGvzIhtwSTdiWT229iGicHHpddr_ma4ers-JwsbYS4vzkoPmgy13iIHdtfAirW6A3N3oZmyfcDqovbtFzLHZDtLDL3fYRhSleY51LerPc_BKdFJMBPLLGP4psIjTTXjdzXup9TFD-dmIJyXgAd7KG58CfeCMe91uMZVw6EiW0m09-ZA3KsBBaMxY6cacl4DJ1aLgENpbaUx20iP8oO4_JVGMwpypuYYLHl074KSF9gssLOsZD-682Ro-ba8nqlonJyfmnReuzwIuD19bv5QDtzrrJKTqdZtuQTs5bqpikHtN4e6CKNpk4zErPV8Nd4cxvE0ZpNbWpej8R-EogPwIClISE7sMS_GHTew0cErUJjnJsuoQUt4_tjUcPhQCOxrrRoUoEydFcGVISP2FVSy9z-JQm1TpKYTKVjqoFbvNokMgi4l5O-F0rgjTROGHpnfrJ4BmLHF-DPqrBViUvDezfqE36D560v4RV1mDhyyReqYuTPKRg",
    "android_action": "",
    "android_activity": "",
    "android_component": "com.dp.android.elong",
    "app_icon": ""
  },
  "entity": "intent"
}
```
集合跳转，没有指定的目标跳转，默认为集合跳转
```json
"target": {
  "url": "c/tv?position=hot.r%7C2.0%7CButtons.Link",
  "params": {
  }
}
```
#商业化
前贴片由播放器实现，方案与手机视频基本相同。
```
"target": {
    "url": "yymobile://3g.yy.com:80?bundle_extra_type=0",
    "params": {
        "apk_url": "http://image.box.xiaomi.com/.../pFU92B.apk",
        ...
        "apk_version": "26",
        "present_url": "",
        "new_task": false,
        "action_url": "",
        "tick_url": "",
        "android_action": "com.duowan.mobile.mi.entry",
        "android_component": "com.duowan.mobile.mi"
    },
    "entity": "intent"
}
present_url 是展示打点需求,可以是多个,能够集成第三方的打点需求,如秒钟android_component是应用的包名,如果应用已经存在系统中,会接打开应用,如果不存在,会下载对于的应用。
```
具体例子
```js
{
  "ui_type": {
    "ratio": ​0.5,
    "name": "linearlayout_single_poster_out",
    "id": ​257
  },
  "target": {
    "url": "http://e.cn.miaozhen.com/r/k%3D2018069%26p%3D6zOMb%26ro%3Dsm%26dx%3D0%26rt%3D2%26ns%3D__IP__%26ni%3D__IESID__%26v%3D__LOC__%26nd%3D__DRA__%26np%3D__POS__%26nn%3D__APP__%26vo%3D3f965d0f3%26vr%3D2%26o%3Dhttp%253A%252F%252Fproall.h5bang.com%252Fcampaign%252Fdefault%252Fxuetl%252Fxtlqlb%252Findex.php%253Ff%253D2",
    "params": {
      "apk_url": "",
      "android_mime": "",
      "android_extra": "",
      "present_url": "http://g.cn.miaozhen.com/x/k=2018069&p=6zOMb&dx=0&rt=2&o=",
      "android_activity": "",
      "apk_version": "",
      "new_task": false,
      "miui_ads": "ads://video?mimarket=0&payload=usiLOSvtw4pYVjgtU6qYoZtNTK2pJf3J8zhGDsGvzIhtwSTdiWT229iGicHHpddr0M-nGn0cwjolrHyDGoZEq417TQKj4SsvnuP9cu70oxc-dWwWdpFS4n7lEVJU880UYNWbWYu4CBnAmX3yme7MpMt59cLYk_sXRgrDW7MRSgw3rDzz4x0yOf1DLI8R3vuk1TJjIuBgXinNjwgwqqpzow4zNcTlxtzvIgsMgSW93WSLznwNzMt53WiHIdpPD3m3GPS_c13ESOllwY-ovt-Q747Nfa4vv0_LzwHDWKSE35uSYq1xY-i5PUWlBeEwZd_trjQfHJY7zgXrmf9lcT7O4TEvxh03sNHBK1CY5ybLqEFLeP7Y1HD4UAjsa60aFKBMcQDexL0oF0uJL4phK3J9-ae6aRX9t8SS",
      "present_url_1": "http://mvideo.duokanbox.com/api/a1/count/ad?p=g3H7tfEXS5lwO7onq0Y78S0lOhR1Ium7Qf1V5e6Go3_D0F71rZX_Srg28BdmwuMCSZtpcxTE7ZCqJ6UYPvd0nI8U31ksu7ZNyYNf86gAV_Z1g5QPWpy3RotMck-bB0oe",
      "android_action": "",
      "android_component": ""
    },
    "entity": "intent"
  },
  "title": "",
  "hint": {
    
  },
  "sub_title": "",
  "times": {
    "updated": ​0,
    "created": ​0
  },
  "images": {
    "poster": {
      "url": "http://file.market.xiaomi.com/download/AdCenter/033f4529d52f63cfea04abef5dcc907532d43fcde/AdCenter033f4529d52f63cfea04abef5dcc907532d43fcde.jpg/AdCenter033f4529d52f63cfea04abef5dcc907532d43fcdeAdCenter033f4529d52f63cfea04abef5dcc907532d43fcde.jpg.jpg"
    }
  },
  "ns": "video",
  "id": "http://e.cn.miaozhen.com/r/k=2018069&p=6zOMb&ro=sm&dx=0&rt=2&ns=__IP__&ni=__IESID__&v=__LOC__&nd=__DRA__&np=__POS__&nn=__APP__&vo=3f965d0f3&vr=2&o=http%3A%2F%2Fproall.h5bang.com%2Fcampaign%2Fdefault%2Fxuetl%2Fxtlqlb%2Findex.php%3Ff%3D2"
}
```

#播放器插件化
聚合，接耦，独立自升级
<img src="https://raw.githubusercontent.com/AiAndroid/tvhome/master/tvhome/design/vp.png"/></br>

#应用跳转
```
{
  "target": {
    "url": "o/V001118597?position=hot.r%7C1.0%7CCarousel.O",
    "params": {
      "entity": "pvideo",
      "schema": "mivideo"
    },
    "entity": "pvideo"
  },
  ...
  "ns": "video",
  "id": "V001118597",
  "settings":{} //这是一个k-v 参数会传递到到达的目的地
}

#
schema://ns/entity?id=id


schema:
对于video, app, game, music,emarket,player会定义缺省的值
video： mitvvideo
app: :  mitvapp
game :  mitvgame
music:  mitvmusic
emarket:mitvemarket

entity：
这个跳转的对应位置，一般为定义的path

ns定义的应用Host

各个应用自己实现自己的API,以在线视频为例子
详情页：
mitvvideo://video/detail?id=视频id&key=value

集合页面
mitvvideo://video/album?id=视频id&key=value

第三方跳转：follow原来的小米视频逻辑
```

#参考文档
<a href="https://raw.githubusercontent.com/AiAndroid/tvhome/master/tvhome/design/MIUI_Video_Scrum.pdf">
揭秘MIUI视频从0到千万用户的研发迭代</a>
Facebook Stream,
Twitter tweets,
MI Mobile Video API,
