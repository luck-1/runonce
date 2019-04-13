package com.runonce.model.http;

import com.runonce.model.eventpublic.EventsTable;
import lombok.Data;

@Data
public class EventsTableParam {

    private  EventsTable eventsTable;

    private String directory;

    private String thisEventName;
}