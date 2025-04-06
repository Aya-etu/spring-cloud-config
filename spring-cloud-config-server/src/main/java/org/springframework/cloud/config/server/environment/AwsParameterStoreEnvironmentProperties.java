/*
 * Copyright 2018-2020 the original author or authors.
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

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * @author Iulian Antohe
 */
@Validated
@ConfigurationProperties("spring.cloud.config.server.awsparamstore")
public class AwsParameterStoreEnvironmentProperties extends EnvironmentProperties {

	static final String DEFAULT_PATH_SEPARATOR = "/";

	private static final String DEFAULT_ORIGIN = "aws:ssm:parameter:";

	private static final String DEFAULT_PROFILE_SEPARATOR = "-";

	private static final String DEFAULT_PREFIX = DEFAULT_PATH_SEPARATOR + "config";

	/**
	 * Prefix indicating the property's origin. Defaults to "aws:ssm:parameter:".
	 */
	@NotNull
	private String origin = DEFAULT_ORIGIN;

	/**
	 * Prefix indicating first level for every property loaded from the AWS Parameter
	 * Store. Value must start with a forward slash followed by one or more valid path
	 * segments or be empty. Defaults to "/config".
	 */
	@NotNull
	@Pattern(regexp = "(/[a-zA-Z0-9.\\-_]+)*")
	private String prefix = DEFAULT_PREFIX;

	/**
	 * String that separates an appended profile from the context name. Note that an AWS
	 * parameter name can only contain dots, dashes and underscores next to alphanumeric
	 * characters. Defaults to "-".
	 */
	@NotBlank
	@Pattern(regexp = "[a-zA-Z0-9.\\-_/]+")
	private String profileSeparator = DEFAULT_PROFILE_SEPARATOR;

	/**
	 * Flag to indicate the retrieval of all AWS parameters within a hierarchy. Defaults
	 * to "true".
	 */
	private boolean recursive = true;

	/**
	 * Flag to indicate the retrieval of all AWS parameters in a hierarchy with their
	 * value decrypted. Defaults to "true".
	 */
	private boolean decryptValues = true;

	/**
	 * The maximum number of items to return for an AWS Parameter Store API call. Defaults
	 * to "10".
	 */
	@Min(1)
	@Max(10)
	private int maxResults = 10;

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public boolean isRecursive() {
		return recursive;
	}

	public void setRecursive(boolean recursive) {
		this.recursive = recursive;
	}

	public boolean isDecryptValues() {
		return decryptValues;
	}

	public void setDecryptValues(boolean decryptValues) {
		this.decryptValues = decryptValues;
	}

	public int getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

}
