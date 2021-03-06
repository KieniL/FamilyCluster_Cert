package com.kienast.certservice.service;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import com.kienast.certservice.controller.CertificationController;
import com.kienast.certservice.model.Certification;
import com.kienast.certservice.respository.CertificationRepository;
import com.kienast.certservice.rest.api.model.CertificationModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CertificationServiceImpl implements CertificationService {

	@Autowired
	private CertificationRepository certificationRepository;

	private static Logger logger = LogManager.getLogger(CertificationController.class.getName());

	@Override
	public Certification addCertification(String shortName, String description, Timestamp dateTo, Timestamp dateFrom) {

		try {
			certificationRepository.save(new Certification(dateTo, dateFrom, description, shortName));
			return getCerts().stream().filter(item -> item.getShortName().equals(shortName)).findAny().get();

		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public CertificationModel getCertificationByShortname(String shortName) {

		Certification certification = null;
		CertificationModel certModel = null;

		try{
			certification = getCerts().stream().filter(item -> item.getShortName().equals(shortName)).findAny().get();

			certModel = new CertificationModel();
			certModel.setDateFrom(certification.getDateFrom().toInstant().atZone(ZoneId.of("UTC")).toLocalDate());
			certModel.setDateTo(certification.getDateTo().toInstant().atZone(ZoneId.of("UTC")).toLocalDate());
			certModel.setDescription(certification.getDescription());
			certModel.setShortname(certification.getShortName());
		}catch(Exception e){
			logger.info(e.getMessage());
		}
		

		return certModel;
	}

	@Override
	public List<CertificationModel> getCertifications() {
		List<CertificationModel> response = new ArrayList<>();

		List<Certification> certifications = getCerts();

		for (Certification c : certifications) {
			CertificationModel certModel = new CertificationModel();
			certModel.setDateFrom(c.getDateFrom().toInstant().atZone(ZoneId.of("UTC")).toLocalDate());
			certModel.setDateTo(c.getDateTo().toInstant().atZone(ZoneId.of("UTC")).toLocalDate());
			certModel.setDescription(c.getDescription());
			certModel.setShortname(c.getShortName());
			response.add(certModel);

		}

		return response;

	}

	private List<Certification> getCerts() {
		return certificationRepository.findAll();
	}

}
