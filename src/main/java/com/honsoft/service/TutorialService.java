package com.honsoft.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.honsoft.entity.Tutorial;
import com.honsoft.mapper.TutorialMapper;
import com.honsoft.repository.TutorialRepository;

@Service
public class TutorialService {
	
	@Autowired
	private TutorialRepository tutorialRepository;
	
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private TutorialMapper mapper;
	
	@Autowired
	private JdbcTemplate jdbcTempate;
	
	public List<Tutorial> getAll(){
		return em.createQuery("select t from Tutorial t").getResultList();
	}
	
	public List<Tutorial> getAllByMapper(){
		return mapper.getAll();
	}
	
	public List<Tutorial> getAllByJdbcTemplate(){
		return jdbcTempate.query("select * from Tutorials", new TutorialRowMapper());
	
	}
	
	public List<Tutorial> getAllByRepository(){
		return (List<Tutorial>) tutorialRepository.findAll();
	
	}
	
	class TutorialRowMapper implements RowMapper<Tutorial>{

		@Override
		public Tutorial mapRow(ResultSet rs, int rowNum) throws SQLException {
			Tutorial tutorial = new Tutorial();
			tutorial.setId(rs.getLong("id"));
			tutorial.setTitle(rs.getString("title"));
			tutorial.setDescription(rs.getString("description"));
			tutorial.setPublished(rs.getBoolean("published"));
			return tutorial;
		}
		
	}
}
