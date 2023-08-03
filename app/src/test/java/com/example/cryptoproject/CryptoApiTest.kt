package com.example.cryptoproject

import com.example.cryptoproject.data.remote.CryptoApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CryptoApiTest {

    lateinit var mockWebServer: MockWebServer
    lateinit var cryptoApi: CryptoApi

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        cryptoApi = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(CryptoApi::class.java)
    }

    @Test
    fun testGetCrypto() = runTest {
        val mockResponse = MockResponse()
        mockResponse.setBody("null")
        mockWebServer.enqueue(mockResponse)

        val response = cryptoApi.getCryptoDetails("20", "10")
        mockWebServer.takeRequest()
        Assert.assertEquals(true, response.body()==null)
    }

    @Test
    fun testGetCrypto_returnList() = runTest {
        val mockResponse = MockResponse()
        val content = Helper.readFileResource("/response.json")
        mockResponse.setResponseCode(200)
        mockResponse.setBody(content)
        mockWebServer.enqueue(mockResponse)

        val response = cryptoApi.getCryptoDetails("20", "10")
        mockWebServer.takeRequest()

        Assert.assertEquals(false, response.body()!!.data!!.isEmpty())
        Assert.assertEquals(20, response.body()!!.data!!.size)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }


}