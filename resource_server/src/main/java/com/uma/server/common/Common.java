package com.uma.server.common;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.xdi.oxauth.client.uma.wrapper.UmaClient;
import org.xdi.oxauth.model.uma.wrapper.Token;
import org.xdi.uma.demo.common.server.Configuration;
import org.xdi.uma.demo.common.server.ref.IPat;
import org.xdi.util.InterfaceRegistry;
import org.xdi.util.StringHelper;

import com.uma.server.domain.controller.RptRequester;

// TODO: Auto-generated Javadoc
public class Common {

	/** The Constant LOG. */
	private static final Logger LOG = Logger.getLogger(Common.class);

	/** The Constant INTERNAL_ERROR. */
	public static final Response INTERNAL_ERROR = Response.status(
			Response.Status.INTERNAL_SERVER_ERROR).build();

	/**
	 * Instantiates a new utils.
	 */
	private Common() {
	}

	/**
	 * Extract.
	 * 
	 * @param p_request
	 *            the p_request
	 * @return the rpt status response
	 */
	public static RptStatusResponse extract(HttpServletRequest p_request) {
		return (RptStatusResponse) p_request
				.getAttribute(RptRequester.RPT_STATUS_ATTR_NAME);
	}

	/**
	 * Unauthorized response.
	 * 
	 * @return the response
	 */
	public static Response unauthorizedResponse() {
		return Response
				.status(Response.Status.UNAUTHORIZED)
				.header("host_id", Configuration.getInstance().getRsHost())
				.header("as_uri",
						Configuration.getInstance().getUmaMetaDataUrl())
				.build();
	}

	/**
	 * Gets the token from authorization.
	 * 
	 * @param p_authorization
	 *            the p_authorization
	 * @return the token from authorization
	 */
	public static String getTokenFromAuthorization(String p_authorization) {
		if (StringHelper.isNotEmpty(p_authorization)
				&& p_authorization.startsWith("Bearer ")) {
			return p_authorization.substring("Bearer ".length());
		}
		return null;
	}

	/**
	 * Creates the msg.
	 * 
	 * @param p_type
	 *            the p_type
	 * @param p_resourceType
	 *            the p_resource type
	 * @return the string
	 */
	public static String createMsg(ScopeType p_type, ResourceType p_resourceType) {
		return "Request - Action: " + p_type + " Resource: " + p_resourceType;
	}

	/**
	 * Gets the pat.
	 * 
	 * @return the pat
	 */
	public static Token getPat() {
		final Token pat = InterfaceRegistry.get(IPat.class);
		if (pat == null) {
			return getToken();
		}
		return pat;
	}

	/**
	 * Obtain pat.
	 * 
	 * @return the token
	 */
	public static Token getToken() {
		try {
			Configuration c = Configuration.getInstance();
			LOG.info("Get PAT token");
			Token patToken = UmaClient.requestPat(c.getAuthorizeUrl(),
					c.getTokenUrl(), c.getUmaUserId(), c.getUmaUserSecret(),
					c.getUmaPatClientId(), c.getUmaPatClientSecret(),
					c.getUmaRedirectUri());
			if (patToken != null) {
				InterfaceRegistry.put(IPat.class, patToken);
				return patToken;
			}
			LOG.error("PAT get some error");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
