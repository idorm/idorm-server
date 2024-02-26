package idorm.idormServer.report.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import idorm.idormServer.report.entity.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {
}