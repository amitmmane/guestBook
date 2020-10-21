package com.example.guestbook.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import com.example.guestbook.entity.Role;
import com.example.guestbook.entity.User;

@RunWith(MockitoJUnitRunner.class)
public class UserTest {
	
	@InjectMocks
	 User user;
	
	@Test
	public void testUser_toString() {
		
		User user = new User();
		user.setEmail("email");
		user.setFirstName("firstName");
		user.setId((long) 1);
		user.setLastName("lastName");
		user.setPassword("password");
		Collection<Role> roles = Arrays.asList(new Role("ROLE_USER"));
		user.setRoles(roles );
		user.toString();
		
	}

}
