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

import androidx.annotation.NonNull;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.navercorp.volleyextensions.volleyer.VolleyerConfiguration;
import com.navercorp.volleyextensions.volleyer.http.HttpMethod;

class TestPurposeRequestBuilder extends RequestBuilder<TestPurposeRequestBuilder> {

	public TestPurposeRequestBuilder(RequestQueue requestQueue, VolleyerConfiguration configuration, String url,
									 HttpMethod method, @NonNull RetryPolicy retryPolicy) {
		super(requestQueue, configuration, url, method, retryPolicy);
	}

}
