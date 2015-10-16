package com.uma.server.service.impl;

import org.apache.commons.lang.StringUtils;
import org.xdi.uma.demo.common.server.Configuration;
import org.xdi.uma.demo.rs.shared.ScopeType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 15/05/2013
 */

public class ScopeService {

    private static final ScopeService INSTANCE = new ScopeService();

    private ScopeService() {
    }

    public static ScopeService getInstance() {
        return INSTANCE;
    }

    public String getScopeAsUri(ScopeType p_type) {
        final String scopeUrl = Configuration.getInstance().getRsScope();
        final StringBuilder sb = new StringBuilder(scopeUrl);
        sb.append("/").append(p_type.getValue());
        return sb.toString();
    }

    public List<String> getScopesAsUrls(List<ScopeType> p_scopes) {
        final List<String> result = new ArrayList<String>();
        if (p_scopes != null && !p_scopes.isEmpty()) {
            for (ScopeType t : p_scopes) {
                final String url = getScopeAsUri(t);
                if (StringUtils.isNotBlank(url)) {
                    result.add(url);
                }
            }
        }
        return result;
    }

    public boolean hasAnyScope(List<String> p_scopesAsUri, List<ScopeType> p_scopeToCheck) {
        if (p_scopeToCheck != null && !p_scopeToCheck.isEmpty()) {
            for (ScopeType scope : p_scopeToCheck) {
                if (hasScope(p_scopesAsUri, scope)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasScope(List<String> p_scopesAsUri, ScopeType p_scopeToCheck) {
        if (p_scopeToCheck != null && p_scopesAsUri != null && !p_scopesAsUri.isEmpty()) {
            for (String scopeUrl : p_scopesAsUri) {
                final String scopeUriToCheck = getScopeAsUri(p_scopeToCheck);
                if (scopeUrl.equals(scopeUriToCheck)) {
                    return true;
                }
            }
        }
        return false;
    }
}
