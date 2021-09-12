package com.honsoft.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.honsoft.entity.Tutorial;

public interface TutorialMapper {
	@Select("select * from Tutorials")
	public List<Tutorial> getAll();
}
