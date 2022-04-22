package com.credibanco.smartpos.payment.controller

import com.credibanco.smartpos.payment.dto.AnnulmentRequest
import com.credibanco.smartpos.payment.dto.AnnulmentResponse
import com.credibanco.smartpos.payment.dto.AuthorizationRequest
import com.credibanco.smartpos.payment.dto.AuthorizationResponse
import org.springframework.core.env.Environment
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
class TestController {

    private var env = System.getProperties()
    private var dir = System.getProperty("user.dir")
    private val catalinaBase = System.getProperty("catalina.base")
    private val osName = System.getProperty("os.name")

    @RequestMapping("/")
    fun welcomepage(): String {
        return "Hello Docker World"
    }

    @RequestMapping("/docker")
    fun docker(): MutableList<String> {
        return mutableListOf(dir, catalinaBase, osName)
    }
}

@RestController
@RequestMapping("/api/payments")
class PaymentController {

    @PostMapping("/authorization")
    fun authorization(@RequestBody authorizationRequest: AuthorizationRequest,
                      @RequestHeader(name = HttpHeaders.AUTHORIZATION) basicAuth: String)
    : ResponseEntity<AuthorizationResponse>{
        println(authorizationRequest)
        if (basicAuth == "Basic MDAwMTIzMDAwQUJD") {
            val commerceCode = authorizationRequest.commerceCode
            val terminalCode = authorizationRequest.terminalCode
            if (commerceCode == "000123" && terminalCode == "000ABC") {
                println("   Paso commerce y terminal")
                val card = authorizationRequest.card
                return if (card == "1234567890123456"){
                    println("       Paso card")
                    val receiptId = UUID.randomUUID().toString()
                    val rrn = UUID.randomUUID().toString()

                    val response = AuthorizationResponse(receiptId, rrn, "00", "Aprobada")
                    ResponseEntity<AuthorizationResponse>(response, HttpStatus.ACCEPTED)
                } else {
                    val response = AuthorizationResponse(null,
                            null, "99", "Número de tarjeta incorrecto")
                    ResponseEntity<AuthorizationResponse>(response, HttpStatus.BAD_REQUEST)
                }
            } else {
                val response = AuthorizationResponse(null,
                        null, "99", "Código de comercio o número de terminal incorrecto")
                return ResponseEntity<AuthorizationResponse>(response, HttpStatus.BAD_REQUEST)
            }
        } else {
            val response = AuthorizationResponse(null, null, "99",
                    "Header incorrecto")
            return ResponseEntity<AuthorizationResponse>(response, HttpStatus.UNAUTHORIZED)
        }
    }

    @PostMapping("/annulment")
    fun annulment(@RequestBody annulmentRequest: AnnulmentRequest,
                  @RequestHeader(name = HttpHeaders.AUTHORIZATION) basicAuth: String)
    : ResponseEntity<AnnulmentResponse> {
        return if (basicAuth == "Basic MDAwMTIzMDAwQUJD") {
            val response = AnnulmentResponse("00", "Aprobada")
            ResponseEntity<AnnulmentResponse>(response, HttpStatus.ACCEPTED)
        } else {
            val response = AnnulmentResponse("99", "Header incorrecto")
            ResponseEntity<AnnulmentResponse>(response, HttpStatus.UNAUTHORIZED)
        }
    }
}