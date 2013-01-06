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

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
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
		String referer = ((HttpServletRequest)req).getHeader("Referer");
		String protectedResource = "";
		String additionalpath = "";
		String accessURL = StringUtils.substringAfter(pathIn, "/wrs/");
		HttpSession session = ((HttpServletRequest)req).getSession();
		
		if (referer != null)
		{
			session.setAttribute("httpReferer", referer);
		
		}else if(session.getAttribute("httpReferer") != null)
		{
			referer = (String)session.getAttribute("httpReferer");
		}
		else
		{
			referer = pathIn;
		}
		protectedResource = StringUtils.substringBefore(StringUtils.substringAfter(referer, "/wrs/"), "/");
		String refererAdditionalPath = StringUtils.substringAfter(StringUtils.substringAfter(referer, "/wrs/"),protectedResource);
		additionalpath = StringUtils.substringAfter(StringUtils.substringAfter(pathIn, "/wrs/"),protectedResource);
		
		if (StringUtils.contains(additionalpath, refererAdditionalPath))
		{
			accessURL = additionalpath ;//+ StringUtils.substringAfter(pathIn,"/wrs/" + protectedResource ) ;
		
		}else
		{
			accessURL = refererAdditionalPath + additionalpath;
		}
		
		

		if (configuration.containsKey(protectedResource)) {
			
			
			String resourceUrl =configuration.get(protectedResource);
			resourceUrl = resourceUrl + accessURL;
			
			
			
			URL proxySite = new URL(URLDecoder.decode(
					//configuration.get(pathIn) , "utf-8"));
					resourceUrl,"utf-8"));
			
			if (StringUtils.contains(resourceUrl, ".")){
				
				MimetypesFileTypeMap mimeType = new MimetypesFileTypeMap();
				
				String contentType = mimeType.getContentType(resourceUrl);
				
				response.setContentType(contentType);
			}
			
			

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
				throw new RuntimeException("Could not invoke requested URL" +resourceUrl );
			}

		}

		chain.doFilter(req, response);

	}
}
