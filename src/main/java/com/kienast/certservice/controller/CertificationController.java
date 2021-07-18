package com.kienast.certservice.controller;

import java.sql.Timestamp;
import java.util.List;

import javax.validation.Valid;

import com.kienast.certservice.exception.BadRequestException;
import com.kienast.certservice.rest.api.CertApi;
import com.kienast.certservice.rest.api.model.CertificationModel;
import com.kienast.certservice.service.CertificationService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class CertificationController implements CertApi {

	private static Logger logger = LogManager.getLogger(CertificationController.class.getName());

	@Autowired
	private CertificationService certificationService;

	@Override
	public ResponseEntity<CertificationModel> addCertifaction(@Valid CertificationModel certificationModel) {

		logCustomValues("1", "192.168.0.0", "1", true, 200, "Nothing");
		logger.info("Got Request (Add Certification) for " + certificationModel.getDescription());
		try {
			logCustomValues("1", "192.168.0.0", "1", true, 200, "Nothing");
			logger.info("Try to add Certification for " + certificationModel.getDescription());
			certificationService.addCertification(certificationModel.getShortname(), certificationModel.getDescription(),
					Timestamp.valueOf(certificationModel.getDateTo().atStartOfDay()),
					Timestamp.valueOf(certificationModel.getDateFrom().atStartOfDay()));
		} catch (Exception e) {
			logCustomValues("1", "192.168.0.0", "1", true, 200, "Nothing");
			logger.error("Error on adding Certification:  " + e.getMessage());
			throw new BadRequestException("Adding");
		}


		logCustomValues("1", "192.168.0.0", "1", true, 200, "Nothing");
		logger.info("Adding of Certification for " + certificationModel.getDescription() + " was successfull");

		return ResponseEntity.ok(certificationModel);

	}

	@Override
	public ResponseEntity<CertificationModel> getCertification(String shortname) {
		logCustomValues("1", "192.168.0.0", "1", true, 200, "Nothing");
		logger.info("Got Request (Get Certification) for " + shortname);
		return ResponseEntity.ok(certificationService.getCertificationByShortname(shortname));
	}

	@Override
	public ResponseEntity<List<CertificationModel>> getCertifications() {
		logCustomValues("1", "192.168.0.0", "1", true, 200, "Nothing");
		logger.info("Got Request (Get Certifications)");
		return ResponseEntity.ok(certificationService.getCertifications());
	}

	private void logCustomValues(String requestId, String sourceIP, String userId, boolean isResponse, int httpStatus,
			String reasonForHttpStatus) {
		MDC.put("SYSTEM_LOG_LEVEL", System.getenv("CERT_LOG_LEVEL"));
		MDC.put("REQUEST_ID", requestId);
		MDC.put("SOURCE_IP", sourceIP);
		MDC.put("USER_ID", userId);
		if (isResponse) {
			MDC.put("HTTP_STATUS_CODE", String.valueOf(httpStatus));
			MDC.put("REASON_FOR_HTTP_STATUS", reasonForHttpStatus);
		}
	}

}
