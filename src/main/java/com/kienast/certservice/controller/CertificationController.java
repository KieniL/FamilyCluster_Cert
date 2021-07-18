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

public class CertificationController implements CertApi{

 
	private static Logger logger = LogManager.getLogger(CertificationController.class.getName());

	@Autowired
	private CertificationService certificationService;
	
	
	@Override
	public ResponseEntity<CertificationModel> addCertifaction(@Valid CertificationModel certificationModel) {

		
		try{
			certificationService.addCertification(certificationModel.getShortname(), certificationModel.getDescription(),
					Timestamp.valueOf(certificationModel.getDateTo().atStartOfDay()),
					Timestamp.valueOf(certificationModel.getDateFrom().atStartOfDay()));
		}catch (Exception e) {
			System.out.println(e.getMessage());
			throw new BadRequestException("Adding");
		}
				
		return ResponseEntity.ok(certificationModel);
		
		
	}

	@Override
	public ResponseEntity<CertificationModel> getCertification(String shortname) {
		return ResponseEntity.ok(certificationService.getCertificationByShortname(shortname));
	}

	@Override
	public ResponseEntity<List<CertificationModel>> getCertifications() {
		logger.trace("Trace message");
		logger.debug("debug message");
		logger.info("Info message");
		logger.warn("warn message");
    logger.error("Error message");
		MDC.put("SYSTEM_LOG_LEVEL", System.getenv("CERT_LOG_LEVEL")); 
 		MDC.put("REQUEST_ID", "2025b9bcf"); 
		logger.fatal("fatal message");
		return ResponseEntity.ok(certificationService.getCertifications());
	}

}
