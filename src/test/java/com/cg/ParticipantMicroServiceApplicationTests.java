package com.cg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cg.entity.Participant;
import com.cg.exception.ParticipantAlreadyExistsException;
import com.cg.exception.ParticipantNotFoundException;
import com.cg.repository.ParticipantRepository;
import com.cg.service.ParticipantService;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ParticipantMicroServiceApplicationTests {

	@MockBean
	ParticipantRepository repo;
	@Autowired
	private ParticipantService service;
	Participant p;
	List<Participant> participantList;
	
	@Test
	public void contextLoads() {
	}

	@Before
	public void setUp() throws Exception {
		p = new Participant("E105","deepak@gmail.com",105,"S105");
		participantList = new ArrayList<Participant>();
		participantList.add(p);
	}
	
	@Test
	public void testGetAllParticipantsSuccess() {
		Mockito.when(repo.findAll()).thenReturn(participantList);
		assertEquals(participantList, service.getAllParticipants());
	}

	@Test
	public void testGetAllParticipantsWithNoValuePresentFailure() {
		Mockito.when(repo.findAll()).thenReturn(new ArrayList<>());
		assertNotEquals(participantList, service.getAllParticipants());
	}
	
	@Test
	public void testGetParticipantByApplicationIdSuccess() throws ParticipantNotFoundException
	{
		int pid = 101;
		Optional<Participant> optional = Optional.of(p);
		Mockito.when(repo.findById(pid)).thenReturn(optional);
		assertEquals(p, service.getParticipantByApplicationId(pid));
	}
	@Test(expected=ParticipantNotFoundException.class)
	public void testGetParticipantByApplicationIdNotFoundFailure() throws ParticipantNotFoundException
	{
		int pid = 101;
		service.getParticipantByApplicationId(pid);
	}
	@Test
	public void testGetParticipantByRollNoSuccess() throws ParticipantNotFoundException
	{
		String rollNo = "C101";
		Optional<Participant> optional = Optional.of(p);
		Mockito.when(repo.findByRollNo(rollNo)).thenReturn(optional);
		assertEquals(p, service.getParticipantByRollNo(rollNo));
	}
	@Test(expected=ParticipantNotFoundException.class)
	public void testGetParticipantByRollNoNotFoundFailure() throws ParticipantNotFoundException
	{
		String rollNo = "C101";
		service.getParticipantByRollNo(rollNo);
		
	}
	@Test
	public void testAddParticipantForSuccess() throws ParticipantAlreadyExistsException
	{
		p = new Participant("C106","ravi@mail.com",106,"A106");
		Mockito.when(repo.existsById(p.getApplicationId())).thenReturn(false);
		Mockito.when(repo.saveAndFlush(p)).thenReturn(p);
		assertEquals(p, service.addParticipant(p));
	}
	
	@Test(expected = ParticipantAlreadyExistsException.class)
	public void testAddParticipantForAlreadyExistingParticipantFailure() throws ParticipantAlreadyExistsException
	{
		p = new Participant("C103","ravi@mail.com",102,"S106");
		Mockito.when(repo.existsById(p.getApplicationId())).thenReturn(true);
		service.addParticipant(p);
	}
	@Test
	public void testUpdateParticipantSuccess() throws ParticipantNotFoundException
	{
		p  = new Participant("C101", "deep@mail.com", 101, "S102");
		Mockito.when(repo.existsById(p.getApplicationId())).thenReturn(true);
		Mockito.when(repo.save(p)).thenReturn(p);
		assertEquals(p, service.updateParticipant(p));
		
	}
	@Test(expected=ParticipantNotFoundException.class)
	public void testUpdateParticipantForParticipantNotFoundFailure() throws ParticipantNotFoundException
	{
		p  = new Participant("C101", "deep@mail.com", 102, "S102");
		Mockito.when(repo.existsById(p.getApplicationId())).thenReturn(false);
		service.updateParticipant(p);
	}
	@Test
	public void testDeleteParticipantByIdSuccess() throws ParticipantNotFoundException
	{
		int id =1;
		p = new Participant("C103","ravi@mail.com",102,"S106");
		Optional<Participant> opt = Optional.of(p);
		Mockito.when(repo.findById(id)).thenReturn(opt);
		Mockito.when(repo.existsById(id)).thenReturn(true);
		//assertEquals(p, service.deleteParticipantById(id));
		service.deleteParticipantById(id);
	}
	@Test(expected=ParticipantNotFoundException.class)
	public void testDeleteParticipantNotFoundFailure() throws ParticipantNotFoundException
	{
		int pid =101;
		Mockito.when(repo.existsById(pid)).thenReturn(false);
		service.deleteParticipantById(pid);
	}

}
