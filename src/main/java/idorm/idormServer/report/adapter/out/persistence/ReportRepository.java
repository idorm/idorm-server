package idorm.idormServer.report.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<ReportJpaEntity, Long> {
}