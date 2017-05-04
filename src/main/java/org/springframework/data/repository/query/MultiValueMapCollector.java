/*
 * Copyright 2017 the original author or authors.
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
package org.springframework.data.repository.query;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;

/**
 * @author Jens Schauder
 */
public class MultiValueMapCollector<T, K, V> implements Collector<T, MultiValueMap<K, V>, MultiValueMap<K, V>> {

	private final Function<T, K> keyFunction;
	private final Function<T, V> valueFunction;

	public MultiValueMapCollector(Function<T, K> keyFunction, Function<T, V> valueFunction) {
		this.keyFunction = keyFunction;
		this.valueFunction = valueFunction;
	}

	@Override
	public Supplier<MultiValueMap<K, V>> supplier() {
		return () -> CollectionUtils.toMultiValueMap(new HashMap<>());
	}

	@Override
	public BiConsumer<MultiValueMap<K, V>, T> accumulator() {
		return (map, t) -> map.add(keyFunction.apply(t), valueFunction.apply(t));
	}

	@Override
	public BinaryOperator<MultiValueMap<K, V>> combiner() {

		return (map1,map2) -> {

			for (K key : map2.keySet()) {
				map1.addAll(key, map2.get(key));
			}
			return map1;
		};
	}

	@Override
	public Function<MultiValueMap<K, V>, MultiValueMap<K, V>> finisher() {
		return it -> it;
	}

	@Override
	public Set<Characteristics> characteristics() {
		return EnumSet.of(Characteristics.IDENTITY_FINISH, Characteristics.UNORDERED);
	}
}
