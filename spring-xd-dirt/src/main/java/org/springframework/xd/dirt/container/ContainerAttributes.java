/*
 * Copyright 2013-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.xd.dirt.container;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Metadata for a Container instance.
 *
 * @author Mark Fisher
 * @author David Turanski
 */
public class ContainerAttributes extends LinkedHashMap<String, String> {

	public final static String CONTAINER_ID_KEY = "id";

	public final static String HOST_KEY = "host";

	public final static String PROCESS_ID_KEY = "pid";

	public final static String GROUPS_KEY = "groups";

	public final static String IP_ADDRESS_KEY = "ip";

	private final List<String> commonAttributeKeys = Arrays.asList(new String[] { CONTAINER_ID_KEY, PROCESS_ID_KEY,
		HOST_KEY,
		IP_ADDRESS_KEY, GROUPS_KEY });

	/**
	 * Default constructor generates a random id.
	 */
	public ContainerAttributes() {
		this(UUID.randomUUID().toString());
	}

	/**
	 * Constructor to be called when the id is already known
	 *
	 * @param id the container's id
	 */
	public ContainerAttributes(String id) {
		Assert.hasText(id, "id is required");
		this.put(CONTAINER_ID_KEY, id);
	}

	public ContainerAttributes(Map<? extends String, ? extends String> attributes) {
		this.putAll(attributes);
	}


	public String getId() {
		return this.get(CONTAINER_ID_KEY);
	}

	public String getHost() {
		return this.get(HOST_KEY);
	}

	public String getIp() {
		return this.get(IP_ADDRESS_KEY);
	}

	public int getPid() {
		return Integer.parseInt(this.get(PROCESS_ID_KEY));
	}

	public Set<String> getGroups() {
		Set<String> groupSet = new HashSet<String>();
		String groups = this.get(GROUPS_KEY);
		groupSet = StringUtils.hasText(groups) ? StringUtils.commaDelimitedListToSet(groups) : new HashSet<String>();
		return Collections.unmodifiableSet(groupSet);
	}

	public ContainerAttributes setPid(Integer pid) {
		this.put(PROCESS_ID_KEY, String.valueOf(pid));
		return this;
	}

	public ContainerAttributes setHost(String host) {
		this.put(HOST_KEY, host);
		return this;
	}


	public ContainerAttributes setIp(String ip) {
		this.put(IP_ADDRESS_KEY, ip);
		return this;
	}

	public Map<String, String> getCustomAttributes() {

		Map<String, String> customAttributes = new HashMap<String, String>();
		for (String key : this.keySet()) {
			if (!commonAttributeKeys.contains(key)) {
				customAttributes.put(key, this.get(key));
			}
		}
		return customAttributes;
	}
}
