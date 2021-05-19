package com.kienast.certservice.service;

import java.sql.Timestamp;
import java.util.List;

import com.kienast.certservice.model.Certification;
import com.kienast.certservice.rest.api.model.CertificationModel;

public interface CertificationService {

	Certification addCertification(String shortName, String description, Timestamp dateTo, Timestamp dateFrom);
	
	
	CertificationModel getCertificationByShortname(String shortName);
	
	
	List<CertificationModel> getCertifications();
}
