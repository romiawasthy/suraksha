/**
 * 
 */
package com.romi.suraksha.security.filters;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.filter.GenericFilterBean;

/**
 * @author rawsthy
 * 
 */
public class ProxyRequestFilter extends GenericFilterBean {

	private Logger logger = org.slf4j.LoggerFactory
			.getLogger(ProxyRequestFilter.class);
	public Map<String, String> getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Map<String, String> configuration) {
		this.configuration = configuration;
	}

	Map<String, String> configuration = new HashMap<String, String>();

	@Override
	public void doFilter(ServletRequest req, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		
		String pathIn = ((HttpServletRequest)req).getRequestURI();

		if (configuration.containsKey(pathIn)) {
			URL proxySite = new URL(URLDecoder.decode(
					configuration.get(pathIn), "utf-8"));
			
			

			try {

				FileCopyUtils.copy(proxySite.openStream(),
						response.getOutputStream());

			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				throw new RuntimeException("Could not invoke requested URL");
			}

		}

		chain.doFilter(req, response);

	}
}
