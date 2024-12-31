package it.unibo.ticketmaster.data.repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.unibo.ticketmaster.data.model.entity.Bug;

public interface BugRepository extends JpaRepository<Bug, Long> {

    @Query("SELECT b FROM Bug b"
            + " JOIN SviluppoBug sb ON sb.id.bug = b"
            + " WHERE (:descrizione IS NULL OR lower(b.descrizione) LIKE lower(concat('%', :descrizione, '%')))"
            + " AND (:priorita IS NULL OR b.priorita.nome = :priorita)"
            + " AND (:stato IS NULL OR b.stato.nome = :stato)")
    // + " AND (:stato IS NULL OR t.stato.nome = :stato)")
    List<Bug> search(@Param("descrizione") String descrizione, @Param("priorita") String priorita,
            @Param("stato") String stato);

    @Query("SELECT COUNT(DISTINCT b) FROM Bug b"
            + " JOIN SviluppoBug sb ON sb.id.bug = b"
            + " AND (:dataInizio IS NULL OR sb.dataOraInizio >= :dataInizio)"
            + " AND (:dataFine IS NULL OR sb.dataOraInizio <= :dataFine)")
    Long countBugs(@Param("dataInizio") Timestamp dataInizio, @Param("dataFine") Timestamp dataFine);

    @Query("SELECT COUNT(b) FROM Bug b"
            + " JOIN SviluppoBug sb ON sb.id.bug = b"
            + " WHERE sb.id.statoBug.nome = :stato"
            + " AND (:dataInizio IS NULL OR sb.dataOraInizio >= :dataInizio)"
            + " AND (:dataFine IS NULL OR sb.dataOraInizio <= :dataFine)")
    Long countByStato(@Param("stato") String stato, @Param("dataInizio") Timestamp dataInizio, @Param("dataFine") Timestamp dataFine);

    @Query("SELECT COUNT(DISTINCT b) FROM Bug b"
            + " JOIN SviluppoBug sb ON sb.id.bug = b"
            + " AND b.ticket.problema.nome = :problema"
            + " AND (:dataInizio IS NULL OR sb.dataOraInizio >= :dataInizio)"
            + " AND (:dataFine IS NULL OR sb.dataOraInizio <= :dataFine)")
    Long countByProblema(@Param("problema") String problema, @Param("dataInizio") Timestamp dataInizio, @Param("dataFine") Timestamp dataFine);

    @Query("SELECT COUNT(DISTINCT b) FROM Bug b"
            + " JOIN SviluppoBug sb ON sb.id.bug = b"
            + " AND b.ticket.sistemaOperativo.nome = :sistemaOperativo"
            + " AND (:dataInizio IS NULL OR sb.dataOraInizio >= :dataInizio)"
            + " AND (:dataFine IS NULL OR sb.dataOraInizio <= :dataFine)")
    Long countBySistemaOperativo(@Param("sistemaOperativo") String sistemaOperativo, @Param("dataInizio") Timestamp dataInizio, @Param("dataFine") Timestamp dataFine);

    @Query("SELECT AVG(TIMESTAMPDIFF(HOUR, sb2.dataOraInizio, sb.dataOraInizio)) FROM Bug b"
            + " JOIN SviluppoBug sb ON sb.id.bug = b"
            + " JOIN SviluppoBug sb2 ON sb2.id.bug = b"
            + " AND (sb.id.statoBug.nome = 'chiuso - risolto' OR sb.id.statoBug.nome = 'chiuso - non riproducibile')"
            + " AND sb2.id.statoBug.nome = 'non in correzione'"
            + " AND (:dataInizio IS NULL OR sb.dataOraInizio >= :dataInizio)"
            + " AND (:dataFine IS NULL OR sb.dataOraInizio <= :dataFine)")
    Optional<Float> averageBugDuration(@Param("dataInizio") Timestamp dataInizio, @Param("dataFine") Timestamp dataFine);
}
