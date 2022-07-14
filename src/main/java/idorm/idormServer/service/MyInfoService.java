package idorm.idormServer.service;

import idorm.idormServer.repository.MyInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MyInfoService {

    private final MyInfoRepository myInfoRepository;

}
