/*
 * Copyright 2013-2021 the original author or authors.
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

package org.springframework.cloud.config.server.environment;

import io.micrometer.common.KeyValue;
import io.micrometer.common.KeyValues;
import io.micrometer.common.docs.KeyName;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationConvention;

import org.springframework.util.StringUtils;

/**
 * Default provider of key values for {@link ObservationEnvironmentRepositoryContext}.
 *
 * @author Marcin Grzejszczak
 * @since 4.0.0
 */
class ObservationEnvironmentRepositoryObservationConvention
		implements ObservationConvention<ObservationEnvironmentRepositoryContext> {

	@Override
	public KeyValues getLowCardinalityKeyValues(ObservationEnvironmentRepositoryContext context) {
		KeyValues keyValues = KeyValues.empty();
		keyValues = appendWithValueOrUseDefault(keyValues,
				DocumentedConfigObservation.LowCardinalityTags.ENVIRONMENT_CLASS,
				context.getEnvironmentRepositoryClass().getName());
		keyValues = appendWithValueOrUseDefault(keyValues, DocumentedConfigObservation.LowCardinalityTags.LABEL,
				context.getLabel());
		keyValues = appendWithValueOrUseDefault(keyValues, DocumentedConfigObservation.LowCardinalityTags.PROFILE,
				context.getProfile());
		return appendWithValueOrUseDefault(keyValues, DocumentedConfigObservation.LowCardinalityTags.APPLICATION,
				context.getApplication());
	}

	private KeyValues appendWithValueOrUseDefault(KeyValues keyValues, KeyName keyName, String value) {
		value = StringUtils.hasText(value) ? value : KeyValue.NONE_VALUE;
		return keyValues.and(keyName.withValue(value));
	}

	@Override
	public boolean supportsContext(Observation.Context context) {
		return context instanceof ObservationEnvironmentRepositoryContext;
	}

	@Override
	public String getName() {
		return "spring.cloud.config.environment.find";
	}

	@Override
	public String getContextualName(ObservationEnvironmentRepositoryContext context) {
		return "env find";
	}

}
