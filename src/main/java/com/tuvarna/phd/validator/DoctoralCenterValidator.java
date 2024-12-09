package com.tuvarna.phd.validator;

import com.tuvarna.phd.exception.DoctoralCenterRoleException;

import org.jboss.logging.Logger;
import com.tuvarna.phd.service.dto.DoctoralCenterDTO;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DoctoralCenterValidator {

	@Inject
	private final static Logger LOG = Logger.getLogger(DoctoralCenterValidator.class);

	private enum VALID_ROLES {
		expert, manager;
	}

	public void validateRoleIsExpert(DoctoralCenterDTO doctoralCenterDTO) throws DoctoralCenterRoleException {
		try {
			VALID_ROLES isRoleExpert = VALID_ROLES.valueOf(doctoralCenterDTO.getRole());
			if (isRoleExpert != VALID_ROLES.expert)
				throw new DoctoralCenterRoleException("Role is not expert!");
		} catch (IllegalArgumentException e) {
			throw new DoctoralCenterRoleException("Role is not valid!");
		}
		;
	}
}
