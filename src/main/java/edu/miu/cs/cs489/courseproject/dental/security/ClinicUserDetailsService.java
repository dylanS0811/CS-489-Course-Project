package edu.miu.cs.cs489.courseproject.dental.security;

import edu.miu.cs.cs489.courseproject.dental.repository.AppUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ClinicUserDetailsService implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    public ClinicUserDetailsService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return appUserRepository.findByUsername(username)
                .map(ClinicUserDetails::fromUser)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User '%s' was not found".formatted(username)));
    }
}
