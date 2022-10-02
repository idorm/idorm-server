//package idorm.idormServer.calendar.repository;
//
//import idorm.idormServer.calendar.domain.Calendar;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.time.LocalDateTime;
//
//@Repository
//public interface CalendarRepository extends JpaRepository<Calendar, Long> {
//
//    @Query("select C from Calendar C where" +
//            ":startDate is null or C.endTime > :startDate and" +
//            ":endDate is null or C.startTime < :endDate")
//    Page<Calendar> search(Pageable pageable,
//                          @Param("startDate") LocalDateTime startDate,
//                          @Param("endDate") LocalDateTime endDate);
//}
