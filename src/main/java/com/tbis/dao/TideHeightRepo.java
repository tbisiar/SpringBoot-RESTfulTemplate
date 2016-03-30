
package com.tbis.dao;

import org.joda.time.DateTime;
import org.springframework.data.repository.Repository;
import com.tbis.model.TideHeight;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;


public interface TideHeightRepo extends Repository<TideHeight, Long> {

    public Page<TideHeight> findAll(Pageable pageable);

    public TideHeight findByDate(DateTime dateTime, int timeSpan);

}