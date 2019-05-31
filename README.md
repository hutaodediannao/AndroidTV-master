# 欢迎使用 TvRecyclerView [ ![Download](https://api.bintray.com/packages/zhousuqiang/maven/tv-recyclerview/images/download.svg) ](https://bintray.com/zhousuqiang/maven/tv-recyclerview/_latestVersion)

>* 针对TV端的特性对RecyclerView进行了适配与开发;
>* 优化[TwoWayView](https://github.com/lucasr/twoway-view)在TV端的适配和延伸;
>* 支持v7包原生的LayoutManager;
>* 支持阿里[vlayout](https://github.com/alibaba/vlayout)的布局及其它第三方LayoutManager;

### 效果

![](https://github.com/zhousuqiang/TvRecyclerView/blob/master/images/all_4.png)

![](https://github.com/zhousuqiang/TvRecyclerView/blob/master/images/img_all.gif)

### Android Studio 集成

```java
// 2.0+版本的引入
implementation 'com.owen:tv-recyclerview:2.0.3'

// 1.0版本的引入(已过时版本)
implementation 'com.tv.boost:tv-recyclerview:1.1.0'
```

### 自定义属性
| 属性      |  值/类型  |  简介  |
| -------- | :-----: | :---- |
| tv_layoutManager     | string |   指定LayoutManager     |
| tv_selectedItemOffsetStart | dimension | 选中的item距离开始(上/左)的偏移量, 与tv_selectedItemIsCentered二选一 |
| tv_selectedItemOffsetEnd | dimension | 选中的item距离结尾(右/下)的偏移量, 与tv_selectedItemIsCentered二选一 |
| tv_selectedItemIsCentered | boolean | 选中居中, 与上面的偏移量二选一 |
| tv_isMenu | boolean | 是否为菜单模式 |
| tv_isMemoryFocus | boolean | 是否记忆焦点 |
| tv_loadMoreBeforehandCount | int | 提前多少个开始加载更多 |
| tv_optimizeLayout | boolean | 布局优化, 常用的LayoutManager作用不是很大,但对MetroGridLayoutManager这种计算量大的布局来说有一定的提升 |
| tv_verticalSpacingWithMargins | dimension | 设置布局item间的竖向间距 |
| tv_horizontalSpacingWithMargins | dimension | 设置布局item间的横向间距 |
|  |  |  |
| 自定义LayoutManager属性 |  |  |
| tv_numColumns | int | 列数, GridLayoutManager及子类所拥有 |
| tv_numRows | int | 行数, GridLayoutManager及子类所拥有 |
| tv_laneCountsStr | string | 每块区域的列数, MetroGridLayoutManager所拥有, 格式 如:24,60,10 |
| tv_isIntelligentScroll | boolean | 根据区域智能滚动, MetroGridLayoutManager所拥有 |


### 特性

- [x] 支持快速移动焦点不丢失;

- [x] 支持Item选中放大不叠压;

- [x] 多种选中和滚动方式:
    ```java
    //选中指定项
    setSelection(int psotion);

    //选中指定项(平滑的滚动方式)
    setSelectionWithSmooth(int position)

    //滚动到指定位置, 可以指定便宜量, 可以指定是否获取焦点
    scrollToPositionWithOffset(int position, int offset, boolean isRequestFocus)

    //平滑的滚动到指定位置, 可以指定便宜量, 可以指定是否获取焦点
    smoothScrollToPositionWithOffset(int position, int offset, boolean isRequestFocus)
    ```
- [x] 监听回调
    ```java
    //item选中、点击监听
    mRecyclerView.setOnItemListener(new TvRecyclerView.OnItemListener() {
        @Override
        public void onItemPreSelected(TvRecyclerView parent, View itemView, int position) {
                //上次选中
        }

        @Override
        public void onItemSelected(TvRecyclerView parent, View itemView, int position) {
                //当前选中
        }

        @Override
        public void onItemClick(TvRecyclerView parent, View itemView, int position) {
                //点击
        }
    });
    
    //焦点移动边界监听
    mRecyclerView.setOnInBorderKeyEventListener(new TvRecyclerView.OnInBorderKeyEventListener() {
                @Override
                public boolean onInBorderKeyEvent(int direction, View focused) {
                    switch (direction) {
                        case View.FOCUS_DOWN:

                            break;
                        case View.FOCUS_UP:

                            break;
                        case View.FOCUS_LEFT:

                            break;
                        case View.FOCUS_RIGHT:

                            break;
                    }
                    //返回true时,事件将会被拦截由你来控制焦点
                    return false;
                }
            });
    
    //加载更多监听
    mRecyclerView.setOnLoadMoreListener(new TvRecyclerView.OnLoadMoreListener() {
                @Override
                public boolean onLoadMore() {
                    mRecyclerView.setLoadingMore(true); //正在加载数据
                    mLayoutAdapter.appendDatas(); //加载数据
                    mRecyclerView.setLoadingMore(false); //加载数据完毕
                    return true; //是否还有更多数据
                }
            });
           
    ```

### 更详细的使用请见exmaple

------

扣 扣 群：484790001（注：加群时请写明来源及目的）

![](https://github.com/zhousuqiang/TvRecyclerView/blob/master/images/qq.png)

### 其它作品

焦点框框架[TvFocusBorder](https://github.com/zhousuqiang/TvFocusBorder)

Tab框架[TvTabLayout](https://github.com/zhousuqiang/TvTabLayout)

Tv控件[TvWidget](https://github.com/zhousuqiang/TvWidget)

作者 [owen](https://github.com/zhousuqiang)
