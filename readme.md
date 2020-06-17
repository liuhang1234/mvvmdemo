@[TOC](MVVM使用)

## MVVM理解

MVVM 是Model（数据）、View（视图）、ViewModel（数据视图） ，是一种把UI和数据分离的一种设计模式
直接上代码


### 1.首先要启用databinding，因为我使用的是AS4.0,与之前的databinding不太一致，

```java
buildFeatures{
        dataBinding = true
	// for view binding :
	// viewBinding = true
    }
```

### 2.布局 有一个TextView 和一个RecyclerView

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">

     <data>

        <variable
            name="test"
            type="com.lh.myapplication.main.Model" />

        <variable
            name="activity"
            type="com.lh.myapplication.MainActivity" />
    </data>

	<!-- 该布局使用的布局 与关联的ViewModel name: 是在布局中使用的tag,type:是实际的ViewModel -->
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        tools:context=".MainActivity">

        <TextView
            android:id="@+id/tv_show"
            android:background="#FF0000"
            android:text="@={test.page}"
            android:textSize="20sp"
            android:textColor="#000000"
            tools:text="哈哈哈哈啊哈"
            android:onClick="@{activity::click}"
            android:layout_width="match_parent"
            android:layout_height="40dp" />

        <com.scwang.smartrefresh.layout.SmartRefreshLayout
            android:id="@+id/refreshLayout"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            app:srlEnableLoadMoreWhenContentNotFull="false"
            app:srlEnableOverScrollBounce="false"
            app:srlEnableRefresh="true">

            <androidx.recyclerview.widget.RecyclerView
                android:id="@+id/mRecyclerView"
                android:layout_width="match_parent"
                android:layout_height="wrap_content" />
        </com.scwang.smartrefresh.layout.SmartRefreshLayout>
    </LinearLayout>
</layout>
```
### 3.ViewModel

```kotlin
class MainViewModel :BaseViewModel() {

    // 获取列表的数据内容，使用LiveData来观察该数据
    var articlesData = MutableLiveData<ListDatas>()
    // TextView的数据展示
    var text = MutableLiveData<String>()
    //TextView的点击事件
    fun click(view : View){
        Log.d("click","dayin")
    }
    //获取服务器数据
    fun getListData(page:Int){
        launch({httpUtil.getArticleList(page)},
            articlesData)
    }

}
```
### 4 BaseViewModel

```kotlin
open class BaseViewModel:ViewModel() {


    val httpUtil by lazy { HttpUtils.getInstance().getApiService() }


    fun <T> launch(block: suspend CoroutineScope.() -> BaseResult<T>,
        liveData: MutableLiveData<T>){
        // kotlin 的协程
        viewModelScope.launch{
            var result: BaseResult<T>? = null
            withContext(Dispatchers.IO) {
                result = block()
            }
            if (result!!.errorCode == 0) {//请求成功
                // 把数据存至liveData的value中
                liveData.value=result!!.data
            } else {
                Log.d("error","message error")
            }
        }
    }
}
```
### 5 View中观察属性的变化，比如list数据这样的，要处理的
	
```kotlin
class MainActivity : BaseActivity<MainViewModel,ActivityMainBinding>() {
    //RV的adapter
    var adapter: MainAdapter? = null
    // RV的数据源
    var list: ArrayList<Data>? = null
    // 分页请求
    var page :Int = 0
    // 布局id
    override fun getLayoutResID(): Int = R.layout.activity_main
    override fun init() {
        list = ArrayList()
        adapter = MainAdapter()
        mVB.mRecyclerView.layoutManager = LinearLayoutManager(this)
        mVB.mRecyclerView.adapter = adapter
        adapter!!.setList(list)

        mVB.refreshLayout.setOnRefreshListener {
            page = 0
            mVM.getListData(page)
            // 给text

        }
        mVB.refreshLayout.setOnLoadMoreListener {
            page++;
            mVM.getListData(page)
        }
        mVM.model.value = Model()
        mVB.test=mVM.model.value
        mVB.activity=this
        mVB.refreshLayout.autoRefresh()
        // 不添加。不会更新UI
        mVB.lifecycleOwner=this

    }

    override fun initVM() {
        //观察ViewModel中的model数据，更新adapter 绑定数据
        mVM.articlesData.observe(this, Observer {
            mVB.refreshLayout.finishRefresh()
            mVB.refreshLayout.finishLoadMore()
            if (page == 0) {
                list!!.clear()
            }
            it.datas?.let { it1 -> list!!.addAll(it1) }
            adapter?.let {it2->
                it2.setList(list)
            }
        })
    }
	
    fun click(view : View){
        Log.d("click","print")
    }

}
```
### 6.recyclerview 配合databinding
	

```kotlin
class MainAdapter() : BaseQuickAdapter<Data, BaseDataBindingHolder<ItemArticleBinding>>(R.layout.item_article) {
    override fun convert(holder: BaseDataBindingHolder<ItemArticleBinding>, item: Data) {
        var bind = holder.dataBinding
        bind?.let {
            it.data=item
            it.executePendingBindings()
        }
    }
}
```

```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android">

    <data>
        <variable
            name="data"
            type="com.lh.myapplication.bean.Data" />
    </data>

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical">

        <RelativeLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:background="?attr/selectableItemBackground"
            android:padding="10dp">

            <ImageView
                android:id="@+id/iv_cover"
                android:layout_width="80dp"
                android:layout_height="60dp"
                android:background="#F7F7F7"
                android:scaleType="centerCrop" />

            <TextView
                android:id="@+id/tv_title"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginLeft="10dp"
                android:layout_toRightOf="@+id/iv_cover"
                android:ellipsize="end"
                android:maxLines="1"
                android:text="@{data.title}"
                android:textColor="#333333"
                android:textSize="14sp" />

            <TextView
                android:id="@+id/tv_des"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_below="@+id/tv_title"
                android:layout_marginLeft="10dp"
                android:layout_marginTop="10dp"
                android:layout_toRightOf="@+id/iv_cover"
                android:ellipsize="end"
                android:maxLines="2"
                android:text="@{data.desc}"
                android:textColor="#666666"
                android:textSize="12sp" />
        </RelativeLayout>

        <View
            android:layout_width="match_parent"
            android:layout_height="0.5dp"
            android:layout_marginLeft="100dp"
            android:background="#cccccc" />
    </LinearLayout>
</layout>

```

### 7.网路请求使用Retrofit+okhttp
	

```kotlin
class HttpUtils {
    private val mApiService by lazy {
        RetrofitClient.createService()
    }

    companion object{
        @Volatile
        private var mHttpUtils:HttpUtils? =null
        fun  getInstance() : HttpUtils {
            if (null == mHttpUtils) {
                mHttpUtils = HttpUtils()
            }
            return mHttpUtils as HttpUtils
        }
    }
    fun getApiService():ApiService{
        return mApiService
    }
}
```

```kotlin
class RetrofitClient private constructor(){
    companion object {
        @Volatile
        private var retrofit: Retrofit? = null

        fun createService():ApiService {
            if (retrofit == null) {
                synchronized(RetrofitClient::class) {
                    if (retrofit == null) {
                        retrofit =Retrofit.Builder()
                            .baseUrl(EnvControl.BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(OkHttpUtil.getOkHttpClient())
                            .build()
                    }
                }
            }
            return retrofit!!.create(ApiService::class.java)
        }

    }
}
```


## 总结
Jetpack组件还是有点强的，根据需求，具体使用见自己理解了，以下是[项目地址](https://github.com/liuhang1234/mvvmdemo)