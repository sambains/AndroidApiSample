package me.sambains.androidapisample.core.api

import androidx.test.ext.junit.runners.AndroidJUnit4
import me.sambains.androidapisample.core.base.BaseApplicationTest
import me.sambains.androidapisample.core.dependencies.AppModule.Companion.REQUEST_HEADER_CACHE_CONTROL_NAME
import me.sambains.androidapisample.core.dependencies.AppModule.Companion.REQUEST_HEADER_CACHE_CONTROL_VALUE_NONE
import me.sambains.androidapisample.core.helpers.NetworkHelper
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class TheMovieDbApiCacheTest {

    @Inject
    lateinit var okHttpClient: OkHttpClient

    @Inject
    lateinit var networkHelper: NetworkHelper

    private var mockWebServer: MockWebServer? = null
    private lateinit var defaultMockResponse: MockResponse
    private lateinit var defaultRequest: Request

    @Before
    fun setUp() {
        //MockitoAnnotations.openMocks(this)
        BaseApplicationTest.getAppComponent()
            .inject(this)

//        `when`(networkHelper.hasNetworkConnection(InstrumentationRegistry.getInstrumentation().targetContext))
//            .thenReturn(true)

        okHttpClient.cache()!!.evictAll()

        mockWebServer = MockWebServer()
        mockWebServer!!.start()

        defaultMockResponse = MockResponse()
            .addHeader("Access-Control-Allow-Origin", "*")
            .addHeader("Access-Control-Expose-Headers", "ETag, X-RateLimit-Limit, X-RateLimit-Remaining, X-RateLimit-Reset, Retry-After")
            .addHeader("Cache-Control", "public, max-age=21600")
            .addHeader("Content-Type", "application/json;charset=utf-8")
            .setBody("Test response body!")

        defaultRequest = Request.Builder()
            .url(mockWebServer!!.url("/"))
            .build()
    }

    @After
    fun tearDown() {
        if (mockWebServer != null) {
            mockWebServer!!.shutdown()
        }
    }

    @Test
    fun testOnlyResponsesWithCode200AreCached() {
        assertResponseCodeIsCached(false, 100)
        assertResponseCodeIsCached(false, 101)
        assertResponseCodeIsCached(false, 102)
        assertResponseCodeIsCached(true, 200)
        assertResponseCodeIsCached(true, 201)
        assertResponseCodeIsCached(false, 202)
        assertResponseCodeIsCached(false, 203)
        assertResponseCodeIsCached(false, 204)
        assertResponseCodeIsCached(false, 205)
        assertResponseCodeIsCached(false, 206)
        assertResponseCodeIsCached(false, 207)
        assertResponseCodeIsCached(false, 208)
        assertResponseCodeIsCached(false, 226)
        assertResponseCodeIsCached(false, 300)
        assertResponseCodeIsCached(false, 301)
        assertResponseCodeIsCached(false, 302)
        assertResponseCodeIsCached(false, 303)
        assertResponseCodeIsCached(false, 304)
        assertResponseCodeIsCached(false, 305)
        assertResponseCodeIsCached(false, 306)
        assertResponseCodeIsCached(false, 307)
        assertResponseCodeIsCached(false, 308)
        assertResponseCodeIsCached(false, 400)
        assertResponseCodeIsCached(false, 401)
        assertResponseCodeIsCached(false, 402)
        assertResponseCodeIsCached(false, 403)
        assertResponseCodeIsCached(false, 404)
        assertResponseCodeIsCached(false, 405)
        assertResponseCodeIsCached(false, 406)
        assertResponseCodeIsCached(false, 408)
        assertResponseCodeIsCached(false, 409)
        assertResponseCodeIsCached(false, 410)
        assertResponseCodeIsCached(false, 411)
        assertResponseCodeIsCached(false, 412)
        assertResponseCodeIsCached(false, 413)
        assertResponseCodeIsCached(false, 414)
        assertResponseCodeIsCached(false, 415)
        assertResponseCodeIsCached(false, 416)
        assertResponseCodeIsCached(false, 417)
        assertResponseCodeIsCached(false, 418)
        assertResponseCodeIsCached(false, 421)
        assertResponseCodeIsCached(false, 422)
        assertResponseCodeIsCached(false, 423)
        assertResponseCodeIsCached(false, 424)
        assertResponseCodeIsCached(false, 426)
        assertResponseCodeIsCached(false, 428)
        assertResponseCodeIsCached(false, 429)
        assertResponseCodeIsCached(false, 431)
        assertResponseCodeIsCached(false, 451)
        assertResponseCodeIsCached(false, 500)
        assertResponseCodeIsCached(false, 501)
        assertResponseCodeIsCached(false, 502)
        assertResponseCodeIsCached(false, 503)
        assertResponseCodeIsCached(false, 504)
        assertResponseCodeIsCached(false, 505)
        assertResponseCodeIsCached(false, 506)
        assertResponseCodeIsCached(false, 507)
        assertResponseCodeIsCached(false, 508)
        assertResponseCodeIsCached(false, 510)
        assertResponseCodeIsCached(false, 511)
    }

    @Test
    fun testResponseHasUnmodifiedCacheHeadersFor200ResponseCode() {
        val mockResponse = defaultMockResponse
        val response: Response = makeMockServerRequest(mockResponse)
        assertTrue(response.headers().names().contains(REQUEST_HEADER_CACHE_CONTROL_NAME))
        assertEquals("public, max-age=21600", response.header(REQUEST_HEADER_CACHE_CONTROL_NAME))
    }

    @Test
    fun testResponseHasModifiedCacheHeadersForNon200ResponseCode() {
        val mockResponse = defaultMockResponse
        mockResponse.setResponseCode(403) //Forbidden
        val response: Response = makeMockServerRequest(mockResponse)
        assertTrue(response.headers().names().contains(REQUEST_HEADER_CACHE_CONTROL_NAME))
        assertEquals(REQUEST_HEADER_CACHE_CONTROL_VALUE_NONE, response.header(REQUEST_HEADER_CACHE_CONTROL_NAME))
    }

    @Test
    fun testCachedResponseIsValid() {
        val mockResponse = defaultMockResponse
        mockResponse.setResponseCode(200)
        makeMockServerRequest(mockResponse)
        val cachedResponse: Response = makeMockServerRequest(mockResponse)
        assertNotNull(cachedResponse)
        assertNotNull(cachedResponse.cacheResponse())
        assertNull(cachedResponse.networkResponse())
        assertNotNull(cachedResponse.body())
        val cachedResponseBody: String = cachedResponse.body()!!.string()
        assertNotNull(cachedResponseBody)
        assertEquals("Test response body!", cachedResponseBody)
    }

    @Test
    fun testNoInternetConnectionReturnsCacheWithCustomCacheControlHeader() {
        okHttpClient.cache()!!.evictAll()
        val mockResponse = defaultMockResponse
        //Modify the cache response to have it requested an hour ago, the cache to be valid for
        //30 minutes and the expiry to be 30 minutes ago.
        //This way, the cache will be expired when we make the offline request.
        mockResponse.setHeader("cache-control", "public, max-age=1800")
        mockResponse.setResponseCode(200)

        //Online request
        makeMockServerRequest(mockResponse)
//        `when`(networkHelper.hasNetworkConnection(InstrumentationRegistry.getInstrumentation().targetContext))
//            .thenReturn(false)

        //Offline request
        val offlineRequest: Response = makeMockServerRequest(mockResponse)
        assertNotNull(offlineRequest)
        assertNotNull(offlineRequest.cacheResponse())
        assertNull(offlineRequest.networkResponse())
        assertNotNull(offlineRequest.body())
        assertTrue(offlineRequest.headers().names().contains(REQUEST_HEADER_CACHE_CONTROL_NAME))
        assertEquals("public, max-age=1800", offlineRequest.header(REQUEST_HEADER_CACHE_CONTROL_NAME))
    }

    private fun assertResponseCodeIsCached(shouldCache: Boolean, responseCode: Int) {
        okHttpClient.cache()!!.evictAll()
        val mockResponse = defaultMockResponse
        mockResponse.setResponseCode(responseCode)
        when (responseCode) {
            204, 205 -> mockResponse.setBody("")
            else -> {}
        }
        makeMockServerRequest(mockResponse)
        val cachedResponse: Response = makeMockServerRequest(mockResponse)
        assertNotNull(cachedResponse)
        if (shouldCache) {
            assertNotNull(cachedResponse.cacheResponse())
            assertNull(cachedResponse.networkResponse())
        } else {
            assertNull(cachedResponse.cacheResponse())
            assertNotNull(cachedResponse.networkResponse())
        }
    }

    private fun makeMockServerRequest(mockResponse: MockResponse): Response {
        mockWebServer!!.enqueue(mockResponse)
        val request = defaultRequest
        return okHttpClient.newCall(request).execute()
    }
}