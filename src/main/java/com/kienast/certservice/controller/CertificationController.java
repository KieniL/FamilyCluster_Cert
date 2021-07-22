package com.kienast.certservice.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.kienast.certservice.exception.BadRequestException;
import com.kienast.certservice.exception.NotAuthorizedException;
import com.kienast.certservice.model.TokenVerificationResponse;
import com.kienast.certservice.rest.api.CertApi;
import com.kienast.certservice.rest.api.model.CertificationModel;
import com.kienast.certservice.service.CertificationService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@RestController

public class CertificationController implements CertApi {

	private static Logger logger = LogManager.getLogger(CertificationController.class.getName());

	@Autowired
	private CertificationService certificationService;

	@Value("${logging.level.com.kienast.certservice}")
	private String loglevel;

	// Used for WebTemplate
	@Autowired
	private WebClient.Builder webClientBuilder;

	@Value("${authURL}")
	private String authURL;

	@Override
	public ResponseEntity<CertificationModel> addCertifaction(String JWT, String xRequestID, String SOURCE_IP,
			@Valid CertificationModel certificationModel) {

		initializeLogInfo(xRequestID, SOURCE_IP, "");
		logger.info("Got Request (Add Certification) for " + certificationModel.getDescription());

		try {
			TokenVerificationResponse tokenResponse = new TokenVerificationResponse();

			logger.info("Call Authentication Microservice for JWT Verification");
			tokenResponse = webClientBuilder.build().post() // RequestMethod
					.uri(authURL + "/jwt").header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).header("JWT", JWT)
					.header("X-Request-ID", xRequestID).header("SOURCE_IP", SOURCE_IP).retrieve() // run command
					.onStatus(HttpStatus::is4xxClientError, response -> {
						return Mono.error(new NotAuthorizedException(String.format("Failed JWT Verification")));
					}).bodyToMono(TokenVerificationResponse.class) // convert Response
					.block(); // do as Synchronous call

			initializeLogInfo(xRequestID, SOURCE_IP, tokenResponse.getUserId());
			logger.info("Added userId to log");
		} catch (Exception e) {

			logger.error("Error on verifiying jwt");
			throw new BadRequestException("Verification Failure");
		}

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
		CertificationModel certification = null;
		initializeLogInfo(xRequestID, SOURCE_IP, "");
		logger.info("Got Request (Get Certification) for " + shortname);

		try {
			TokenVerificationResponse tokenResponse = new TokenVerificationResponse();

			logger.info("Call Authentication Microservice for JWT Verification");
			tokenResponse = webClientBuilder.build().post() // RequestMethod
					.uri(authURL + "/jwt").header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).header("JWT", JWT)
					.header("X-Request-ID", xRequestID).header("SOURCE_IP", SOURCE_IP).retrieve() // run command
					.onStatus(HttpStatus::is4xxClientError, response -> {
						return Mono.error(new NotAuthorizedException(String.format("Failed JWT Verification")));
					}).bodyToMono(TokenVerificationResponse.class) // convert Response
					.block(); // do as Synchronous call

			initializeLogInfo(xRequestID, SOURCE_IP, tokenResponse.getUserId());
			logger.info("Added userId to log");
			certification = certificationService.getCertificationByShortname(shortname);
			logger.info("Successfully loaded certification " + shortname);

		} catch (Exception e) {

			logger.error("Error on verifiying jwt");
		}

		return ResponseEntity.ok(certification);
	}

	@Override
	public ResponseEntity<List<CertificationModel>> getCertifications(String JWT, String xRequestID, String SOURCE_IP) {
		List<CertificationModel> certifications = new ArrayList<>();

		initializeLogInfo(xRequestID, SOURCE_IP, "");
		logger.info("Got Request (Get Certifications)");

		try {
			TokenVerificationResponse tokenResponse = new TokenVerificationResponse();

			logger.info("Call Authentication Microservice for JWT Verification");
			tokenResponse = webClientBuilder.build().post() // RequestMethod
					.uri(authURL + "/jwt").header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).header("JWT", JWT)
					.header("X-Request-ID", xRequestID).header("SOURCE_IP", SOURCE_IP).retrieve() // run command
					.onStatus(HttpStatus::is4xxClientError, response -> {
						return Mono.error(new NotAuthorizedException(String.format("Failed JWT Verification")));
					}).bodyToMono(TokenVerificationResponse.class) // convert Response
					.block(); // do as Synchronous call

			initializeLogInfo(xRequestID, SOURCE_IP, tokenResponse.getUserId());
			logger.info("Added userId to log");

			certifications = certificationService.getCertifications();
			logger.info("Successfully loaded all Certifications");

		} catch (Exception e) {

			logger.error("Error on verifiying jwt");
		}

		return ResponseEntity.ok(certifications);
	}

	private void initializeLogInfo(String requestId, String sourceIP, String userId) {
		MDC.put("SYSTEM_LOG_LEVEL", loglevel);
		MDC.put("REQUEST_ID", requestId);
		MDC.put("SOURCE_IP", sourceIP);
		MDC.put("USER_ID", userId);
	}
}
