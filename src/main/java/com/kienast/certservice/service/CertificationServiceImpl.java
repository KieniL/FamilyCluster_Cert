package com.kienast.certservice.service;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kienast.certservice.model.Certification;
import com.kienast.certservice.respository.CertificationRepository;
import com.kienast.certservice.rest.api.model.CertificationModel;

@Service
public class CertificationServiceImpl implements CertificationService {

	@Autowired
	private CertificationRepository certificationRepository;

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

		certification = getCerts().stream().filter(item -> item.getShortName().equals(shortName)).findAny().get();

		certModel = new CertificationModel();
		certModel.setDateFrom(certification.getDateFrom().toInstant().atZone(ZoneId.of("UTC")).toLocalDate());
		certModel.setDateTo(certification.getDateTo().toInstant().atZone(ZoneId.of("UTC")).toLocalDate());
		certModel.setDescription(certification.getDescription());
		certModel.setShortname(certification.getShortName());

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
