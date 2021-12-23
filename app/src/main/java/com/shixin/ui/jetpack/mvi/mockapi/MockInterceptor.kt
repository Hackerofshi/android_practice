package com.shixin.ui.jetpack.mvi.mockapi

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import rx.android.BuildConfig

class MockInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (BuildConfig.DEBUG) {
            val uri = chain.request().url.toUri().toString()
            val responseString = when {
                uri.endsWith("mock") -> getMockApiResponse
                else -> ""
            }

            return Response.Builder()
                .request(chain.request())
                .code(200)
                .protocol(Protocol.HTTP_2)
                .message(responseString)
                .body(
                    ResponseBody.create(
                        "application/json".toMediaTypeOrNull(),
                        responseString.toByteArray()
                    )
                )
                .addHeader("content-type", "application/json")
                .build()
        } else {
            //just to be on safe side.
            throw IllegalAccessError("""MockInterceptor is only meant for Testing Purposes and bound to be used only with DEBUG mode""")
        }
    }
}

const val getMockApiResponse = """
{
  "articles": [
    {
      "title": "Title",
      "description": "Description",
      "imageUrl": "imageUrl"
    },
    {
      "title": "Title",
      "description": "Description",
      "imageUrl": "imageUrl"
    }
  ]
}"""