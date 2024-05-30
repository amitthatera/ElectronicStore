package com.estore.utility;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import com.estore.dto.PageableResponse;

public class Helper {

	public static <U,V> PageableResponse<V> getPageableResponse(Page<U> page, Class<V> type ){
		List<U> data = page.getContent();
		List<V> dto = data.stream().map(object -> new ModelMapper().map(object, type)).collect(Collectors.toList());
		
		PageableResponse<V> response = new PageableResponse<>();
		response.setContent(dto);
		response.setPageNumber(page.getNumber());
		response.setPageSize(page.getSize());
		response.setTotalPages(page.getTotalPages());
		response.setTotalElements(page.getTotalElements());
		response.setIsLastPage(page.isLast());
		return response;
	}
}
