package com.kienast.certservice.controller;

import java.sql.Timestamp;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.kienast.certservice.exception.BadRequestException;
import com.kienast.certservice.rest.api.CertApi;
import com.kienast.certservice.rest.api.model.CertificationModel;
import com.kienast.certservice.service.CertificationService;

@RestController
public class CertificationController implements CertApi{

	
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
		
		return ResponseEntity.ok(certificationService.getCertifications());
	}

}
