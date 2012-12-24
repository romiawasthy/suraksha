/**
 * 
 */
package com.romi.suraksha.security.provider;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.authentication.LdapAuthenticator;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;


/**
 * @author rawasthy
 *
 */
public class InmemoryAuthenticationProvider implements AuthenticationProvider{
	AuthenticationUserDetailsService userDetailsService = null;



	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		
	
		return authentication;
	}
	@Override
	public boolean supports(Class<? extends Object> authentication) {
		// TODO Auto-generated method stub
		return false;
	}
 
	
}
