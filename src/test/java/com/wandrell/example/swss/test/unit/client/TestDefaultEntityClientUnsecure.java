/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2015 the original author or authors.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.wandrell.example.swss.test.unit.client;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.ws.test.client.MockWebServiceServer;
import org.springframework.ws.test.client.RequestMatcher;
import org.springframework.ws.test.client.RequestMatchers;
import org.springframework.ws.test.client.ResponseCreator;
import org.springframework.ws.test.client.ResponseCreators;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.wandrell.example.swss.client.DefaultEntityClient;
import com.wandrell.example.swss.model.ExampleEntity;
import com.wandrell.example.swss.test.util.config.context.ClientWss4jContextPaths;
import com.wandrell.example.swss.test.util.config.properties.SoapPropertiesPaths;
import com.wandrell.example.swss.test.util.test.unit.client.AbstractTestEntityClient;

/**
 * Unit tests for {@link DefaultEntityClient} checking that the client works as
 * expected when not using any security.
 * <p>
 * Checks the following cases:
 * <ol>
 * <li>The client parses correctly formed SOAP messages.</li>
 * <li>The client can handle incorrectly formed SOAP messages.</li>
 * </ol>
 *
 * @author Bernardo Mart&iacute;nez Garrido
 */
@ContextConfiguration(locations = { ClientWss4jContextPaths.UNSECURE })
@TestPropertySource({ SoapPropertiesPaths.UNSECURE })
public final class TestDefaultEntityClientUnsecure extends AbstractTestEntityClient {

	/**
	 * The client being tested.
	 */
	@Autowired
	private DefaultEntityClient client;

	/**
	 * Expected id for the returned entity.
	 */
	@Value("${entity.id}")
	private Integer entityId;

	/**
	 * Expected name for the returned entity.
	 */
	@Value("${entity.name}")
	private String entityName;

	/**
	 * Path to XSD file which validates the SOAP messages.
	 */
	@Value("${xsd.entity.path}")
	private String entityXsdPath;

	/**
	 * Path to the file with the invalid response payload.
	 */
	@Value("${soap.response.payload.invalid.path}")
	private String responsePayloadInvalidPath;

	/**
	 * Path to the file with the valid response payload.
	 */
	@Value("${soap.response.payload.path}")
	private String responsePayloadPath;

	/**
	 * Constructs a {@code TestEntityClientUnsecure}.
	 */
	public TestDefaultEntityClientUnsecure() {
		super();
	}

	/**
	 * Tests that the client can handle incorrectly formed SOAP messages.
	 *
	 * @throws IOException
	 *             if there is any problem loading the entity schema file
	 */
	@Test
	public final void testClient_Payload_Invalid() throws IOException {
		final MockWebServiceServer mockServer; // Mocked server
		final RequestMatcher requestMatcher; // Matcher for the request
		final ResponseCreator responseCreator; // Creator for the response
		final ExampleEntity result; // Queried entity

		// Creates the request matcher
		requestMatcher = RequestMatchers.validPayload(new ClassPathResource(entityXsdPath));

		// Creates the response
		responseCreator = ResponseCreators.withPayload(new ClassPathResource(responsePayloadInvalidPath));

		// Creates the server mock
		mockServer = MockWebServiceServer.createServer(client);
		mockServer.expect(requestMatcher).andRespond(responseCreator);

		// Calls the server mock
		result = client.getEntity("http:somewhere.com", entityId);

		Assert.assertEquals(result.getId(), new Integer(-1));
		Assert.assertEquals(result.getName(), "");

		mockServer.verify();
	}

	/**
	 * Tests that the client parses correctly formed SOAP messages.
	 *
	 * @throws IOException
	 *             if there is any problem loading the entity schema file
	 */
	@Test
	public final void testClient_Payload_Valid() throws IOException {
		final MockWebServiceServer mockServer; // Mocked server
		final RequestMatcher requestMatcher; // Matcher for the request
		final ResponseCreator responseCreator; // Creator for the response
		final ExampleEntity result; // Queried entity

		// Creates the request matcher
		requestMatcher = RequestMatchers.validPayload(new ClassPathResource(entityXsdPath));

		// Creates the response
		responseCreator = ResponseCreators.withPayload(new ClassPathResource(responsePayloadPath));

		// Creates the server mock
		mockServer = MockWebServiceServer.createServer(client);
		mockServer.expect(requestMatcher).andRespond(responseCreator);

		// Calls the server mock
		result = client.getEntity("http:somewhere.com", entityId);

		Assert.assertEquals(result.getId(), entityId);
		Assert.assertEquals(result.getName(), entityName);

		mockServer.verify();
	}

}
