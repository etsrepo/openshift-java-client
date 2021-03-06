/******************************************************************************* 
 * Copyright (c) 2013 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package com.openshift.internal.client;

import static org.fest.assertions.Assertions.assertThat;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import com.openshift.client.IHttpClient;
import com.openshift.client.IOpenShiftConnection;
import com.openshift.client.cartridge.EmbeddableCartridge;
import com.openshift.client.cartridge.IStandaloneCartridge;
import com.openshift.client.cartridge.StandaloneCartridge;
import com.openshift.client.utils.CartridgeAssert;
import com.openshift.client.utils.Cartridges;
import com.openshift.client.utils.Samples;
import com.openshift.client.utils.TestConnectionFactory;

/**
 * @author Andre Dietisheim
 */
public class StandaloneCartridgeTest {

	private IOpenShiftConnection connection;

	@Before
	public void setup() throws Throwable {
		IHttpClient client = new HttpClientMockDirector()
				.mockGetCartridges(Samples.GET_CARTRIDGES)
				.client();
		this.connection = new TestConnectionFactory().getConnection(client);
	}

	@Test
	public void shouldNonDownloadableEqualsNonDownloadable() {
		// pre-coniditions
		// operation
		// verification
		assertThat(new StandaloneCartridge("redhat"))
				.isEqualTo(new StandaloneCartridge("redhat"));
		assertThat(new StandaloneCartridge("redhat"))
				.isNotEqualTo(new StandaloneCartridge("jboss"));
	}

	@Test
	public void shouldDownloadableEqualsDownloadable() throws MalformedURLException {
		// pre-coniditions
		// operation
		// verification
		assertThat(new StandaloneCartridge(new URL(Cartridges.GO_DOWNLOAD_URL)))
				.isEqualTo(new StandaloneCartridge(new URL(Cartridges.GO_DOWNLOAD_URL)));
	}

	@Test
	public void shouldDownloadableWithDifferentNameEqualsDownloadable() throws MalformedURLException {
		// pre-coniditions
		// operation
		// verification
		assertThat(new StandaloneCartridge("redhat", new URL(Cartridges.GO_DOWNLOAD_URL)))
				.isEqualTo(new StandaloneCartridge(new URL(Cartridges.GO_DOWNLOAD_URL)));
		// should equal if url is equal, name doesnt matter 
		// (name is updated as soon as cartridge is deployed)
		assertThat(new StandaloneCartridge("jboss", new URL(Cartridges.GO_DOWNLOAD_URL)))
				.isEqualTo(new StandaloneCartridge("redhat", new URL(Cartridges.GO_DOWNLOAD_URL)));
	}

	@Test
	public void shouldDownloadableStandaloneNotEqualsDownloadableEmbeddable() throws MalformedURLException {
		// pre-coniditions
		// operation
		// verification
		assertThat(new StandaloneCartridge(new URL(Cartridges.GO_DOWNLOAD_URL)))
				.isNotEqualTo(new EmbeddableCartridge(new URL(Cartridges.FOREMAN_DOWNLOAD_URL)));
	}
	
	@Test
	public void shouldHaveNameDisplaynameDescription() throws Throwable {
		// pre-condition
		IStandaloneCartridge nodeJs = connection.getStandaloneCartridges().get(0);
		CartridgeAssert<IStandaloneCartridge> cartridgeAssert = new CartridgeAssert<IStandaloneCartridge>(nodeJs);

		// operation
		// verifcation
		cartridgeAssert
				.hasName("nodejs-0.6")
				.hasDisplayName("Node.js 0.6")
				.hasDescription(
						"Node.js is a platform built on Chrome's JavaScript runtime for easily building fast, "
						+ "scalable network applications. Node.js is perfect for data-intensive real-time "
						+ "applications that run across distributed devices.");
	}


}
