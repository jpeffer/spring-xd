/*
 * Copyright 2013 the original author or authors.
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

package org.springframework.xd.dirt.rest.metrics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.xd.analytics.metrics.core.RichGauge;
import org.springframework.xd.analytics.metrics.core.RichGaugeRepository;
import org.springframework.xd.dirt.analytics.NoSuchMetricException;
import org.springframework.xd.rest.client.domain.metrics.MetricResource;
import org.springframework.xd.rest.client.domain.metrics.RichGaugeResource;

/**
 * Exposes representations of {@link org.springframework.xd.analytics.metrics.core.Gauge}s.
 * 
 * @author Luke Taylor
 */
@Controller
@RequestMapping("/metrics/rich-gauges")
@ExposesResourceFor(RichGaugeResource.class)
public class RichGaugesController extends AbstractMetricsController<RichGaugeRepository, RichGauge> {

	private final DeepRichGaugeResourceAssembler gaugeResourceAssembler = new DeepRichGaugeResourceAssembler();

	@Autowired
	public RichGaugesController(RichGaugeRepository repository) {
		super(repository);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.GET)
	public PagedResources<MetricResource> list(Pageable pageable,
			PagedResourcesAssembler<RichGauge> pagedAssembler) {
		return super.list(pageable, pagedAssembler);
	}

	@ResponseBody
	@RequestMapping(value = "/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public RichGaugeResource display(@PathVariable("name") String name) {
		RichGauge g = repository.findOne(name);
		if (g == null) {
			throw new NoSuchMetricException(name, "There is no rich gauge named '%s'");
		}
		return gaugeResourceAssembler.toResource(g);
	}
}
