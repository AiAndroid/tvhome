# 数据结构

数据以块为单位组织

                             Block
                               |
             __________________|___________________
             |    ...  |           |       ...    |
           Block      Block    Display Item     Display Item
           
           

#  展示块



#  展示单元定义 DisplayItem
展示数据和业务无关，展示数据能展示，视频，游戏，应用，广告
满足以下条件：
1. 展示文本，包括title, sub_title, description, hint(left, center, right), 角标，
2. 展示图片，包括图标，背景，动画，海报
3. 点击跳转，数据可以得到最终跳转的Intent，URI，包括为跳转准备应用下载，安装
4. 商业化打点，包括公司的Post打点和第三方的GET打点

