package it.unibo.ticketmaster.data.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.unibo.ticketmaster.data.model.entity.Bug;
import it.unibo.ticketmaster.data.model.entity.StatoBug;
import it.unibo.ticketmaster.data.model.relation.SviluppoBug;
import it.unibo.ticketmaster.data.model.relation.SviluppoBugId;

public interface SviluppoBugRepository extends JpaRepository<SviluppoBug, SviluppoBugId> {

    @Query("SELECT sb.id.statoBug FROM SviluppoBug sb WHERE sb.id.bug = :bug ORDER BY sb.dataOraInizio DESC LIMIT 1")
    StatoBug findStatoBug(@Param("bug") Bug bug);

    @Query("SELECT sb FROM SviluppoBug sb WHERE sb.id.bug = :bug ORDER BY sb.dataOraInizio DESC LIMIT 1")
    Optional<SviluppoBug> findLatestSviluppoBugByBug(@Param("bug") Bug bug);
}
