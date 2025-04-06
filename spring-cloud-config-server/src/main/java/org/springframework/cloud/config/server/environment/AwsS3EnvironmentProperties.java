/*
 * Copyright 2013-2019 the original author or authors.
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

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Clay McCoy
 */
@ConfigurationProperties("spring.cloud.config.server.awss3")
public class AwsS3EnvironmentProperties extends EnvironmentProperties {

	/**
	 * Name of the S3 bucket that contains config.
	 */
	private String bucket;

	/**
	 * Use application name as intermediate directory. Analogous to `searchPaths:
	 * {application}` from Git backend.
	 */
	private boolean useDirectoryLayout;

	public String getBucket() {
		return bucket;
	}

	public void setBucket(String bucket) {
		this.bucket = bucket;
	}

	public boolean isUseDirectoryLayout() {
		return useDirectoryLayout;
	}

	public void setUseDirectoryLayout(boolean useDirectoryLayout) {
		this.useDirectoryLayout = useDirectoryLayout;
	}

}
