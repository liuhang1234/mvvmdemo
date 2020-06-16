package com.lh.myapplication.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import java.lang.reflect.ParameterizedType

abstract class BaseActivity<VM : BaseViewModel,VB: ViewDataBinding>  :AppCompatActivity() {

    lateinit var mVM: VM
    lateinit var mVB: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //注意 type.actualTypeArguments[0]=BaseViewModel，type.actualTypeArguments[1]=ViewBinding
        val type = javaClass.genericSuperclass as ParameterizedType
        val clazz1 = type.actualTypeArguments[0] as Class<VM>
        mVM = ViewModelProvider(this).get(clazz1)

        mVB = DataBindingUtil.setContentView(this,getLayoutResID())
        init()
        initVM()

    }

    abstract fun initVM()

    abstract fun  getLayoutResID():Int

    abstract fun init();

}