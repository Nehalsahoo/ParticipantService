package com.cg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.entity.Participant;
import com.cg.exception.ParticipantAlreadyExistsException;
import com.cg.exception.ParticipantNotFoundException;
import com.cg.repository.ParticipantRepository;

@Service
public class ParticipantServiceImpl implements ParticipantService {
	
	@Autowired
	ParticipantRepository participantRepository;
	
	public List<Participant> getAllParticipants()
	{
		List<Participant> participants = participantRepository.findAll();
		return participants;
	}
	
	public Participant getParticipantByApplicationId(int pid) throws ParticipantNotFoundException
	{
		Optional<Participant> participant = participantRepository.findById(pid);
		if(participant.isPresent())
			return participant.get();
		else
			throw new ParticipantNotFoundException();
	}
	
	public Participant getParticipantByRollNo(String rollNo) throws ParticipantNotFoundException
	{
		Optional<Participant> participant = participantRepository.findByRollNo(rollNo);
		if(participant.isPresent())
			return participant.get();
		else
			throw new ParticipantNotFoundException();
	}
	
	public Participant updateParticipant(Participant participant) throws ParticipantNotFoundException
	{
		if(participantRepository.existsById(participant.getApplicationId()))
			return participantRepository.save(participant);
		else 
			throw new ParticipantNotFoundException();
	}
	
	public Participant addParticipant(Participant newParticipant) throws ParticipantAlreadyExistsException
	{
		if(!participantRepository.existsById(newParticipant.getApplicationId()))
			return participantRepository.saveAndFlush(newParticipant);
		else
			throw new ParticipantAlreadyExistsException();
	}
	
	public void deleteParticipantById(int pid) throws ParticipantNotFoundException
	{
		Optional<Participant> participant = participantRepository.findById(pid);
		if(participant.isPresent())
			participantRepository.deleteById(pid);
		else
			throw new ParticipantNotFoundException();
	}

}
