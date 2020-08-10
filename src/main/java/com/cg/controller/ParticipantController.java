package com.cg.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cg.entity.Participant;
import com.cg.exception.ParticipantAlreadyExistsException;
import com.cg.exception.ParticipantNotFoundException;
import com.cg.service.ParticipantServiceImpl;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
@RequestMapping("/participant")
public class ParticipantController {
	
	Logger logger = LoggerFactory.getLogger(ParticipantController.class);
	
	@Autowired
	ParticipantServiceImpl participantService;
	
	//http://localhost:8089/participant/getallparticipants
	@GetMapping("/getallparticipants")
	public List<Participant> getAllParticipants()
	{
		List<Participant> participants = participantService.getAllParticipants();
		return participants;
	}
	
	//http://localhost:8089/participant/getparticipantbyapplicationid/pid
	
	@GetMapping("/getparticipantbyapplicationid/{pid}")
	@HystrixCommand(fallbackMethod = "participantNotFoundErrorHandler")
	public Participant getParticipantByApplicationId(@PathVariable("pid") int pid) throws ParticipantNotFoundException
	{
		Participant participant = participantService.getParticipantByApplicationId(pid);
		return participant;
	}
	
	//http://localhost:8089/participant/getparticipantbyrollno/rollno
	@GetMapping("/getparticipantbyrollno/{rollno}")
	@HystrixCommand(fallbackMethod = "participantNotFoundByRollNoErrorHandler")
	public Participant getParticipantByRollNo(@PathVariable("rollno") String rollNo) throws ParticipantNotFoundException
	{
		Participant participant = participantService.getParticipantByRollNo(rollNo);
		return participant;
	}
	
	//http://localhost:8089/participant/addnewparticipant
	@PostMapping("/addnewparticipant")
	@HystrixCommand(fallbackMethod = "participantAdditionErrorHandler")
	public ResponseEntity<Participant> addNewParticipant(@RequestBody Participant newParticipant) throws ParticipantAlreadyExistsException
	{
		participantService.addParticipant(newParticipant);
		logger.info("added participant to the database");
		return new ResponseEntity<Participant>(newParticipant, HttpStatus.OK);
	}
	
	//http://localhost:8089/participant/updateparticipant
	@PutMapping("/updateparticipant/{pid}")
	@HystrixCommand(fallbackMethod = "participantUpdateErrorHandler")
	public ResponseEntity<Participant> updateParticipantByApplicationId(@RequestBody Participant participant) throws ParticipantNotFoundException
	{
		participantService.updateParticipant(participant);
		logger.info("Updated participant in database.");
		return new ResponseEntity<Participant>(participant, HttpStatus.OK);
	}
	
	//http://localhost:8089/participant/deleteparticipantbyid/pid
	@DeleteMapping("/deleteparticipantbyid/{pid}")
	@HystrixCommand(fallbackMethod = "participantNotFoundDeletionErrorHandler")
	public ResponseEntity<Participant> deleteParticipantByApplicationId(@PathVariable("pid") int pid) throws ParticipantNotFoundException
	{
		participantService.deleteParticipantById(pid);
		logger.info("Partcipant deleted successfully.");
		return new ResponseEntity<Participant>(HttpStatus.OK);
	}
	
	public Participant participantNotFoundErrorHandler(int pid)
	{
		logger.info("Participant with this id is not present in database :"+pid);
		return new Participant("0",null,0,"00");
	}
	public ResponseEntity<Participant> participantNotFoundDeletionErrorHandler(int pid)
	{
		logger.info("Can't delete. Participant with this id is not present in database :"+pid);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}
	
	public Participant participantNotFoundByRollNoErrorHandler(String rollNo)
	{
		logger.info("Participant with this roll no. is not present : "+rollNo);
		return new Participant("0",null,0,"00");
	}
	
	public ResponseEntity<Participant> participantAdditionErrorHandler(Participant participant)
	{
		logger.info("cannot add participant. Particpant may already exist.");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}
	//update error handler
	public ResponseEntity<Participant> participantUpdateErrorHandler(Participant participant)
	{
		logger.info("Participant with application id "+participant.getApplicationId()+" not found to update");
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}
}
