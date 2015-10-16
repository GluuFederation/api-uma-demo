package com.uma.server.domain.controller;


import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.core.ResourceMethod;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.Failure;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.interception.PreProcessInterceptor;
import org.xdi.oxauth.client.uma.RptStatusService;
import org.xdi.oxauth.client.uma.UmaClientFactory;
import org.xdi.oxauth.model.uma.RptStatusRequest;
import org.xdi.oxauth.model.uma.RptStatusResponse;
import org.xdi.uma.demo.common.server.CommonUtils;
import org.xdi.uma.demo.common.server.Configuration;

import com.uma.server.common.Common;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class RptPreProcessInterceptor.
 *
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 17/05/2013
 */
@Provider
@ServerInterceptor
public class RptRequester implements PreProcessInterceptor {

    /** The Constant LOG. */
    private static final Logger LOG = Logger.getLogger(RptRequester.class);

    /** The Constant RPT_STATUS_ATTR_NAME. */
    public static final String RPT_STATUS_ATTR_NAME = "rptStatus";

    /* (non-Javadoc)
     * @see org.jboss.resteasy.spi.interception.PreProcessInterceptor#preProcess(org.jboss.resteasy.spi.HttpRequest, org.jboss.resteasy.core.ResourceMethod)
     */
    @Override
    public ServerResponse preProcess(HttpRequest request, ResourceMethod method) throws Failure, WebApplicationException {
        try {
            final HttpHeaders httpHeaders = request.getHttpHeaders();
            if (httpHeaders != null) {
                final List<String> authHeaders = httpHeaders.getRequestHeader("Authorization");
                if (authHeaders != null && !authHeaders.isEmpty()) {
                    final String authorization = authHeaders.get(0);
                    final String token = Common.getTokenFromAuthorization(authorization);
                    if (StringUtils.isNotBlank(token)) {
                        LOG.info("Check if RPT is present in the system or not");
                        final RptStatusResponse status = requestRptStatus(token);
                        if (status != null && status.getActive()) {
                            request.setAttribute(RPT_STATUS_ATTR_NAME, status);
                            return null;
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return (ServerResponse) Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        return (ServerResponse) Common.unauthorizedResponse();
    }

    /**
     * Request rpt status.
     *
     * @param p_rpt the p_rpt
     * @return the rpt status response
     */
    public static RptStatusResponse requestRptStatus(String p_rpt) {
        if (StringUtils.isNotBlank(p_rpt)) {
            final RptStatusRequest request = new RptStatusRequest();
            request.setRpt(p_rpt);

            LOG.debug("Request RPT status...");
            final RptStatusService rptStatusService = UmaClientFactory.instance().createRptStatusService(CommonUtils.getAmConfiguration());
            final RptStatusResponse status = rptStatusService.requestRptStatus("Bearer " + Common.getPat().getAccessToken(), request);
            if (status != null) {
                LOG.debug("RPT status: " + CommonUtils.asJsonSilently(status));
                return status;
            } else {
                LOG.debug("Unable to retrieve RPT status from AM.");
            }
        }
        return null;
    }
}
