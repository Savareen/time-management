package com.igorzaitcev.management.security;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.igorzaitcev.management.model.User;
import com.igorzaitcev.management.repository.UserRepository;

@Service
public class ApplicationUserService implements UserDetailsService {

	private final UserRepository userDao;

	public ApplicationUserService(UserRepository userDao) {
		this.userDao = userDao;
	}

	@Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<User> user = userDao.findByEmail(email);
		if (user.isEmpty()) {
			throw new UsernameNotFoundException(String.format("Email %s not found", email));
    	}
		return new ApplicationUser(user.get());
    }
}
