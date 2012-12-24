package com.romi.suraksha.view;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

public class StreamingView extends AbstractUrlBasedView {
	
	
	

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		URL dfile = new URL(URLDecoder.decode(getUrl(), "utf-8"));
		
		try {
			
			FileCopyUtils.copy(dfile.openStream(), response.getOutputStream());
			
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch (IOException e)
		{
			throw new RuntimeException("Could not invoke RetentionSchedule");
		}	
		
	}
	
	

}
