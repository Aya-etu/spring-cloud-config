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

package org.springframework.cloud.config.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.config.environment.EnvironmentMediaType;
import org.springframework.cloud.configuration.TlsProperties;
import org.springframework.core.env.Environment;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author Dave Syer
 *
 */
@ConfigurationProperties(ConfigClientProperties.PREFIX)
public class ConfigClientProperties {

	private static final int SECONDS_IN_ONE_MINUTE = 60;

	private static final int MILLISECONDS_IN_ONE_SECOND = 1000;

	private static final int READ_TIMEOUT_MINUTES = 3;

	private static final int READ_TIMEOUT_SECONDS = 5;

	private static final int CONNECT_TIMEOUT_SECONDS = 10;

	/**
	 * Prefix for Spring Cloud Config properties.
	 */
	public static final String PREFIX = "spring.cloud.config";

	/**
	 * Default application name.
	 */
	public static final String DEFAULT_APPLICATION = "application";

	/**
	 * Placeholder string that allows ${spring.cloud.config.name} to override
	 * ${spring.application.name:application}.
	 */
	public static final String NAME_PLACEHOLDER = "${" + ConfigClientProperties.PREFIX
			+ ".name:${spring.application.name:" + DEFAULT_APPLICATION + "}}";

	/**
	 * Name of config discovery enabled property.
	 */
	public static final String CONFIG_DISCOVERY_ENABLED = PREFIX + ".discovery.enabled";

	/**
	 * Token header name.
	 */
	public static final String TOKEN_HEADER = "X-Config-Token";

	/**
	 * State header name.
	 */
	public static final String STATE_HEADER = "X-Config-State";

	/**
	 * Authorization header name.
	 */
	public static final String AUTHORIZATION = "authorization";

	/**
	 * Default profile value.
	 */
	public static final String DEFAULT_PROFILE = "default";

	/**
	 * Flag to say that remote configuration is enabled. Default true;
	 */
	private boolean enabled = true;

	/**
	 * The default profile to use when fetching remote configuration (comma-separated).
	 * Default is "default".
	 */
	private String profile = DEFAULT_PROFILE;

	/**
	 * Name of application used to fetch remote properties.
	 */
	@Value("${spring.application.name:" + DEFAULT_APPLICATION + "}")
	private String name;

	/**
	 * The label name to use to pull remote configuration properties. The default is set
	 * on the server (generally "main" for a git based server).
	 */
	private String label;

	/**
	 * The username to use (HTTP Basic) when contacting the remote server.
	 */
	private String username;

	/**
	 * The password to use (HTTP Basic) when contacting the remote server.
	 */
	private String password;

	/**
	 * The URI of the remote server (default http://localhost:8888).
	 */
	private String[] uri = { "http://localhost:8888" };

	/**
	 * The strategy to use when call to server fails and there are multiple URLs
	 * configured on the uri property (default {@link MultipleUriStrategy#ALWAYS}).
	 */
	private MultipleUriStrategy multipleUriStrategy = MultipleUriStrategy.ALWAYS;

	/**
	 * The Accept header media type to send to config server.
	 */
	private String mediaType = EnvironmentMediaType.V2_JSON;

	/**
	 * The charset to read the resource from the config server.
	 */
	private Charset charset = StandardCharsets.UTF_8;

	/**
	 * Discovery properties.
	 */
	private Discovery discovery = new Discovery();

	/**
	 * TLS properties.
	 */
	private TlsProperties tls = new TlsProperties();

	/**
	 * Flag to indicate that failure to connect to the server is fatal (default false).
	 */
	private boolean failFast = false;

	/**
	 * Security Token passed thru to underlying environment repository.
	 */
	private String token;

	/**
	 * timeout on waiting to read data from the Config Server.
	 */
	private int requestReadTimeout = (SECONDS_IN_ONE_MINUTE * MILLISECONDS_IN_ONE_SECOND * READ_TIMEOUT_MINUTES)
			+ READ_TIMEOUT_SECONDS * MILLISECONDS_IN_ONE_SECOND;

	/**
	 * timeout on waiting to connect to the Config Server.
	 */
	private int requestConnectTimeout = MILLISECONDS_IN_ONE_SECOND * CONNECT_TIMEOUT_SECONDS;

	/**
	 * Flag to indicate whether to send state. Default true.
	 */
	private boolean sendState = true;

	/**
	 * Additional headers used to create the client request.
	 */
	private Map<String, String> headers = new HashMap<>();

	/**
	 * If set to true the client will send all labels to the server instead of sending one
	 * at a time. Support for this would require a config server version of 4.2.0 or
	 * higher.
	 */
	private boolean sendAllLabels = false;

	ConfigClientProperties() {
	}

	public ConfigClientProperties(Environment environment) {
		String[] profiles = environment.getActiveProfiles();
		if (profiles.length == 0) {
			profiles = environment.getDefaultProfiles();
		}
		this.setProfile(StringUtils.arrayToCommaDelimitedString(profiles));
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String[] getUri() {
		return this.uri;
	}

	public void setUri(String[] url) {
		this.uri = url;
	}

	public MultipleUriStrategy getMultipleUriStrategy() {
		return multipleUriStrategy;
	}

	public void setMultipleUriStrategy(MultipleUriStrategy multipleUriStrategy) {
		this.multipleUriStrategy = multipleUriStrategy;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProfile() {
		return this.profile;
	}

	public void setProfile(String env) {
		this.profile = env;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Credentials getCredentials(int index) {
		return extractCredentials(index);
	}

	public String getMediaType() {
		return this.mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public Charset getCharset() {
		return charset;
	}

	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	public Discovery getDiscovery() {
		return this.discovery;
	}

	public void setDiscovery(Discovery discovery) {
		this.discovery = discovery;
	}

	public TlsProperties getTls() {
		return tls;
	}

	public void setTls(TlsProperties tls) {
		this.tls = tls;
	}

	public boolean isFailFast() {
		return this.failFast;
	}

	public void setFailFast(boolean failFast) {
		this.failFast = failFast;
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getRequestReadTimeout() {
		return this.requestReadTimeout;
	}

	public void setRequestReadTimeout(int requestReadTimeout) {
		this.requestReadTimeout = requestReadTimeout;
	}

	public int getRequestConnectTimeout() {
		return this.requestConnectTimeout;
	}

	public void setRequestConnectTimeout(int requestConnectTimeout) {
		this.requestConnectTimeout = requestConnectTimeout;
	}

	public boolean isSendState() {
		return this.sendState;
	}

	public void setSendState(boolean sendState) {
		this.sendState = sendState;
	}

	public Map<String, String> getHeaders() {
		return this.headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public boolean isSendAllLabels() {
		return sendAllLabels;
	}

	public void setSendAllLabels(boolean sendAllLabels) {
		this.sendAllLabels = sendAllLabels;
	}

	private Credentials extractCredentials(int index) {
		Credentials result = new Credentials();
		int noOfUrl = this.uri.length;
		if (index < 0 || index >= noOfUrl) {
			throw new IllegalStateException("Trying to access an invalid array index");
		}
		String uri = this.uri[index];
		result.uri = uri;
		Credentials explicitCredentials = getUsernamePassword();
		result.username = explicitCredentials.username;
		result.password = explicitCredentials.password;
		try {
			URL url = new URL(uri);
			String userInfo = url.getUserInfo();
			// no credentials in url, return explicit credentials
			if (ObjectUtils.isEmpty(userInfo) || ":".equals(userInfo)) {
				return result;
			}
			result.uri = UriComponentsBuilder.fromUriString(uri, UriComponentsBuilder.ParserType.WHAT_WG)
				.userInfo(null)
				.build()
				.toUriString();

			// if userInfo does not contain a :, then append a : to it
			if (!userInfo.contains(":")) {
				userInfo = userInfo + ":";
			}

			int sepIndex = userInfo.indexOf(":");
			// set username and password from uri
			result.username = userInfo.substring(0, sepIndex);
			result.password = userInfo.substring(sepIndex + 1);

			// override password if explicitly set
			if (explicitCredentials.password != null) {
				// Explicit username / password takes precedence
				result.password = explicitCredentials.password;
			}
			// override username if explicitly set
			if (!"user".equals(explicitCredentials.username)) {
				// But the username can be overridden
				result.username = explicitCredentials.username;
			}

			result.password = URLDecoder.decode(result.password, StandardCharsets.UTF_8);
			result.username = URLDecoder.decode(result.username, StandardCharsets.UTF_8);
			return result;
		}
		catch (MalformedURLException e) {
			throw new IllegalStateException("Invalid URL: " + uri, e);
		}
	}

	private Credentials getUsernamePassword() {
		Credentials credentials = new Credentials();

		if (StringUtils.hasText(this.password)) {
			credentials.password = this.password.trim();
		}

		if (StringUtils.hasText(this.username)) {
			credentials.username = this.username.trim();
		}
		else {
			credentials.username = "user";
		}
		return credentials;
	}

	public ConfigClientProperties override(org.springframework.core.env.Environment environment) {
		ConfigClientProperties override = new ConfigClientProperties();
		BeanUtils.copyProperties(this, override);
		override.setName(environment.resolvePlaceholders(NAME_PLACEHOLDER));
		if (environment.containsProperty(ConfigClientProperties.PREFIX + ".profile")) {
			override.setProfile(environment.getProperty(ConfigClientProperties.PREFIX + ".profile"));
		}
		if (environment.containsProperty(ConfigClientProperties.PREFIX + ".label")) {
			override.setLabel(environment.getProperty(ConfigClientProperties.PREFIX + ".label"));
		}
		return override;
	}

	@Override
	public String toString() {
		return "ConfigClientProperties [enabled=" + this.enabled + ", profile=" + this.profile + ", name=" + this.name
				+ ", label=" + this.label + ", username=" + this.username + ", password=" + this.password + ", uri="
				+ Arrays.toString(this.uri) + ", mediaType=" + this.mediaType + ", discovery=" + this.discovery
				+ ", failFast=" + this.failFast + ", token=" + this.token + ", requestConnectTimeout="
				+ this.requestConnectTimeout + ", requestReadTimeout=" + this.requestReadTimeout + ", sendState="
				+ this.sendState + ", headers=" + this.headers + ", sendAllLabels=" + this.sendAllLabels + "]";
	}

	/**
	 * Credentials properties.
	 */
	public static class Credentials {

		private String username;

		private String password;

		private String uri;

		public String getUsername() {
			return this.username;
		}

		public String getPassword() {
			return this.password;
		}

		public String getUri() {
			return this.uri;
		}

	}

	/**
	 * Discovery properties.
	 */
	public static class Discovery {

		/**
		 * Default config server service id name.
		 */
		public static final String DEFAULT_CONFIG_SERVER = "configserver";

		/**
		 * Flag to indicate that config server discovery is enabled (config server URL
		 * will be looked up via discovery).
		 */
		private boolean enabled;

		/**
		 * Service id to locate config server.
		 */
		private String serviceId = DEFAULT_CONFIG_SERVER;

		public boolean isEnabled() {
			return this.enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}

		public String getServiceId() {
			return this.serviceId;
		}

		public void setServiceId(String serviceId) {
			this.serviceId = serviceId;
		}

	}

	/**
	 * Enumerates possible strategies to use when multiple URLs are provided and an error
	 * occurs.
	 */
	public enum MultipleUriStrategy {

		/**
		 * Try the next URL in the list on any error.
		 */
		ALWAYS,

		/**
		 * Try the next URL in the list only if no response was received.
		 */
		CONNECTION_TIMEOUT_ONLY

	}

}
