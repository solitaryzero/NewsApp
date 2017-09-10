/*
 * Copyright (C) 2011 Patrik Åkerfeldt
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
package com.java.group37.newsapp.widget;

/**
 * 定义一个接口，FlowIndicator负责显示一个视觉指示器的总数量和当前视图可见视图。
 * 
 */
public interface FlowIndicator extends ViewFlow.ViewSwitchListener {

	/**
	 * 设置当前ViewFlow。这个方法被调用的ViewFlow当FlowIndicator附属于它。
	 * 
	 * @param view
	 */
	public void setViewFlow(ViewFlow view);

	/**
	 * 滚动位置已经被改变了。一个FlowIndicator可能实现这个方法,以反映当前的位置
	 * 
	 * @param h
	 * @param v
	 * @param oldh
	 * @param oldv
	 */
	public void onScrolled(int h, int v, int oldh, int oldv);
}
