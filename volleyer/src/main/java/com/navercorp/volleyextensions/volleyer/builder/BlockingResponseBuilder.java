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
package com.navercorp.volleyextensions.volleyer.builder;

import android.util.Pair;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.RequestFuture;
import com.navercorp.volleyextensions.volleyer.VolleyerConfiguration;
import com.navercorp.volleyextensions.volleyer.http.HttpContent;
import com.navercorp.volleyextensions.volleyer.request.creator.RequestCreator;
import com.navercorp.volleyextensions.volleyer.request.executor.RequestExecutor;
import com.navercorp.volleyextensions.volleyer.response.parser.NetworkResponseParser;
import com.navercorp.volleyextensions.volleyer.util.Assert;

/**
 * A builder class that enables settings for response and that executes a request.
 * @author Wonjun Kim
 *
 * @param <T> Target class that content of a response will be parsed to
 */
public class BlockingResponseBuilder<T> {

	private RequestQueue requestQueue;
	private VolleyerConfiguration configuration;
	private HttpContent httpContent;
	private Class<T> clazz;
	private NetworkResponseParser responseParser;
	private RetryPolicy retryPolicy;

	private boolean isDoneToBuild = false;
	/**
	 * Default constructor for ResponseBuilder
	 * @param requestQueue running RequestQueue instance which will executes a request
	 * @param configuration VolleyerConfiguration instance. See {@link VolleyerConfiguration}.
	 * @param httpContent HttpContent instance which is previously set from {@code RequestBuilder}
	 * @param retryPolicy RetryPolicy instance which is previously set from {@code RequestBuilder}
	 * @param clazz Target class that content of a response will be parsed to.
	 */
	BlockingResponseBuilder(RequestQueue requestQueue, VolleyerConfiguration configuration, HttpContent httpContent,
							Class<T> clazz, @NonNull RetryPolicy retryPolicy) {
		Assert.notNull(requestQueue, "RequestQueue");
		Assert.notNull(configuration, "VolleyerConfiguration");
		Assert.notNull(httpContent, "HttpContent");
		Assert.notNull(clazz, "Target class token");
		Assert.notNull(retryPolicy, "RetryPolicy");

		this.requestQueue = requestQueue;
		this.configuration = configuration;
		this.httpContent = httpContent;
		this.clazz = clazz;
		this.retryPolicy = retryPolicy;
	}

	/**
	 * Check whether execution has been done. 
	 */
	private void assertFinishState() {
		if (isDoneToBuild == true) {
			throw new IllegalStateException("ResponseBuilder should not be used any more. Because execute() has been called.");
		}
	}

	/**
	 * Set a parser for which content of a response is converted to a target class.
	 * <pre>
	 * NOTE : If this is not set, volleyer sets up an NetworkResponseParser instance
	 *        of {@code VolleyerConfiguration} as a fallback for a Request.
	 * </pre>
	 * @param responseParser
	 */
	public BlockingResponseBuilder<T> withResponseParser(NetworkResponseParser responseParser) {
		Assert.notNull(responseParser, "Response Parser");
		assertFinishState();
		this.responseParser = responseParser;
		return this;
	}

	/**
	 * Execute a request finally on a running RequestQueue.
	 * @return Request instance being executed
	 */
	public RequestFuture<T> execute() {
		setFallbackResponseParserIfNull();

		Pair<Request<T>, RequestFuture<T>> requests = buildRequestFuture();
		if (requests == null) {
			return null;
		}
		Request<T> request = requests.first;
		RequestFuture<T> requestFuture = requests.second;
		executeRequestFuture(request, requestFuture);
		markFinishState();
		return requestFuture;
	}

	/**
	 * Make this builder being disabled settings.
	 */
	protected final void markFinishState() {
		isDoneToBuild = true;
		// Let requestQueue be null for avoiding memory leak when this builder is referenced by some variable.
		requestQueue = null;
	}

	private void setFallbackResponseParserIfNull() {
		if (responseParser != null) {
			return;
		}

		responseParser = configuration.getFallbackNetworkResponseParser();
	}

	/**
	 * Create a {@code Request} object by {@link RequestCreator} of {@code VolleyerConfiguration}.
	 * @return Newly built Request object
	 */
	@Nullable
	private Pair<Request<T>, RequestFuture<T>> buildRequestFuture() {
		RequestCreator requestCreator = configuration.getRequestCreator();
		RequestFuture<T> requestFuture = RequestFuture.newFuture();
		Request<T> request =
				requestCreator.createRequest(httpContent, clazz, responseParser, requestFuture, requestFuture, retryPolicy);
		return request == null ? null : Pair.create(request, requestFuture);
	}

	/**
	 * Execute a given request by {@link RequestExecutor} of {@code VolleyerConfiguration}.
	 */
	private void executeRequestFuture(@NonNull Request<T> request, @NonNull RequestFuture<T> requestFuture) {
		RequestExecutor executor = configuration.getRequestExecutor();
		executor.executeRequestFuture(requestQueue, request, requestFuture);
	}
}
