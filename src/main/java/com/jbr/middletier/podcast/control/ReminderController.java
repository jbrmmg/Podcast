package com.jbr.middletier.podcast.control;

import com.jbr.middletier.podcast.data.Reminder;
import com.jbr.middletier.podcast.data.ReminderRequest;
import com.jbr.middletier.podcast.data.StatusResponse;
import com.jbr.middletier.podcast.dataaccess.ReminderRespository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/jbr/ext/podcast/reminder")
public class ReminderController {
    final static private Logger LOG = LoggerFactory.getLogger(Force.class);

    private final
    ReminderRespository reminderRespository;

    @Autowired
    public ReminderController(ReminderRespository reminderRespository) {
        this.reminderRespository = reminderRespository;
    }

    @RequestMapping(method= RequestMethod.POST)
    public @ResponseBody StatusResponse createReminder(@RequestBody ReminderRequest reminder) {
        Reminder newReminder = new Reminder(reminder);

        this.reminderRespository.save(newReminder);
        LOG.info("Added reminder " + reminder.getWhat());

        return new StatusResponse();
    }
}
