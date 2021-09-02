package com.example.demo;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.example.backend.Bug;
import com.example.backend.BugRepository;

/**
 * @author Matt Koeser
 *
 */
public class MockitoPractice {

	@MockBean
	BugRepository br;
	@MockBean
	Bug bug;
	@MockBean
	Bug bug2;
	
	

	@Test
	public void testTrophyPicture() {
		br = mock(BugRepository.class);
		bug = mock(Bug.class);
		bug2 = mock(Bug.class);
		
		int bugid = 10;
		when(br.findBugById(bugid)).thenReturn(bug);
		assertFalse(bug.equals(bug2));
		assertFalse(br.findBugById(bugid).equals(bug2));
			
	}



}