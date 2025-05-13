/*
 * Copyright 2024-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.cloud.ai.graph.state.strategy;

import com.alibaba.cloud.ai.graph.KeyStrategy;

/**
 * ReplaceStrategy is a KeyStrategy that replaces the old value with the new value.
 *
 * @author <a href="mailto:<EMAIL>">Mercy</a>
 * @since 0.2.0
 */
public class ReplaceStrategy implements KeyStrategy {

	@Override
	public Object apply(Object oldValue, Object newValue) {
		return newValue;
	}

}
