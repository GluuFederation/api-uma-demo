 package com.uma.server.domain.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.xdi.oxauth.client.uma.ResourceSetRegistrationService;
import org.xdi.oxauth.client.uma.UmaClientFactory;
import org.xdi.oxauth.model.common.ScopeType;
import org.xdi.oxauth.model.uma.ResourceSet;
import org.xdi.oxauth.model.uma.ResourceSetStatus;
import org.xdi.oxauth.model.uma.wrapper.Token;
import org.xdi.uma.demo.common.gwt.Msg;
import org.xdi.uma.demo.common.server.CommonUtils;
import org.xdi.uma.demo.common.server.Configuration;
import org.xdi.uma.demo.common.server.ref.IMetadataConfiguration;
import org.xdi.util.InterfaceRegistry;

import com.google.common.util.concurrent.Service;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.uma.server.common.Common;
import com.uma.server.common.Resource;
import com.uma.server.common.ResourceType;
import com.uma.server.service.impl.ScopeService;

// TODO: Auto-generated Javadoc
/**
 * The Class RsServlet.
 *
 */

public class UmaServlet extends RemoteServiceServlet implements Service{

    /** The Constant LOG. */
    private static final Logger LOG = Logger.getLogger(UmaServlet.class);

    /* (non-Javadoc)
     * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        try {
            final Configuration c = Configuration.getInstance();
            if (c != null) {
                final MetadataConfiguration umaAmConfiguration = UmaClientFactory.instance().createMetaDataConfigurationService(c.getUmaMetaDataUrl()).getMetadataConfiguration();
                if (umaAmConfiguration != null) {
                    InterfaceRegistry.put(IMetadataConfiguration.class, umaAmConfiguration);
                    LOG.info("Loaded Authorization Server configuration: " + CommonUtils.asJsonSilently(umaAmConfiguration));

                    final Resource resource = registerResource();
                    ResourceRegistry.getInstance().put(ResourceType.PHONE, resource);

                    LOG.info("Resource Server started successfully.");
                } else {
                    LOG.error("Unable to load Authorization Server configuration. Failed to start Resource Server.");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the message list.
     *
     * @return the message list
     */
    @Override
    public List<Msg> getMessageList() {
        return CommonUtils.getLogList().getAll();
    }

    /**
     * Register resource.
     *
     * @return the resource
     */
    @Override
    public Resource registerResource() {
        try {
            Token tkn = Common.getPat();
            if (tkn != null) {
                ResourceSet resourceSet = new ResourceSet();
                resourceSet.setScopes(ScopeService.getInstance().getScopesAsUrls(Arrays.asList(ScopeType.values())));

                final ResourceSetRegistrationService registrationService = UmaClientFactory.instance().createResourceSetRegistrationService(CommonUtils.getAmConfiguration());
                final ResourceSetStatus status = registrationService.addResourceSet("Bearer " + tkn.getAccessToken(), String.valueOf(System.currentTimeMillis()), resourceSet);
                if (status != null && StringUtils.isNotBlank(status.getId())) {
                    Resource result = new Resource();
                    result.setId(status.getId());
                    return result;
                }
            } else {
                LOG.info("PAT token is null, unable to register resource set.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Obtain new pat.
     *
     * @return the string
     */
    @Override
    public String getNewPATToken() {
        try {
            final Token token = Common.getToken();
            if (token != null) {
                return token.getAccessToken();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
