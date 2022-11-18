## AppinfoUtils的使用文档

AppinfoUtils：是手机中的应用信息的一些基础工具类，里面的方法主要是获取一些手机应用的信息

### getVersionName(context:Context?) : String

> 获取当前APP的Version Name

### getVersionCode(context:Context):Long

> 获取当前APP的版本号

### isAppAvailable(context:Context,packageName:String): Boolean

> 判断当前是否已经安装了某个APP

参数：

* packageName:应用的包名

返回值：

* boolean: true-应用已安装  false-应用未安装



