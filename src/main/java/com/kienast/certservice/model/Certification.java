package com.kienast.certservice.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "certification")
public class Certification extends AuditModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
    private Long id;
	
	@NotBlank
	@Column(name = "date_to", columnDefinition = "timestamp", nullable = false)
	private Timestamp dateTo;
	
	@NotBlank
	@Column(name = "date_from", columnDefinition = "timestamp", nullable = false)
	private Timestamp dateFrom;
	
	@NotBlank
	@Size(min = 1, max = 250)
	@Column(name = "description", columnDefinition = "text", nullable = false)
    private String description;
	
	@NotBlank
	@Size(min = 1, max = 40)
	@Column(name = "short_name", columnDefinition = "text", nullable = false)
    private String shortName;
	
	public Certification() {
		
	}
	

	public Certification(Timestamp dateTo, Timestamp dateFrom,
			String description,
			String shortName) {
		this.dateTo = dateTo;
		this.dateFrom = dateFrom;
		this.description = description;
		this.shortName = shortName;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Timestamp getDateTo() {
		return dateTo;
	}

	public void setDateTo(Timestamp dateTo) {
		this.dateTo = dateTo;
	}

	public Timestamp getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Timestamp dateFrom) {
		this.dateFrom = dateFrom;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
}
