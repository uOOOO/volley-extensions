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
package com.navercorp.volleyextensions.volleyer.response.parser;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.navercorp.volleyextensions.volleyer.http.ContentType;
import com.navercorp.volleyextensions.volleyer.http.ContentTypes;
import com.navercorp.volleyextensions.volleyer.util.Assert;

import java.io.UnsupportedEncodingException;

/**
 * <pre>
 * A parser class which converts json data to T object.
 *
 * NOTE : If this class is added into {@link IntegratedNetworkResponseParser},
 * and the content type of a response is "application/json" or "text/json" type,
 * integrated parser automatically delegates to this class.
 *
 * WARN : You have to import gson library to use this class.
 * If not, this class throws an error when initializing.
 * </pre>
 */
public class GsonNetworkResponseParser implements TypedNetworkResponseParser {
    /**
     * Default {@link Gson} is singleton.
     */
    private static class GsonHolder {
        private final static Gson gson;

        static {
            gson = new Gson();
        }

        private static Gson getGson() {
            return gson;
        }
    }

    /**
     * {@code gson} is immutable(but not severely).
     */
    private final Gson gson;

    public GsonNetworkResponseParser() {
        this(GsonHolder.getGson());
    }

    public GsonNetworkResponseParser(Gson gson) {
        Assert.notNull(gson, "Gson");
        this.gson = gson;
    }

    protected final String getBodyString(NetworkResponse response) throws UnsupportedEncodingException {
        return new String(response.data, HttpHeaderParser.parseCharset(response.headers));
    }

    @Override
    public <T> Response<T> parseNetworkResponse(NetworkResponse response, Class<T> clazz) {
        Assert.notNull(response, "Response");
        Assert.notNull(clazz, "Class token");

        try {
            T result = gson.fromJson(getBodyString(response), clazz);
            return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (Exception e) {
            return Response.error(new VolleyError(e));
        }
    }

    @Override
    public ContentTypes getContentTypes() {
        return new ContentTypes(ContentType.CONTENT_TYPE_APPLICATION_JSON, ContentType.CONTENT_TYPE_TEXT_JSON);
    }
}
