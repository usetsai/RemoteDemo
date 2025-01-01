# RemoteDemo


内存占用情况
             3种都加载     SimpleView  SimpleRecyclerView SimpleWebView  都不加载
Java         10.4          10.4        10.4               10.4           10.7
Native       13.2          14.2        14                 14.1           8.3
Graphics     58.7          41.1        41.4               41.1           41.1
Stack        796           816         844                848            936
Code         15.5          15.5        15.6               15.5           15.6
Other        13            13.2        13                 13             13.2
TOTAL        111.7         95.4        95.4               95.1           89.8

1. SurfaceView调用setZOrderOnTop 可以使用SurfaceControlViewHost支持触摸事件传递，应用可以直接使用onClickListener和Touch事件。
setZOrderOnTop会把事件直接抢占。也可以使用SurfaceView监听Touch事件，通过binder传递Touch事件。
调用setZOrderOnTop 可以使用EditText 弹出输入法，但是不会像 Activity 弹出输入法后，向上滑动不被输入法挡住。不设置setZOrderOnTop 无法弹出输入法。

2. SurfaceView无法立即加载View

3. 增加内存大小主要Graphics图形内存大小，应该是SurfaceView加载的图形缓存，加载了三个SurfaceView大概增加17m左右