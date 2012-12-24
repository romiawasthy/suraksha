/**
 * 
 */
package com.romi.suraksha.security.filters;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;



/**
 * @author rawasthy
 * 
 */
@Component
public class HTTPHeaderPopulator
{
	

	/*@Value("#{websealProperties['webseal.dummy.password']}")
	String configPassword;
	public String getConfigPassword()
	{
		return configPassword;
	}

	public void setConfigPassword(String configPassword)
	{
		this.configPassword = configPassword;
	}*/

	/**
	 * Will populate HTTP Headers from the list of GrantedAuthorities created by emulated AuthenticationProvider
	 */
	public HttpServletRequest addHeaders(HttpServletRequest request, HttpServletResponse response)
	{
		// Authentication authentication =
		// SecurityContextHolder.getContext().getAuthentication();
		// authentication.getPrincipal();
		AbstractAuthenticationToken principal = (AbstractAuthenticationToken) request.getUserPrincipal();
		request = new HttpHeadersRequestWrapper(request);
		String sUserID = principal.getName();

		try
		{
			sUserID = URLEncoder.encode(sUserID, "UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{

			throw new RuntimeException(e);
		}
		((HttpHeadersRequestWrapper) request).addCustomHeader("iv-user", sUserID);
		//((HttpHeadersRequestWrapper) request).addCustomHeader("authorization", genAuth(sUserID, websealUtility.getConfigPassword()));
		((HttpHeadersRequestWrapper) request).addCustomHeader("iv-groups", getGroupsString(principal.getAuthorities()));
		MDC.remove("iv-user");
		MDC.put("iv-user", sUserID);
				
		return request;
	}

	private String getGroupsString(Collection<GrantedAuthority> authorities)
	{
		StringBuffer strAuthorities = new StringBuffer();
		for (GrantedAuthority authro : authorities)
		{
			strAuthorities.append(authro.getAuthority());
		}
		return strAuthorities.toString();
	}

	}
