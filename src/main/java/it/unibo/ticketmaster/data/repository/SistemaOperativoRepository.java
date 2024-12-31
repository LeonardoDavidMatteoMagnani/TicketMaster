package it.unibo.ticketmaster.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import it.unibo.ticketmaster.data.model.entity.SistemaOperativo;

public interface SistemaOperativoRepository extends JpaRepository<SistemaOperativo, String> {
}
