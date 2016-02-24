# 数据结构
<img src="https://raw.githubusercontent.com/AiAndroid/tvhome/master/tvhome/design/TV01.jpg"/></br>
<img src="https://raw.githubusercontent.com/AiAndroid/tvhome/master/tvhome/design/TV02.jpg"/></br>

数据以块为单位组织

                             Block
                               |
             __________________|___________________
             |    ...  |           |       ...    |
           Block      Block    Display Item     Display Item
           
           

#  展示块
<img src="https://raw.githubusercontent.com/AiAndroid/tvhome/master/tvhome/design/block.png"/></br>


#  展示单元定义 DisplayItem
展示数据和业务无关，展示数据能展示，视频，游戏，应用，广告</br>
满足以下条件：</br>
1. 展示文本，包括title, sub_title, description, hint(left, center, right), 角标，</br>
2. 展示图片，包括图标，背景，动画，海报</br>
3. 点击跳转，数据可以得到最终跳转的Intent，URI，包括为跳转准备应用下载，安装</br>
4. 商业化打点，包括公司的Post打点和第三方的GET打点</br>

 "长视频点击": {
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
            }, 


