package com.honsoft;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

import com.honsoft.dao.ArtistDao;
import com.honsoft.dao.ArtistDaoImpl;
import com.honsoft.entity.Artist;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class })
public class SpringbootjpabookApplication implements CommandLineRunner {

	@Autowired
	private ArtistDao artistDao;

	public static void main(String[] args) {
		SpringApplication.run(SpringbootjpabookApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		Artist artist = new Artist();
		artist.setName("test artist");
		artistDao.save(artist);

		artist = artistDao.findById(1L);
		System.out.println("Artist = " + artist);

		artist.setName("Bon Jovi");
		artistDao.update(artist);

		artist = artistDao.findById(artist.getId());
		System.out.println("Artist = " + artist);

	}

}
