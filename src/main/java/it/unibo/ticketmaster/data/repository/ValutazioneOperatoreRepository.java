package it.unibo.ticketmaster.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import it.unibo.ticketmaster.data.model.entity.ValutazioneOperatore;
import it.unibo.ticketmaster.data.model.entity.ValutazioneOperatoreId;

public interface ValutazioneOperatoreRepository extends JpaRepository<ValutazioneOperatore, ValutazioneOperatoreId> {
}
