/*
 * Copyright (C) 2014 Naver Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.navercorp.volleyextensions.request;

import com.android.volley.*;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.navercorp.volleyextensions.util.Assert;

import java.io.UnsupportedEncodingException;

/**
 * A Request{@literal <T>} for which make a request and convert response data by Gson.
 *
 * @param <T> Specific type of an converted object from response data
 * @see AbstractConverterRequest
 * @see Request
 */
public class GsonRequest<T> extends AbstractConverterRequest<T> {
    /**
     * Default {@link Gson} is singleton.
     */
    private static class GsonHolder {
        private static final Gson gson;

        static {
            gson = new Gson();
        }

        private static Gson defaultGson() {
            return gson;
        }
    }

    /**
     * {@code objectMapper} is immutable(but not severely).
     */
    private final Gson gson;

    /**
     * Make a GET request
     *
     * @param url      URL of the request to make
     * @param clazz    Specific type object of an converted object from response data
     * @param listener listener for response
     */
    public GsonRequest(String url, Class<T> clazz, Listener<T> listener) {
        this(url, clazz, GsonHolder.defaultGson(), listener);
    }

    /**
     * Make a GET request with custom {@code objectMapper}
     *
     * @param url      URL of the request to make
     * @param clazz    Specific type object of an converted object from response data
     * @param gson     {@link Gson} to convert
     * @param listener listener for response
     */
    public GsonRequest(String url, Class<T> clazz, Gson gson, Listener<T> listener) {
        super(url, clazz, listener);
        assertGson(gson);
        this.gson = gson;
    }

    /**
     * Make a GET request with {@code errorListener}
     *
     * @param url           URL of the request to make
     * @param clazz         Specific type object of an converted object from response data
     * @param listener      listener for response
     * @param errorListener listener for errors
     */
    public GsonRequest(String url, Class<T> clazz, Listener<T> listener,
                       ErrorListener errorListener) {
        this(url, clazz, GsonHolder.defaultGson(), listener, errorListener);
    }

    /**
     * Make a request with custom {@code objectMapper} and {@code errorListener}
     *
     * @param url           URL of the request to make
     * @param clazz         Specific type object of an converted object from response data
     * @param gson          {@link Gson} to convert
     * @param listener      listener for response
     * @param errorListener listener for errors
     */
    public GsonRequest(String url, Class<T> clazz, Gson gson, Listener<T> listener, ErrorListener errorListener) {
        super(url, clazz, listener, errorListener);
        assertGson(gson);
        this.gson = gson;
    }

    /**
     * Make a request with {@code errorListener}
     *
     * @param method        HTTP method. See here : {@link Method}
     * @param url           URL of the request to make
     * @param clazz         Specific type object of an converted object from response data
     * @param listener      listener for response
     * @param errorListener listener for errors
     */
    public GsonRequest(int method, String url, Class<T> clazz, Listener<T> listener, ErrorListener errorListener) {
        this(method, url, clazz, GsonHolder.defaultGson(), listener, errorListener);
    }

    /**
     * Make a request with custom {@code objectMapper} and {@code errorListener}
     *
     * @param method        HTTP method. See here : {@link Method}
     * @param url           URL of the request to make
     * @param clazz         Specific type object of an converted object from response data
     * @param gson          {@link Gson} to convert
     * @param listener      listener for response
     * @param errorListener listener for errors
     */
    public GsonRequest(int method, String url, Class<T> clazz, Gson gson, Listener<T> listener, ErrorListener errorListener) {
        super(method, url, clazz, listener, errorListener);
        assertGson(gson);
        this.gson = gson;
    }

    private final void assertGson(Gson gson) {
        Assert.notNull(gson, "gson");
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            T result = gson.fromJson(getBodyString(response),
                    getTargetClass());
            return Response.success(result,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (Exception e) {
            return Response.error(new VolleyError(e));
        }
    }
}
