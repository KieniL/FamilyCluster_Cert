package com.kienast.certservice.respository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kienast.certservice.model.Certification;

@Repository
public interface CertificationRepository extends JpaRepository<Certification, Long> {

}