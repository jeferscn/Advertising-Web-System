package br.com.unicuritiba.projetopisistemaweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import br.com.unicuritiba.projetopisistemaweb.model.Anuncio;

@Repository
public interface AnuncioRepository extends JpaRepository<Anuncio, Long> {
	
}