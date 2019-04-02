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

import com.android.volley.toolbox.HttpResponse;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
/**
 * <pre>
 * Default multipart http stack.
 * It includes a internal multipart stack which is determined by an Android version on a device.
 * </pre>
 */
public class DefaultMultipartHttpStack extends MultipartHttpStack {
	private MultipartHttpStack stack;
	/**
	 * Default constructor
	 */
	public DefaultMultipartHttpStack() {
		determineMultipartStack();
	}

	private void determineMultipartStack() {
		stack = new MultipartHurlStack();
	}

	@Override
	public HttpResponse executeRequest(Request<?> request, Map<String, String> additionalHeaders) throws IOException, AuthFailureError {
		return stack.executeRequest(request, additionalHeaders);
	}
}
