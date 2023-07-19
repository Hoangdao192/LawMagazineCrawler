package org.hoangdao.taskmanagement.service;

import org.hoangdao.taskmanagement.entity.Magazine;
import org.hoangdao.taskmanagement.repository.MagazineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MagazineService {

    @Autowired
    private MagazineRepository magazineRepository;

    public Magazine createMagazine(Magazine magazine) {
        return magazineRepository.save(magazine);
    }

}
