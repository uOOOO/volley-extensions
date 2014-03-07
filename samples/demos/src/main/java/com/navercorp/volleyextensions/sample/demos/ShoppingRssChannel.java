/*
 * Copyright (C) 2014 Naver Business Platform Corp.
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
package com.navercorp.volleyextensions.sample.demos;

import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class ShoppingRssChannel {
	@Element
	private String title;
	@Element
	private String link;
	@Element(required = false)
	private String description;
	@Element
	private String lastBuildDate;
	@Element
	private long total;
	@Element
	private int start;
	@Element
	private int display;

	@ElementList(inline=true)
	private List<ShoppingItem> items;

	public List<ShoppingItem> getShoppingItems() {
		return items;
	}
}