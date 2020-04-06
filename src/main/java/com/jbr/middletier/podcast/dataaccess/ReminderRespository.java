package com.jbr.middletier.podcast.dataaccess;

import com.jbr.middletier.podcast.data.Reminder;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReminderRespository  extends CrudRepository<Reminder, String>, JpaSpecificationExecutor {
}
