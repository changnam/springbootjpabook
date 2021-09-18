package com.honsoft.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.honsoft.entity.Artist;
@Component
public class ArtistDaoImpl implements ArtistDao {
    
    
	private EntityManager manager;
	
	public ArtistDaoImpl(@Qualifier("mysqlEntityManagerFactory")EntityManager em) {
		this.manager = em;
	}

    /**
     * Find Artist based on the entity Id.
     *
     * @param artistId the artist Id.
     * @return Artist.
     * @throws EntityNotFoundException when no artist is found.
     */
    public Artist findById(Long artistId) {
        Artist artist = manager.find(Artist.class, artistId);
        if (artist == null) {
            throw new EntityNotFoundException("Can't find Artist for ID "
                + artistId);
        }
        return artist;
    }

    @Override
    @Transactional("mysqlJpaTransactionManager")
    public void save(Artist artist) {
        manager.persist(artist);
    }

    /**
     * Update Artist information.
     *
     * @param artist an Artist to be updated.
     */
    @Override
    @Transactional("mysqlJpaTransactionManager")
    public void update(Artist artist) {
        manager.merge(artist);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Artist> getArtists() {
        Query query = manager.createQuery("select a from Artist a", Artist.class);
        return query.getResultList();
    }
}