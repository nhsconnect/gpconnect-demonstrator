package net.nhs.esb.aggregators;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class ListAddListAggregator {
	
	public <T> List<T> aggregate(List<T> existing, List<T> next) {
    	existing.addAll(next);
    	return existing;
	}
}
