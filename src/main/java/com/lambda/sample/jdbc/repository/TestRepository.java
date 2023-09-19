package com.lambda.sample.jdbc.repository;

import com.lambda.sample.beans.TempBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface TestRepository extends PagingAndSortingRepository<TempBean, Integer> {


    @Query(value = "SELECT * FROM temp t where t.first_name=?1", nativeQuery = true)
    List<TempBean> findAllUsersByCol2(String val, Pageable pageable);

    @Query(value = "SELECT count(*) from temp t where t.first_name=?1", nativeQuery = true)
    Integer findCountOfAllUsers(String val);

}
