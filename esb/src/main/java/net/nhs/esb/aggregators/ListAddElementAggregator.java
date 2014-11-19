package net.nhs.esb.aggregators;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class ListAddElementAggregator {
	
	public <T> List<T> aggregate(List<T> existing, T next) {
        if(existing == null) {
        	existing = new ArrayList<T>();
        }
    	existing.add(next);
    	return existing;
	}
}
