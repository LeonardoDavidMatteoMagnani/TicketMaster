package it.unibo.ticketmaster.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.unibo.ticketmaster.data.model.entity.Bug;
import it.unibo.ticketmaster.data.model.entity.StatoBug;

public interface StatoBugRepository extends JpaRepository<StatoBug, String> {

    @Query("SELECT sb FROM StatoBug sb WHERE sb NOT IN (SELECT sb.id.statoBug FROM SviluppoBug sb WHERE sb.id.bug = :bug)")
    List<StatoBug> findStatiNotInSviluppoBug(@Param("bug") Bug bug);

}
