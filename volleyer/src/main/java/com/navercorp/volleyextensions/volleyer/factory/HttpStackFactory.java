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
package com.navercorp.volleyextensions.volleyer.factory;

import com.android.volley.toolbox.BaseHttpStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.navercorp.volleyextensions.volleyer.multipart.stack.*;
/**
 * <pre>
 * A factory which creates a HttpStack instance for Volleyer.
 * </pre>
 * @author Wonjun Kim
 *
 * @see HttpStack
 * @see DefaultMultipartHttpStack
 * @see MultipartHttpStackWrapper
 */
public class HttpStackFactory {

	private static final String DEFAULT_USER_AGENT = "volleyer";
	/**
	 * <pre>
	 * Create a HttpStack instance that it contains the multipart feature.
	 * The factory determines each of HttpStacks by an Android version on a device.
	 *
	 * NOTE : For a multipart feature, this method must be required.
	 * Instead, you can use directly use {@link MultipartHttpStackWrapper} as a wrapper for a {@code HttpStack}.
	 * </pre>
	 * @return Default HttpStack instance
	 */
	public static BaseHttpStack createDefaultHttpStack() {
		BaseHttpStack httpStack = createHurlStack();

		MultipartHttpStack multipartStack = new DefaultMultipartHttpStack();
		BaseHttpStack completedStack = new MultipartHttpStackWrapper(httpStack, multipartStack);

		return completedStack;
	}
	/**
	 * Create a HurlStack instance (without a multipart feature).
	 * @return HurlStack instance
	 */
	public static HurlStack createHurlStack() {
		return new HurlStack();
	}
}
