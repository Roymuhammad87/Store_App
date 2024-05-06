package com.adrammedia.storenet.backend.di

import com.adrammedia.storenet.backend.api.profile.StoreProfileApi
import com.adrammedia.storenet.backend.api.user.LogoutApi
import com.adrammedia.storenet.backend.api.user.RegisterLoginApiApi
import com.adrammedia.storenet.backend.api.tool.ToolsApi
import com.adrammedia.storenet.backend.api.category.CategoriesApi
import com.adrammedia.storenet.backend.api.user.passwords.ForgetPasswordApi
import com.adrammedia.storenet.frontend.ui.activities.HomeActivity
import com.adrammedia.storenet.utils.Constants
import com.adrammedia.storenet.utils.Constants.NETWORK_TIMEOUT
import com.adrammedia.storenet.utils.TokenNotRequired
import com.adrammedia.storenet.utils.TokenRequired
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    fun provideNetworkTimeOut() = NETWORK_TIMEOUT


    @Provides
    @Singleton
    fun requestInterceptor():Interceptor= Interceptor { chain->
        val url = chain.request()
            .url
            .newBuilder()
            .build()
        val request = chain.request()
            .newBuilder()
            .addHeader("Authorization", "Bearer ${HomeActivity.apiToken}")
            .url(url)
            .build()
        return@Interceptor chain.proceed(request)
    }
    @TokenNotRequired
    @Provides
    @Singleton
    fun provideOkHttpClient(
        time: Long
    ): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(time, TimeUnit.SECONDS)
        .readTimeout(time, TimeUnit.SECONDS)
        .writeTimeout(time, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()
    @TokenRequired
    @Provides
    @Singleton
    fun provideLoginLogoutClient(time: Long,interceptor: Interceptor) = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .connectTimeout(time, TimeUnit.SECONDS)
        .readTimeout(time, TimeUnit.SECONDS)
        .writeTimeout(time, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()



    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().setLenient().create()


    @Provides
    @Singleton
    fun provideRegisterApi(@TokenNotRequired client: OkHttpClient, gson: Gson): RegisterLoginApiApi =
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(RegisterLoginApiApi::class.java)


    @Provides
    @Singleton
    fun provideLogoutApi(@TokenRequired client: OkHttpClient, gson: Gson): LogoutApi =
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(LogoutApi::class.java)

    @Provides
    @Singleton
    fun provideToolsApi(@TokenRequired client: OkHttpClient, gson: Gson): ToolsApi =
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ToolsApi::class.java)
    @Provides
    @Singleton
    fun providCategoriessApi(@TokenNotRequired client: OkHttpClient, gson: Gson): CategoriesApi =
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(CategoriesApi::class.java)


    @Provides
    @Singleton
    fun provideForgetPasswordApi(@TokenNotRequired client: OkHttpClient, gson: Gson): ForgetPasswordApi =
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ForgetPasswordApi::class.java)

    @Provides
    @Singleton
    fun provideStoreProfileApi(@TokenRequired client: OkHttpClient, gson: Gson): StoreProfileApi =
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(StoreProfileApi::class.java)
}
