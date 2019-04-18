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

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.navercorp.volleyextensions.mock.ErrorResponseHoldListener;
import com.navercorp.volleyextensions.mock.ResponseHoldListener;
import org.apache.http.protocol.HTTP;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class GsonRequestTest {
	String url = "http://localhost";
	ResponseHoldListener<News> listener = new ResponseHoldListener<News>();
	ErrorResponseHoldListener errorListener = new ErrorResponseHoldListener();

	@BeforeClass
	public static void setUpOnce() throws Exception {
		ShadowLog.stream = System.out;
	}

	@Test
	public void networkResponseShouldBeParsed() throws Exception {
		// Given
		String content = "{\"imageUrl\":\"http://static.naver.com/volley-ext.jpg\"," +
						   "\"title\":\"Volley extention has released\"}";
		NetworkResponse networkResponse = new NetworkResponse(content.getBytes());
		GsonRequest<News> request = new GsonRequest<>(url, News.class, listener);
		// When
		Response<News> response = request.parseNetworkResponse(networkResponse);
		// Then
		News news = response.result;
		assertThat(news.imageUrl, is("http://static.naver.com/volley-ext.jpg"));
		assertThat(news.title, is("Volley extention has released"));
	}
	
	@Test
	public void networkResponseShouldBeParsedWithAddtionalAttribute() throws Exception {
		// Given
		String content = "{\"imageUrl\":\"http://static.naver.com/volley-ext.jpg\"," +
						   "\"title\":\"Volley extention has released\"," +
						   "\"content\":\"Very good News\"}";
		NetworkResponse networkResponse = new NetworkResponse(content.getBytes());
		GsonRequest<News> request = new GsonRequest<>(url, News.class, listener);
		// When
		Response<News> response = request.parseNetworkResponse(networkResponse);
		// Then
		News news = response.result;
		assertThat(news.imageUrl, is("http://static.naver.com/volley-ext.jpg"));
		assertThat(news.title, is("Volley extention has released"));
	}
	
	@Test
	public void networkResponseShouldNotBeParsedWithInvalidFormat() throws Exception {
		// Given
		String content = "{\"imageUrl\":\"http://static.nav";
		NetworkResponse networkResponse = new NetworkResponse(content.getBytes());
		GsonRequest<News> request = new GsonRequest<>(url, News.class, listener);
		// When
		Response<News> response = request.parseNetworkResponse(networkResponse);
		// Then
		assertNull(response.result);
		assertThat(response.error, is(instanceOf(ParseError.class)));
		assertThat(response.error.getCause(), is(instanceOf(JsonSyntaxException.class)));
	}

	@Test
	public void networkResponseShouldBeParsedWithSpecialChars() throws Exception {
		// Given
		String content = "{\"title\":\"å &acirc;\"}";
		NetworkResponse networkResponse = new NetworkResponse(content.getBytes());
		Gson gsonWithDefaultOption = new Gson();
		GsonRequest<News> request = new GsonRequest<News>(url, News.class, gsonWithDefaultOption, listener);
		// When
		Response<News> response = request.parseNetworkResponse(networkResponse);
		// Then
		assertNotNull(response.result);
		assertNull(response.error);
	}

	@Test
	public void networkResponseShouldNotBeParsedWithUnsupportedException() throws Exception {
		// Given
		String content = "{\"imageUrl\":\"http://static.naver.com\"}";
		HashMap<String, String> headers = new HashMap<>();
		headers.put(HTTP.CONTENT_TYPE, "text/html;charset=UTF-14");
		NetworkResponse networkResponse = new NetworkResponse(content.getBytes(), headers);
		GsonRequest<News> request = new GsonRequest<News>(url, News.class, listener);
		// When
		Response<News> response = request.parseNetworkResponse(networkResponse);
		// Then
		assertNull(response.result);
		assertThat(response.error, is(instanceOf(ParseError.class)));
		assertThat(response.error.getCause(), is(instanceOf(UnsupportedEncodingException.class)));
	}	
	
	
	@Test(expected = NullPointerException.class)
	public void requestShouldThrowNpeWhenGsonIsNull() {
		new GsonRequest<>(url, News.class, null, listener);
	}

	@Test(expected = NullPointerException.class)
	public void requestShouldThrowNpeWhenGsonIsNullWithErrorListener() {
		new GsonRequest<>(url, News.class, null, listener, errorListener);
	}

	@Test(expected = NullPointerException.class)
	public void requestShouldThrowNpeWhenGsonIsNullWithErrorListenerAndMethod() {
		new GsonRequest<>(Method.GET, url, News.class, null, listener, errorListener);
	}

	@Test(expected = NullPointerException.class)
	public void requestShouldThrowNpeWhenListenerIsNull() {
		new GsonRequest<>(url, News.class, null);
	}

	@Test(expected = NullPointerException.class)
	public void testWhenListenerIsNullWithErrorListener() {
		new GsonRequest<>(url, News.class, null, errorListener);
	}

	@Test(expected = NullPointerException.class)
	public void testWhenListenerIsNullWithErrorListenerAndMethod() {
		new GsonRequest<>(Method.GET, url, News.class, null, errorListener);
	}

	/** just for test */
	private static class News {
		public String imageUrl;
		public String title;		
	}
}
