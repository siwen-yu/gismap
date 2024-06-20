#  GIS打点工具

## 打点功能
 
技术框架：vue + iview + SpringBoot

地图厂商：高德地图

主要功能：打印海量点，打印轮廓，打印折线，打印热力图，坐标拾取，距离测量，轮廓编辑等

### 使用步骤

1. 点击开始`打点按钮`或者右键地图任意位置选择`开始打点`

2. 弹框中选择数据类型：`字符串`，`文件`

3. 弹框中选择相应的数据内容：`字符串-直接输入`，`文件-上传文件`

4. 弹框中选择打点类型：`海量点`，`轮廓`，`折线`，`热力图`

5. 注意数据内容的格式，详细参见`数据示例`

6. 选择数据内容`经纬度坐标系`和`格式`(整型=标准经纬度*10^7)。

7. 按`确定`按钮开始打点


### 打印海量点
> 建议打印10万以内的坐标点

### 打印轮廓/折线
> 建议打印1000个以内的轮廓（主要取决于轮廓内点的个数）

### 打印热力图

> 建议打印1万以内的坐标点(有待考量)


## 其它功能 

### 坐标拾取 

1. 地图任意区域`双击左键`或右键菜单选择`标记位置`
2. 查看相应位置显示的经纬度坐标(wgs84)

### 清除打印 

1. hover界面左上`清除按钮`，选择需要清除的图层类型，或者右键地图，选择需要清除的图层类型（如果界面图层过多，清除时可能存在卡顿，请耐心等待）

### 地图测距
 
1. 在地图任意位置点击右键，并选择`地图测距`，进入地图测距模式。
2. `单击左键`选取多个点，查看距离，双击左键退出测距模式。

## docker安装

```shell
docker-compose up -d
```

## 效果图
![img.png](imgs/img.png)
#### 海量点
![img.png](imgs/points.png)
#### 轮廓、折线
![img.png](imgs/polygon.png)
#### 热力图
![img_1.png](imgs/heatmap.png)
#### 坐标拾取 
![img.png](imgs/pick.png)
#### 轮廓编辑
![img.png](imgs/edit.png)