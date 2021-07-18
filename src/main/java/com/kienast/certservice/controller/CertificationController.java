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
	public ResponseEntity<CertificationModel> addCertifaction(String JWT, String xRequestID, String SOURCE_IP,
			@Valid CertificationModel certificationModel) {

		initializeLogInfo(xRequestID, SOURCE_IP, "1");
		logger.info("Got Request (Add Certification) for " + certificationModel.getDescription());

		try {

			logger.info("Try to add Certification for " + certificationModel.getDescription());
			certificationService.addCertification(certificationModel.getShortname(), certificationModel.getDescription(),
					Timestamp.valueOf(certificationModel.getDateTo().atStartOfDay()),
					Timestamp.valueOf(certificationModel.getDateFrom().atStartOfDay()));

		} catch (Exception e) {

			logger.error("Error on adding Certification:  " + e.getMessage());
			throw new BadRequestException("Adding");
		}

		logger.info("Adding of Certification for " + certificationModel.getDescription() + " was successfull");

		return ResponseEntity.ok(certificationModel);

	}

	@Override
	public ResponseEntity<CertificationModel> getCertification(String shortname, String JWT, String xRequestID,
			String SOURCE_IP) {
		initializeLogInfo(xRequestID, SOURCE_IP, "1");
		logger.info("Got Request (Get Certification) for " + shortname);

		CertificationModel certification = certificationService.getCertificationByShortname(shortname);
		logger.info("Successfully loaded certification " + shortname);

		return ResponseEntity.ok(certification);
	}

	@Override
	public ResponseEntity<List<CertificationModel>> getCertifications(String JWT, String xRequestID, String SOURCE_IP) {
		initializeLogInfo(xRequestID, SOURCE_IP, "1");
		logger.info("Got Request (Get Certifications)");

		List<CertificationModel> certifications = certificationService.getCertifications();
		logger.info("Successfully loaded all Certifications");

		return ResponseEntity.ok(certifications);
	}

	private void initializeLogInfo(String requestId, String sourceIP, String userId) {
		MDC.put("SYSTEM_LOG_LEVEL", System.getenv("CERT_LOG_LEVEL"));
		MDC.put("REQUEST_ID", requestId);
		MDC.put("SOURCE_IP", sourceIP);
		MDC.put("USER_ID", userId);
	}
}
