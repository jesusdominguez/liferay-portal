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

package com.liferay.portal.monitoring.statistics.portlet;

import com.liferay.portal.kernel.monitoring.MonitoringException;
import com.liferay.portal.kernel.monitoring.statistics.DataSampleProcessor;
import com.liferay.portal.kernel.monitoring.statistics.RequestStatistics;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.CompanyConstants;
import com.liferay.portal.service.CompanyLocalService;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Karthik Sudarshan
 * @author Michael C. Han
 * @author Brian Wing Shun Chan
 */
public class CompanyStatistics
	implements DataSampleProcessor<PortletRequestDataSample> {

	public CompanyStatistics() {
		_companyId = CompanyConstants.SYSTEM;
		_webId = CompanyConstants.SYSTEM_STRING;
	}

	public CompanyStatistics(
		CompanyLocalService companyLocalService, String webId) {

		try {
			Company company = companyLocalService.getCompanyByWebId(webId);

			_companyId = company.getCompanyId();
			_webId = webId;
		}
		catch (Exception e) {
			throw new IllegalStateException(
				"Unable to get company with web id " + webId, e);
		}
	}

	public RequestStatistics getActionRequestStatistics(String portletId)
		throws MonitoringException {

		PortletStatistics portletStatistics = _portletStatisticsByPortletId.get(
			portletId);

		if (portletStatistics == null) {
			throw new MonitoringException(
				"No statistics for portlet id " + portletId);
		}

		return portletStatistics.getActionRequestStatistics();
	}

	public Set<RequestStatistics> getActionRequestStatisticsSet() {
		Set<RequestStatistics> actionStatisticsSet =
			new HashSet<RequestStatistics>();

		for (PortletStatistics portletStatistics :
				_portletStatisticsByPortletId.values()) {

			actionStatisticsSet.add(
				portletStatistics.getActionRequestStatistics());
		}

		return actionStatisticsSet;
	}

	public long getCompanyId() {
		return _companyId;
	}

	public RequestStatistics getEventRequestStatistics(String portletId)
		throws MonitoringException {

		PortletStatistics portletStatistics = _portletStatisticsByPortletId.get(
			portletId);

		if (portletStatistics == null) {
			throw new MonitoringException(
				"No statistics for portlet id " + portletId);
		}

		return portletStatistics.getEventRequestStatistics();
	}

	public Set<RequestStatistics> getEventRequestStatisticsSet() {
		Set<RequestStatistics> eventStatisticsSet =
			new HashSet<RequestStatistics>();

		for (PortletStatistics portletStatistics :
				_portletStatisticsByPortletId.values()) {

			eventStatisticsSet.add(
				portletStatistics.getEventRequestStatistics());
		}

		return eventStatisticsSet;
	}

	public long getMaxTime() {
		return _maxTime;
	}

	public long getMinTime() {
		return _minTime;
	}

	public Collection<String> getPortletIds() {
		return _portletStatisticsByPortletId.keySet();
	}

	public RequestStatistics getRenderRequestStatistics(String portletId)
		throws MonitoringException {

		PortletStatistics portletStatistics = _portletStatisticsByPortletId.get(
			portletId);

		if (portletStatistics == null) {
			throw new MonitoringException(
				"No statistics for portlet id " + portletId);
		}

		return portletStatistics.getRenderRequestStatistics();
	}

	public Set<RequestStatistics> getRenderRequestStatisticsSet() {
		Set<RequestStatistics> renderStatisticsSet =
			new HashSet<RequestStatistics>();

		for (PortletStatistics portletStatistics :
				_portletStatisticsByPortletId.values()) {

			renderStatisticsSet.add(
				portletStatistics.getRenderRequestStatistics());
		}

		return renderStatisticsSet;
	}

	public RequestStatistics getResourceRequestStatistics(String portletId)
		throws MonitoringException {

		PortletStatistics portletStatistics = _portletStatisticsByPortletId.get(
			portletId);

		if (portletStatistics == null) {
			throw new MonitoringException(
				"No statistics for portlet id " + portletId);
		}

		return portletStatistics.getResourceRequestStatistics();
	}

	public Set<RequestStatistics> getResourceRequestStatisticsSet() {
		Set<RequestStatistics> resourceStatisticsSet =
			new HashSet<RequestStatistics>();

		for (PortletStatistics portletStatistics :
				_portletStatisticsByPortletId.values()) {

			resourceStatisticsSet.add(
				portletStatistics.getResourceRequestStatistics());
		}

		return resourceStatisticsSet;
	}

	public String getWebId() {
		return _webId;
	}

	public void processDataSample(
			PortletRequestDataSample portletRequestDataSample)
		throws MonitoringException {

		if (portletRequestDataSample.getCompanyId() != _companyId) {
			return;
		}

		String portletId = portletRequestDataSample.getPortletId();

		PortletStatistics portletStatistics = _portletStatisticsByPortletId.get(
			portletId);

		if (portletStatistics == null) {
			portletStatistics = new PortletStatistics(
				portletId, portletRequestDataSample.getName(),
				portletRequestDataSample.getDisplayName());

			_portletStatisticsByPortletId.put(portletId, portletStatistics);
		}

		portletStatistics.processDataSample(portletRequestDataSample);

		long duration = portletRequestDataSample.getDuration();

		if (_maxTime < duration) {
			_maxTime = duration;
		}
		else if (_minTime > duration) {
			_minTime = duration;
		}
	}

	public void reset() {
		_maxTime = 0;
		_minTime = 0;

		for (PortletStatistics portletStatistics :
				_portletStatisticsByPortletId.values()) {

			portletStatistics.reset();
		}
	}

	private long _companyId;
	private long _maxTime;
	private long _minTime;
	private Map<String, PortletStatistics> _portletStatisticsByPortletId =
		new ConcurrentHashMap<String, PortletStatistics>();
	private String _webId;

}