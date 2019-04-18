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
package com.navercorp.volleyextensions.volleyer.multipart.stack;

import java.io.IOException;
import java.util.Map;

import com.android.volley.toolbox.BaseHttpStack;
import com.android.volley.toolbox.HttpResponse;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.navercorp.volleyextensions.volleyer.multipart.MultipartContainer;
import com.navercorp.volleyextensions.volleyer.util.Assert;
/**
 * <pre>
 * A wrapper for extending a multipart feature into original {@code HttpStack}.
 * </pre>
 */
public class MultipartHttpStackWrapper extends BaseHttpStack {

	private final BaseHttpStack stack;
	private MultipartHttpStack multipartStack;
	/**
	 * Constructor with default multipart stack.
	 * @param stack original stack which performs for a common case.
	 */
	public MultipartHttpStackWrapper(BaseHttpStack stack) {
		this(stack, new DefaultMultipartHttpStack());
	}
	/**
	 * Constructor with custom multipart stack.
	 * @param stack original stack which performs for a common case.
	 * @param multipartStack Multipart stack which performs for a multipart case.
	 */
	public MultipartHttpStackWrapper(BaseHttpStack stack, MultipartHttpStack multipartStack) {
		Assert.notNull(stack, "HttpStack");
		Assert.notNull(multipartStack, "MultipartHttpStack");
		this.stack = stack;
		this.multipartStack = multipartStack;
	}

	/**
	 * <pre>
	 * Delegate a perform to {@code MultipartHttpStack} if {@code Request} has a {@code Multipart}.
	 * If not, This delegate to original {@code HttpStack}.
	 * </pre>
	 */
	@Override
	public HttpResponse executeRequest(Request<?> request, Map<String, String> additionalHeaders) throws IOException, AuthFailureError {
		if (hasMultipart(request)) {
			return multipartStack.executeRequest(request, additionalHeaders);
		}

		return stack.executeRequest(request, additionalHeaders);
	}

	private boolean hasMultipart(Request<?> request) {
		if (!(request instanceof MultipartContainer)) {
			return false;
		}

		MultipartContainer container = (MultipartContainer) request;
		return container.hasMultipart();
	}
}
