package com.tuvarna.phd.validator;

import org.jboss.logging.Logger;

import com.tuvarna.phd.service.dto.DoctoralCenterDTO;

import jakarta.inject.Inject;

public class DoctoralCenterValidator {

	@Inject
	private final static Logger LOG = Logger.getLogger(DoctoralCenterValidator.class);

	private enum VALID_ROLES {
		expert, manager;
	}

	public void validateRoleIsExpert(DoctoralCenterDTO doctoralCenterDTO) {
		if (VALID_ROLES.valueOf(doctoralCenterDTO.getRole()) == VALID_ROLES.expert) {
			System.out.println("");
		}
	}
}
