package org.kimp.earnactive.facade.controller

import net.devh.boot.grpc.client.inject.GrpcClient
import org.kimp.earnactive.facade.dto.BalanceResponse
import org.kimp.earnactive.facade.dto.Promocode
import org.kimp.earnactive.facade.dto.PromocodesResponse
import org.kimp.earnactive.promocodes.api.IEarnActivePromoCodesGrpc.IEarnActivePromoCodesBlockingStub
import org.kimp.earnactive.promocodes.api.TSetStepsReq
import org.kimp.earnactive.promocodes.api.TBuyPromoCodeReq
import org.kimp.earnactive.promocodes.api.TGetMyPromoCodesReq
import org.kimp.earnactive.promocodes.api.TGetMyStepsBalanceReq
import org.kimp.earnactive.promocodes.api.TGetPromoCodesInfoReq
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class PromoCodesController : BaseController() {
    @GrpcClient("promocodes")
    private lateinit var promocodesStub: IEarnActivePromoCodesBlockingStub

    @GetMapping("promocodes")
    fun getPromoCodes(
        @RequestHeader("OAuth")
        accessToken: String
    ): PromocodesResponse {
        return PromocodesResponse(
            promocodesStub.getPromoCodesInfo(
                TGetPromoCodesInfoReq.newBuilder().setAccessToken(accessToken).build()
            ).promoCodesList
                .map { p -> Promocode(p.uuid, p.name, p.description, p.cost, avatarUrl =  p.avatarUrl) }
        )
    }

    @GetMapping("promocodes/my")
    fun getMyPromoCodes(
        @RequestHeader("OAuth")
        accessToken: String
    ): PromocodesResponse {
        return PromocodesResponse(
            promocodesStub.getMyPromoCodes(
                TGetMyPromoCodesReq.newBuilder().setAccessToken(accessToken).build()
            ).promoCodesList
                .map { p ->
                    Promocode(
                        p.promoCodeInfo.uuid,
                        p.promoCodeInfo.name,
                        p.promoCodeInfo.description,
                        p.promoCodeInfo.cost,
                        p.promoCodeValue,
                        p.promoCodeInfo.avatarUrl
                    )
                }
        )
    }

    @GetMapping("balance")
    fun getMyBalance(
        @RequestHeader("OAuth")
        accessToken: String
    ): BalanceResponse {
        val balanceInfo = promocodesStub.getMyStepsBalance(
            TGetMyStepsBalanceReq.newBuilder().setAccessToken(accessToken).build()
        )
        return BalanceResponse(
            balanceInfo.balance,
            balanceInfo.lastBalanceChangeTimestamp.seconds
        )
    }

    @PostMapping("promocodes/buy")
    fun buyPromo(
        @RequestHeader("OAuth")
        accessToken: String,
        @RequestParam("promo")
        promoUuid: String
    ): Promocode {
        val p = promocodesStub.buyPromoCode(
            TBuyPromoCodeReq.newBuilder()
                .setAccessToken(accessToken)
                .setPromoCodeUuid(promoUuid)
                .build()
        )
        return Promocode(
            p.promoCode.promoCodeInfo.uuid,
            p.promoCode.promoCodeInfo.name,
            p.promoCode.promoCodeInfo.description,
            p.promoCode.promoCodeInfo.cost,
            p.promoCode.promoCodeValue,
            p.promoCode.promoCodeInfo.avatarUrl
        )
    }

    @PutMapping("steps")
    fun buyPromo(
        @RequestHeader("OAuth")
        accessToken: String,
        @RequestParam("steps")
        steps: Int
    ): BalanceResponse {
        promocodesStub.setSteps(
            TSetStepsReq.newBuilder()
                .setAccessToken(accessToken)
                .setSteps(steps)
                .build()
        )
        return getMyBalance(accessToken)
    }
}
