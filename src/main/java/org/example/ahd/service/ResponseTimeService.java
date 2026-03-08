package org.example.ahd.service;

import org.example.ahd.domain.ResponseTime;
import org.example.ahd.repository.ResponseTimeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ResponseTimeService {
    private final ResponseTimeRepository responseTimeRepository;

    public ResponseTimeService(ResponseTimeRepository responseTimeRepository) {
        this.responseTimeRepository = responseTimeRepository;
    }

    @Transactional(readOnly = true)
    public List<ResponseTime> getAllResponseTimes(){
        return responseTimeRepository.getAll();
    }
}
