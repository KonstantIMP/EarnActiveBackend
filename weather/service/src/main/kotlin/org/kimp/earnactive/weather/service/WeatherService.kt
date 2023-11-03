package org.kimp.earnactive.weather.service

import io.grpc.stub.StreamObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.kimp.earnactive.auth.api.IEarnActiveAuthGrpc
import org.kimp.earnactive.auth.api.IEarnActiveAuthGrpc.IEarnActiveAuthBlockingStub
import org.kimp.earnactive.auth.api.IEarnActiveAuthGrpc.IEarnActiveAuthStub
import org.kimp.earnactive.auth.api.TGetUserInfoReq
import org.kimp.earnactive.weather.api.IEarnAuthWeatherGrpc.IEarnAuthWeatherImplBase
import org.kimp.earnactive.weather.api.TWeatherInfoReq
import org.kimp.earnactive.weather.api.TWeatherInfoRsp
import org.kimp.earnactive.weather.owa.OpenWeatherApi

class WeatherService(
    private val openWeatherApi: OpenWeatherApi,
    private val authStub: IEarnActiveAuthBlockingStub,
    private val weatherMapper: WeatherMapper
) : IEarnAuthWeatherImplBase() {
    override fun getWeatherInfo(
        request: TWeatherInfoReq,
        responseObserver: StreamObserver<TWeatherInfoRsp>
    ) {
        val userInfo = authStub.getUserInfo(
            TGetUserInfoReq.newBuilder()
                .setAuthToken(request.accessToken)
                .build()
        )

        val weatherInfo = runBlocking (Dispatchers.IO) {
            openWeatherApi.getWeather(request.longitude, request.latitude)
        }

        responseObserver.onNext(weatherMapper.map(weatherInfo))
        responseObserver.onCompleted()
    }
}
