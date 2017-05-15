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

import static java.util.Arrays.asList;
import static org.junit.Assert.*;
import static org.springframework.util.CollectionUtils.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.util.MultiValueMap;

/**
 * @author Jens Schauder
 */
public class MultiValueMapTests {

	@Test
	public void putAllReplacesAllValuesForKeysPresentInTheSecondMap() {

		// should not be a surprise, because it is the putAll of a Map<K, List<V>>
		MultiValueMap<String, String> originalMap = toMultiValueMap(new HashMap<>());
		MultiValueMap<String, String> secondMap = toMultiValueMap(new HashMap<>());

		originalMap.add("a", "A");
		originalMap.add("c", "C");

		secondMap.add("a", "AA");
		secondMap.add("a", "a");

		secondMap.add("b", "B");

		originalMap.putAll(secondMap);

		assertEquals(2, originalMap.get("a").size());
		assertEquals(1, originalMap.get("b").size());
		assertEquals(1, originalMap.get("c").size());

		assertFalse(originalMap.get("a").contains("A"));
	}

	@Test
	public void addAllReplacesAllValuesForKeysPresentInTheSecondMap() {

		// should not be a surprise, because it is the putAll of a Map<K, List<V>>
		MultiValueMap<String, String> originalMap = toMultiValueMap(new HashMap<>());

		originalMap.add("a", "A");
		originalMap.add("c", "C");

		originalMap.addAll("a", asList("A", "a"));

		assertEquals(3, originalMap.get("a").size());
		assertEquals(1, originalMap.get("c").size());

		assertTrue(originalMap.get("a").contains("A"));
	}


	@Test
	public void setAllSetsTheValueOfTheKeysPresentInTheArgumentMapToSingleElementLists() {

		MultiValueMap<String, String> originalMap = toMultiValueMap(new HashMap<>());
		Map<String, String> secondMap = new HashMap<>();

		originalMap.add("a", "A");
		originalMap.add("c", "C");

		secondMap.put("a", "a");

		secondMap.put("b", "b");

		originalMap.setAll(secondMap);

		assertEquals(1, originalMap.get("a").size());
		assertEquals(1, originalMap.get("b").size());
		assertEquals(1, originalMap.get("c").size());

		assertFalse(originalMap.get("a").contains("A"));
	}

}
