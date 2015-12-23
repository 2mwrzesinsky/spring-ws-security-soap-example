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

package com.wandrell.example.swss.testing.util.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.ws.soap.client.SoapFaultClientException;
import org.testng.annotations.Test;

import com.wandrell.example.swss.client.EntityClient;

/**
 * Integration tests for {@link EntityClient} testing that it generates a fault
 * when the message is invalid.
 * <p>
 * Checks the following cases:
 * <ol>
 * <li>An invalid message causes a {@code SoapFaultClientException} to be
 * thrown.</li>
 * </ol>
 * <p>
 * Pay attention to the fact that it requires the WS to be running.
 *
 * @author Bernardo Martínez Garrido
 */
public abstract class AbstractITEntityClientInvalid extends
        AbstractTestNGSpringContextTests {

    /**
     * Client being tested.
     */
    @Autowired
    private EntityClient client;

    /**
     * Default constructor.
     */
    public AbstractITEntityClientInvalid() {
        super();
    }

    /**
     * Tests that an invalid message causes a {@code SoapFaultClientException}
     * to be thrown.
     */
    @Test(expectedExceptions = SoapFaultClientException.class)
    public final void testEndpoint_InvalidPassword_Exception() {
        client.getEntity(1);
    }

}
