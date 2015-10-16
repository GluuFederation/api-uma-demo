package com.uma.server.domain.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.xdi.oxauth.client.uma.ResourceSetRegistrationService;
import org.xdi.oxauth.client.uma.UmaClientFactory;
import org.xdi.oxauth.model.common.ScopeType;
import org.xdi.oxauth.model.uma.ResourceSet;
import org.xdi.oxauth.model.uma.ResourceSetStatus;
import org.xdi.oxauth.model.uma.wrapper.Token;
import org.xdi.uma.demo.common.server.CommonUtils;

import com.uma.server.common.Common;
import com.uma.server.common.Resource;
import com.uma.server.common.ResourceType;
import com.uma.server.service.impl.ScopeService;

// TODO: Auto-generated Javadoc
/**
 * The Class ResourceRegistry.
 *
 */

public class ResourceRegistry {

    /** The Constant LOG. */
    private static final Logger LOG = Logger.getLogger(ResourceRegistry.class);

    /** The Constant INSTANCE. */
    private static final ResourceRegistry INSTANCE = new ResourceRegistry();

    /** The m_map. */
    private final Map<ResourceType, Resource> m_map = new HashMap<ResourceType, Resource>();

    /**
     * Instantiates a new resource registry.
     */
    private ResourceRegistry() {
    }

    /**
     * Gets the single instance of ResourceRegistry.
     *
     * @return single instance of ResourceRegistry
     */
    public static ResourceRegistry getInstance() {
        return INSTANCE;
    }

    /**
     * Gets the resource id.
     *
     * @param p_resourceType the p_resource type
     * @return the resource id
     */
    public Resource getResourceId(ResourceType p_resourceType) {
        Resource resource = m_map.get(p_resourceType);
        if (resource == null) {
            resource = registerResource();
            put(ResourceType.PHONE, resource);
        }
        return resource;
    }

    /**
     * Put.
     *
     * @param p_resourceType the p_resource type
     * @param p_resource the p_resource
     */
    public void put(ResourceType p_resourceType, Resource p_resource) {
        m_map.put(p_resourceType, p_resource);
    }

    /**
     * Register resource.
     *
     * @return the resource
     */
    public Resource registerResource() {
        final Token pat = Common.getPat();
        if (pat != null) {
            final ResourceSet resourceSet = new ResourceSet();
            resourceSet.setScopes(ScopeService.getInstance().getScopesAsUrls(Arrays.asList(ScopeType.values())));

            final ResourceSetRegistrationService registrationService = UmaClientFactory.instance().createResourceSetRegistrationService(CommonUtils.getAmConfiguration());
            final ResourceSetStatus status = registrationService.addResourceSet("Bearer " + pat.getAccessToken(), String.valueOf(System.currentTimeMillis()), resourceSet);
            if (status != null && StringUtils.isNotBlank(status.getId())) {
                final Resource result = new Resource();
                result.setId(status.getId());
                LOG.info("Resource registered, resource id: " + status.getId());
                return result;
            }
        } else {
            LOG.error("PAT token is null, unable to register resource set.");
        }
        return null;
    }
}
