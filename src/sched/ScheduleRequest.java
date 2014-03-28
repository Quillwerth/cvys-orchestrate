package sched;

import java.util.ArrayList;
import java.util.List;

public class ScheduleRequest {
	String id = null;
	String name = null;
	List<Integer> dayPreferences = null;
	
	
	public ScheduleRequest(String id, String name, List<Integer> dayPrefs){
		this.id = id;
		this.name = name;
		dayPreferences = new ArrayList<Integer>();
		for(Integer i : dayPrefs){
			dayPreferences.add(i);
		}
	}
	
	public String toString(){
		return "(" + id +", "+name+", "+dayPreferences+")";
	}
}
