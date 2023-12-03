package org.kimp.earnactive.promocodes.service

import io.grpc.Status
import io.grpc.stub.StreamObserver
import org.kimp.earnactive.auth.api.IEarnActiveAuthGrpc.IEarnActiveAuthBlockingStub
import org.kimp.earnactive.auth.api.TGetUserInfoReq
import org.kimp.earnactive.db.tables.pojos.Promocodes
import org.kimp.earnactive.promocodes.api.IEarnActivePromoCodesGrpc.IEarnActivePromoCodesImplBase
import org.kimp.earnactive.promocodes.api.TAddStepsReq
import org.kimp.earnactive.promocodes.api.TAddStepsRsp
import org.kimp.earnactive.promocodes.api.TBuyPromoCodeReq
import org.kimp.earnactive.promocodes.api.TBuyPromoCodeRsp
import org.kimp.earnactive.promocodes.api.TGetMyPromoCodesReq
import org.kimp.earnactive.promocodes.api.TGetMyPromoCodesRsp
import org.kimp.earnactive.promocodes.api.TGetMyStepsBalanceReq
import org.kimp.earnactive.promocodes.api.TGetMyStepsBalanceRsp
import org.kimp.earnactive.promocodes.api.TGetPromoCodesInfoReq
import org.kimp.earnactive.promocodes.api.TGetPromoCodesInfoRsp
import org.kimp.earnactive.promocodes.api.TPromoCode
import org.kimp.earnactive.promocodes.api.TPromoCodeInfo
import org.kimp.earnactive.utils.millisToTimestamp
import org.kimp.earnactive.utils.toMillis
import java.time.ZoneId
import java.util.UUID

class PromoCodesService(
    private val authStub: IEarnActiveAuthBlockingStub,
    private val promoCodesManager: PromoCodesManager
) : IEarnActivePromoCodesImplBase() {
    override fun getPromoCodesInfo(
        request: TGetPromoCodesInfoReq,
        responseObserver: StreamObserver<TGetPromoCodesInfoRsp>
    ) {
        val user = authStub.getUserInfo(
            TGetUserInfoReq.newBuilder().setAuthToken(request.accessToken).build()
        )

        responseObserver.onNext(
            TGetPromoCodesInfoRsp.newBuilder()
                .addAllPromoCodes(
                    promoCodesManager.getAvailablePromoCodes()
                        .map { p -> promoToInfo(p) }
                )
                .build()
        )
        responseObserver.onCompleted()
    }

    override fun getMyStepsBalance(
        request: TGetMyStepsBalanceReq,
        responseObserver: StreamObserver<TGetMyStepsBalanceRsp>
    ) {
        val user = authStub.getUserInfo(
            TGetUserInfoReq.newBuilder().setAuthToken(request.accessToken).build()
        )

        responseObserver.onNext(
            TGetMyStepsBalanceRsp.newBuilder()
                .setBalance(promoCodesManager.getUserBalance(UUID.fromString(user!!.uuid)))
                .setLastBalanceChangeTimestamp(
                    promoCodesManager.getLastUserTransactionTimestamp(UUID.fromString(user.uuid))
                        ?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()?.millisToTimestamp()
                        ?: user.creationTimestamp
                )
                .build()
        )
        responseObserver.onCompleted()
    }

    override fun addSteps(
        request: TAddStepsReq,
        responseObserver: StreamObserver<TAddStepsRsp>
    ) {
        val user = authStub.getUserInfo(
            TGetUserInfoReq.newBuilder().setAuthToken(request.accessToken).build()
        )

        promoCodesManager.addStepsToBalance(UUID.fromString(user!!.uuid), request.steps)
        responseObserver.onNext(
            TAddStepsRsp.newBuilder().setAddedSteps(request.steps).build()
        )
        responseObserver.onCompleted()
    }

    override fun buyPromoCode(
        request: TBuyPromoCodeReq,
        responseObserver: StreamObserver<TBuyPromoCodeRsp>
    ) {
        val user = authStub.getUserInfo(
            TGetUserInfoReq.newBuilder().setAuthToken(request.accessToken).build()
        )

        val promo = promoCodesManager.buyPromoCodeForUser(
            UUID.fromString(user.uuid),
            UUID.fromString(request.promoCodeUuid)
        )

        if (promo == null) {
            responseObserver.onError(
                Status.INVALID_ARGUMENT.withDescription("Smack my ass")
                    .asRuntimeException()
            )
        } else {
            responseObserver.onNext(
                TBuyPromoCodeRsp.newBuilder()
                    .setPromoCode(
                        TPromoCode.newBuilder()
                            .setPromoCodeValue(promo.value)
                            .setPromoCodeInfo(promoToInfo(promo))
                            .build()
                    )
                    .build()
            )
            responseObserver.onCompleted()
        }
    }

    override fun getMyPromoCodes(
        request: TGetMyPromoCodesReq,
        responseObserver: StreamObserver<TGetMyPromoCodesRsp>
    ) {
        val user = authStub.getUserInfo(
            TGetUserInfoReq.newBuilder().setAuthToken(request.accessToken).build()
        )

        responseObserver.onNext(
            TGetMyPromoCodesRsp.newBuilder()
                .addAllPromoCodes(
                    promoCodesManager.getUserPromoCodes(UUID.fromString(user!!.uuid))
                        .map { p ->
                            TPromoCode.newBuilder()
                                .setPromoCodeValue(p.value!!)
                                .setPromoCodeInfo(promoToInfo(p))
                                .build()
                        }
                )
                .build()
        )
        responseObserver.onCompleted()
    }

    private fun promoToInfo(p: Promocodes): TPromoCodeInfo = TPromoCodeInfo.newBuilder()
        .setUuid(p.uuid.toString())
        .setName(p.name)
        .setCost(p.cost!!)
        .setDescription(p.description)
        .build()
}
