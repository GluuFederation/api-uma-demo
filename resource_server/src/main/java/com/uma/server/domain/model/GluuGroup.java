/*
 * oxTrust is available under the MIT License (2008). See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (c) 2014, Gluu
 */

package com.uma.server.domain.model;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.gluu.site.ldap.persistence.annotation.LdapAttribute;
import org.gluu.site.ldap.persistence.annotation.LdapEntry;
import org.gluu.site.ldap.persistence.annotation.LdapObjectClass;
import org.xdi.ldap.model.Entry;
import org.xdi.ldap.model.GluuStatus;

/**
 * Group
 * 
 * @author Yuriy Movchan Date: 11.02.2010
 */
@LdapEntry(sortBy = { "displayName" })
@LdapObjectClass(values = { "top", "gluuGroup" })
public class GluuGroup extends Entry implements Serializable {

	private static final long serialVersionUID = -2812480357430436503L;

	private transient boolean selected;

	@LdapAttribute(ignoreDuringUpdate = true)
	private String inum;

	@LdapAttribute(ignoreDuringUpdate = true)
	private String iname;

	@NotNull
	@Size(min = 0, max = 60, message = "Length of the Display Name should not exceed 60")
	@LdapAttribute
	private String displayName;

	@Size(min = 0, max = 4000, message = "Length of the Description should not exceed 4000")
	@LdapAttribute
	private String description;

	@NotNull
	@LdapAttribute
	private String owner;

	@LdapAttribute(name = "member")
	private List<String> members;

	@LdapAttribute(name = "c")
	private String countryName;

	@LdapAttribute(name = "o")
	private String organization;

	@LdapAttribute
	private String seeAlso;

	@LdapAttribute(name = "gluuStatus")
	private GluuStatus status;

	@LdapAttribute(name = "gluuGroupVisibility")
	private GluuGroupVisibility visibility;

	public String getInum() {
		return inum;
	}

	public void setInum(String inum) {
		this.inum = inum;
	}

	public String getIname() {
		return iname;
	}

	public void setIname(String iname) {
		this.iname = iname;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public List<String> getMembers() {
		return members;
	}

	public void setMembers(List<String> members) {
		this.members = members;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getSeeAlso() {
		return seeAlso;
	}

	public void setSeeAlso(String seeAlso) {
		this.seeAlso = seeAlso;
	}

	public GluuStatus getStatus() {
		return status;
	}

	public void setStatus(GluuStatus status) {
		this.status = status;
	}

	public GluuGroupVisibility getVisibility() {
		return visibility;
	}

	public void setVisibility(GluuGroupVisibility visibility) {
		this.visibility = visibility;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	@Override
	public String toString() {
		return String
				.format("GluuGroup [countryName=%s, description=%s, displayName=%s, iname=%s, inum=%s, members=%s, organization=%s, owner=%s, seeAlso=%s, status=%s, visibility=%s, toString()=%s]",
						countryName, description, displayName, iname, inum,
						members, organization, owner, seeAlso, status,
						visibility, super.toString());
	}

}
