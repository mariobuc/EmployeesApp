package app.shimi.com.employeelist.module

import app.shimi.com.employeelist.data.persistence.db.EmployeeDao
import com.thales.employeesapp.core.ApiInterceptor
import com.thales.employeesapp.data.repository.EmployeeRepository
import com.thales.employeesapp.model.EmployeeApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    private const val BASE_URL = "https://dummy.restapiexample.com/api/v1/"

    @Provides
    fun providesHttpLoggingInterceptor() = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    fun providesApiInterceptor() = ApiInterceptor()

    @Singleton
    @Provides
    fun providesOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor,
                             apiInterceptor: ApiInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(apiInterceptor)
            .build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun provideEmployeeApiClient(retrofit: Retrofit):EmployeeApiClient{
        return retrofit.create(EmployeeApiClient::class.java)
    }

    @Singleton
    @Provides
    fun providesRepository(apiService: EmployeeApiClient, employeeDao: EmployeeDao) = EmployeeRepository(apiService,employeeDao)
}

