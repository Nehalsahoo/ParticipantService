package com.cg.service;

import java.util.List;

import com.cg.entity.Participant;
import com.cg.exception.ParticipantAlreadyExistsException;
import com.cg.exception.ParticipantNotFoundException;

public interface ParticipantService {
	
	public List<Participant> getAllParticipants();
	
	public Participant getParticipantByApplicationId(int pid) throws ParticipantNotFoundException;
	
	public Participant getParticipantByRollNo(String rollNo) throws ParticipantNotFoundException;
	
	public Participant updateParticipant(Participant participant) throws ParticipantNotFoundException;
	
	public Participant addParticipant(Participant newParticipant) throws ParticipantAlreadyExistsException;
	
	public void deleteParticipantById(int pid) throws ParticipantNotFoundException;

}
