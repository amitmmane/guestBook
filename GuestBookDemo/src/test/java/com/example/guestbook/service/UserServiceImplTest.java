package com.example.guestbook.service;



import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.guestbook.entity.Role;
import com.example.guestbook.entity.User;
import com.example.guestbook.helper.UserRegistrationDto;
import com.example.guestbook.repository.UserRepository;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {
	
	@InjectMocks
	UserServiceImpl userServiceImpl;
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private BCryptPasswordEncoder passwordEncoder;
	
	@Test
	public void testSaveUser_Success() throws Exception {
		
		UserRegistrationDto registrationDto =  new UserRegistrationDto();
		registrationDto.setEmail("Email");
		registrationDto.setFirstName("fname");
		registrationDto.setLastName("lname");
		registrationDto.setPassword("pwd");
		
		User user = new User();
		user.setEmail("Email");
		user.setFirstName("fname");
		user.setId((long) 1);
		user.setLastName("lname");
		Collection<Role> roles = Arrays.asList(new Role("ROLE_USER"));
		user.setRoles(roles);
		/*
		 * User user = new User(registrationDto.getFirstName(),
		 * registrationDto.getLastName(), registrationDto.getEmail(),
		 * passwordEncoder.encode(registrationDto.getPassword()), Arrays.asList(new
		 * Role("ROLE_USER")));
		 */
		Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
		Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn("PWD");
		
		User userexpected = userServiceImpl.save(registrationDto);
		assertEquals(userexpected.getEmail(), user.getEmail());
		assertEquals(userexpected.getFirstName(), user.getFirstName());
		assertEquals(userexpected.getLastName(), user.getLastName());
	}
	
	
	@Test
	public void testLoadUserByUsername_Success() throws Exception {
		
		User user = new User();
		user.setEmail("Email");
		user.setFirstName("fname");
		user.setId((long) 1);
		user.setLastName("lname");
		user.setPassword("pwd");
		Collection<Role> roles = Arrays.asList(new Role("ROLE_USER"));
		user.setRoles(roles);
		
		Mockito.when(userRepository.findByEmail("aName")).thenReturn(user);
		UserDetails userdetails  =userServiceImpl.loadUserByUsername("aName");
		assertEquals(userdetails.getUsername(), user.getEmail());
	}
	
	@Test(expected = UsernameNotFoundException.class)
	public void testLoadUserByUsername_Failure() throws Exception {
		
		Mockito.when(userRepository.findByEmail("aName")).thenReturn(null);
		 userServiceImpl.loadUserByUsername("aName");
	}
	
	
	@Test
	public void testFindByEmail_Success() throws Exception {
		
		User user = new User();
		user.setEmail("Email");
		user.setFirstName("fname");
		user.setId((long) 1);
		user.setLastName("lname");
		user.setPassword("pwd");
		Collection<Role> roles = Arrays.asList(new Role("ROLE_USER"));
		user.setRoles(roles);
		Mockito.when(userRepository.findByEmail("aName")).thenReturn(user);
		User userexpected =userServiceImpl.findByEmail("aName");
		assertEquals(userexpected.getEmail(), user.getEmail());
		
	}
	
}
