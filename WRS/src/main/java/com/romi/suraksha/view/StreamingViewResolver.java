package com.romi.suraksha.view;

import java.util.Map;

import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

public class StreamingViewResolver extends UrlBasedViewResolver {
	
	Map<String, String> viewMapping;
	
	
	@Override
	protected AbstractUrlBasedView buildView(String viewName) throws Exception {
		
		AbstractUrlBasedView view = new StreamingView();
		view.setUrl(getViewUrl(viewName));
		String contentType = getContentType();
		if (contentType != null) {
			view.setContentType(contentType);
		}
		view.setRequestContextAttribute(getRequestContextAttribute());
		view.setAttributesMap(getAttributesMap());
		
		return view;
	}


	public Map<String, String> getViewMapping() {
		return viewMapping;
	}


	public void setViewMapping(Map<String, String> viewMapping) {
		this.viewMapping = viewMapping;
	}


	private String getViewUrl(String viewName) {
		if (viewMapping == null || !viewMapping.containsKey(viewName))
		{
			throw new RuntimeException("No viewMapping found");
		}
		return viewMapping.get(viewName);
		
	}
	
	

}
