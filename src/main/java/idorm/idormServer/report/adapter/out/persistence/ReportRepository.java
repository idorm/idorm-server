package idorm.idormServer.report.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import idorm.idormServer.report.entity.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
}