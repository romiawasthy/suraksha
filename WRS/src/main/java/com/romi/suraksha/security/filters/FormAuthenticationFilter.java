/**
 * 
 */
package com.romi.suraksha.security.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.MDC;
import org.slf4j.Logger;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author rawsthy
 * 
 */
public class FormAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private Logger logger = org.slf4j.LoggerFactory.getLogger(FormAuthenticationFilter.class);

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		String sUserID = (String) request.getHeader("iv-user");

		if (sUserID == null) {
			String iv_user = (String) request.getSession().getAttribute("iv-user");
			if (iv_user == null) {
				sUserID = request.getParameter("j_username");
				if (sUserID != null) {
					MDC.put("iv-user", sUserID);
					request.getSession().setAttribute("iv-user", sUserID);
				}
			}

			logger.debug("INFO: Did not find iv_iser in Request header, doing Spring Authentication");
			super.doFilter(request, res, chain);

		}
		else {
			String iv_user = (String) request.getSession().getAttribute("iv-user");
			if (iv_user == null) {
				MDC.put("iv-user", sUserID);

				request.getSession().setAttribute("iv-user", sUserID);
			}
			logger.debug("INFO: Found iv_iser in Request header, doing WebSEAL Pre-Authentication");

			try {
				chain.doFilter(request, res);
			}
			finally {
				MDC.remove("iv-user");
			}
		}
	}
}
