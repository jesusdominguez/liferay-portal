/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.security.pacl.checker;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.security.pacl.PACLConstants;
import com.liferay.portal.security.pacl.PACLPolicy;

import java.util.Properties;
import java.util.Set;

/**
 * @author Brian Wing Shun Chan
 * @author Raymond Augé
 */
public abstract class BaseChecker implements Checker, PACLConstants {

	public AuthorizationProperty generateAuthorizationProperty(
		Object... arguments) {

		throw new UnsupportedOperationException();
	}

	public ClassLoader getClassLoader() {
		return _paclPolicy.getClassLoader();
	}

	public PACLPolicy getPACLPolicy() {
		return _paclPolicy;
	}

	public String getServletContextName() {
		return _paclPolicy.getServletContextName();
	}

	public void setPACLPolicy(PACLPolicy paclPolicy) {
		_paclPolicy = paclPolicy;
	}

	protected Properties getProperties() {
		return _paclPolicy.getProperties();
	}

	protected String getProperty(String key) {
		return _paclPolicy.getProperty(key);
	}

	protected String[] getPropertyArray(String key) {
		return _paclPolicy.getPropertyArray(key);
	}

	protected boolean getPropertyBoolean(String key) {
		return _paclPolicy.getPropertyBoolean(key);
	}

	protected Set<String> getPropertySet(String key) {
		return _paclPolicy.getPropertySet(key);
	}

	protected void logSecurityException(Log log, String message) {
		if (log.isWarnEnabled()) {
			log.warn(message);
		}
	}

	private PACLPolicy _paclPolicy;

}