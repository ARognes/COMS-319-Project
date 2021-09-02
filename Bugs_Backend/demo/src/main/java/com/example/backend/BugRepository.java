package com.example.backend;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface BugRepository extends CrudRepository<Bug,Integer> {
	Bug findBugById(@Param("bugid")int id);
	List<Bug> findAllBugs(@Param("bugid")int bugid);

}

