package com.cg.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cg.entity.Participant;
import com.cg.exception.ParticipantNotFoundException;


@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Integer>{
	
	public Optional<Participant> findByRollNo(String rollNo) throws ParticipantNotFoundException;

}
