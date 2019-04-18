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

import com.android.volley.toolbox.BaseHttpStack;
import org.junit.Test;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.navercorp.volleyextensions.volleyer.request.VolleyerRequest;

import static org.mockito.Mockito.*;

public class MultipartHttpStackWrapperTest {

	static final MultipartHttpStack mockMultipartHttpStack = mock(MultipartHttpStack.class);
	static final BaseHttpStack mockHttpStack = mock(BaseHttpStack.class);

	@Test(expected = NullPointerException.class)
	public void shouldThrowNpeWhenHttpStackIsNull() {
		new MultipartHttpStackWrapper(null /* httpstack */, mockMultipartHttpStack);
	}

	@Test(expected = NullPointerException.class)
	public void shouldThrowNpeWhenMultipartHttpStackIsNull() {
		new MultipartHttpStackWrapper(mockHttpStack  , null /* multipart httpstack */);
	}

	@Test
	public void shouldCallMultipartHttpStackWhenRequestHasMultipart() throws AuthFailureError, IOException {
		// Given
		VolleyerRequest<?> request = mock(VolleyerRequest.class);
		MultipartHttpStackWrapper wrapper = new MultipartHttpStackWrapper(mockHttpStack  , mockMultipartHttpStack);
		// When
		when(request.hasMultipart()).thenReturn(true);
		wrapper.executeRequest(request, null);
		// Then
		verify(mockHttpStack, never()).executeRequest(request, null);
		verify(mockMultipartHttpStack).executeRequest(request, null);
	}

	@Test
	public void shouldCallHttpStackWhenRequestHasNotMultipart() throws AuthFailureError, IOException {
		// Given
		VolleyerRequest<?> request = mock(VolleyerRequest.class);
		MultipartHttpStackWrapper wrapper = new MultipartHttpStackWrapper(mockHttpStack, mockMultipartHttpStack);
		// When
		when(request.hasMultipart()).thenReturn(false);
		wrapper.executeRequest(request, null);
		// Then
		verify(mockHttpStack).executeRequest(request, null);
		verify(mockMultipartHttpStack, never()).executeRequest(request, null);
	}

	@Test
	public void shouldCallHttpStackWhenRequestIsNotContainer() throws AuthFailureError, IOException {
		// Given
		Request<?> request = mock(Request.class);
		MultipartHttpStackWrapper wrapper = new MultipartHttpStackWrapper(mockHttpStack, mockMultipartHttpStack);
		// When
		wrapper.executeRequest(request, null);
		// Then
		verify(mockHttpStack).executeRequest(request, null);
		verify(mockMultipartHttpStack, never()).executeRequest(request, null);
	}
}
