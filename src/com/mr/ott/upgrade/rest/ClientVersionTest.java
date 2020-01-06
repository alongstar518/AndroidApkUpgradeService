package com.mr.ott.upgrade.rest;

import static org.junit.Assert.*;

import org.junit.Test;

public class ClientVersionTest {

	@Test
	public void testClientVersion() {
		ClientVersion cv = new ClientVersion();
		assertNotNull(cv);
	}
	
	@Test
	public void testClientVersionStringString() {
		ClientVersion cv = new ClientVersion("1", "2");
		assertNotNull(cv);
	}

}
