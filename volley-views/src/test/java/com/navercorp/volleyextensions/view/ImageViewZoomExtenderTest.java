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
package com.navercorp.volleyextensions.view;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class ImageViewZoomExtenderTest {
	
	@Test(expected=NullPointerException.class)
	public void extenderShouldthrowNpeWhenImageViewIsNull() {
		// Given
		ImageView nullImageView = null;
		// When 
		ZoomableComponent extender = new ImageViewZoomExtender(nullImageView);
	}

	@Test
	public void imageViewShouldbeMatrixTypeWhenExtenderIsCreated() {
		// Given
		ImageView imageView = new ImageView(ApplicationProvider.getApplicationContext());
		// When 
		ZoomableComponent extender = new ImageViewZoomExtender(imageView);
		// Then
		assertEquals(ScaleType.MATRIX, imageView.getScaleType());
	}

	@Test
	public void zoomLevelShouldBeOriginalLevelWhenExtenderIsCreated() {
		// Given
		ImageView imageView = new ImageView(ApplicationProvider.getApplicationContext());
		// When 
		ZoomableComponent extender = new ImageViewZoomExtender(imageView);
		// Then
		assertEquals(extender.getZoomLevel(), ImageViewZoomExtender.ORIGINAL_LEVEL, 0.0);
	}

	@Test
	public void zoomLevelShouldBeZoomInfoLevelWhenExtenderRestores() {
		// Given
		float zoomLevel = 7.0f;
		ZoomInfo zoomInfo = new ZoomInfo(zoomLevel);
		ImageView imageView = new ImageView(ApplicationProvider.getApplicationContext());
		imageView.setImageBitmap(createTestBitmap());
		ZoomableComponent extender = new ImageViewZoomExtender(imageView);
		// When
		extender.restore(zoomInfo);
		// Then
		assertEquals(extender.getZoomLevel(), zoomLevel, 0.0);
	}
	
	@Test
	public void extenderShouldCheckDrawableExistsWhenExtenderRestores() {
		// Given
		float zoomLevel = 7.0f;
		ZoomInfo zoomInfo = new ZoomInfo(zoomLevel);
		ImageView imageView = mock(ImageView.class);
		imageView.setImageBitmap(createTestBitmap());
		ZoomableComponent extender = new ImageViewZoomExtender(imageView);
		// When
		extender.restore(zoomInfo);
		// Then
		verify(imageView).getDrawable();
	}

	@Test
	public void extenderShouldCheckDrawableExistsWhenExtenderZooms() {
		// Given
		float zoomLevel = 7.0f;
		float zoomX = 0.0f;
		float zoomY = 0.0f;
		ImageView imageView = mock(ImageView.class);
		imageView.setImageBitmap(createTestBitmap());
		ZoomableComponent extender = new ImageViewZoomExtender(imageView);
		// When
		extender.zoomTo(zoomLevel, zoomX, zoomY);
		// Then
		verify(imageView).getDrawable();
	}

	@Test
	public void extenderShouldCheckDrawableExistsWhenExtenderPans() {
		// Given
		float dX = 0.0f;
		float dY = 0.0f;
		ImageView imageView = mock(ImageView.class);
		imageView.setImageBitmap(createTestBitmap());
		ZoomableComponent extender = new ImageViewZoomExtender(imageView);
		// When
		extender.panTo(dX, dY);
		// Then
		verify(imageView).getDrawable();
	}

	@Test
	public void zoomLevelShouldBeTargetLevelWhenExtenderZooms() {
		// Given
		float targetLevel = 7.0f;
		float zoomX = 0.0f;
		float zoomY = 0.0f;
		ImageView imageView = new ImageView(ApplicationProvider.getApplicationContext());
		imageView.setImageBitmap(createTestBitmap());
		ZoomableComponent extender = new ImageViewZoomExtender(imageView);
		// When
		extender.zoomTo(targetLevel, zoomX, zoomY);
		// Then
		assertEquals(extender.getZoomLevel(), targetLevel, 0.0);
	}

	private static Bitmap createTestBitmap() {
		int width = 300;
		int height = 300;
		Bitmap.Config config = Bitmap.Config.ARGB_8888;
		return Bitmap.createBitmap(width, height, config );
	}	
}
