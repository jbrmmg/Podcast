package com.jbr.middletier.podcast.control;

import com.jbr.middletier.podcast.data.Reminder;
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

import java.util.Optional;

@Controller
@RequestMapping("/jbr")
public class ReminderController {
    final static private Logger LOG = LoggerFactory.getLogger(Force.class);

    private final
    ReminderRespository reminderRespository;

    @Autowired
    public ReminderController(ReminderRespository reminderRespository) {
        this.reminderRespository = reminderRespository;
    }

    @RequestMapping(path="/ext/podcast/reminder", method= RequestMethod.POST)
    public @ResponseBody StatusResponse createReminder(@RequestBody Reminder reminder) {
        LOG.info("Create new reminder " + reminder);

        Optional<Reminder> existing = reminderRespository.findById(reminder.getWhat());
        if(existing.isPresent()) {
            return new StatusResponse(reminder.getWhat() + " already exists");
        }

        this.reminderRespository.save(reminder);

        return new StatusResponse();
    }

    @RequestMapping(path="/int/podcast/reminder", method= RequestMethod.GET)
    public @ResponseBody Iterable<Reminder> getReminders() {
        LOG.info("Get the reminders");

        return reminderRespository.findAll();
    }

    @RequestMapping(path="/int/podcast/reminder", method= RequestMethod.PUT)
    public @ResponseBody StatusResponse updateReminder(@RequestBody Reminder reminder) {
        LOG.info("Update the reminder" + reminder.getWhat());

        Optional<Reminder> existing = reminderRespository.findById(reminder.getWhat());
        if(!existing.isPresent()) {
            return new StatusResponse(reminder.getWhat() + " does not exist");
        }

        reminderRespository.save(reminder);

        return new StatusResponse();
    }

    @RequestMapping(path="/int/podcast/reminder", method= RequestMethod.DELETE)
    public @ResponseBody StatusResponse deleteReminder(@RequestBody Reminder reminder) {
        LOG.info("Delete the reminder" + reminder.getWhat());

        Optional<Reminder> existing = reminderRespository.findById(reminder.getWhat());
        if(!existing.isPresent()) {
            return new StatusResponse(reminder.getWhat() + " does not exist");
        }

        reminderRespository.delete(reminder);

        return new StatusResponse();
    }
}
