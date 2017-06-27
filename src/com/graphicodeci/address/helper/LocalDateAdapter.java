package com.graphicodeci.address.helper;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;

/**
 * Created by MARTIAL ANOUMAN on 6/25/2017.
 *
 * Adapter (for JAXB) to convert between LocalDAte and String
 * representation such as '14.09.1985.
 *
 * @author Martial Anouman
 */
public class LocalDateAdapter extends XmlAdapter<String,LocalDate> {

    @Override
    public LocalDate unmarshal(String v) throws Exception {
        return DateHelper.parse(v);
    }

    @Override
    public String marshal(LocalDate v) throws Exception {
        return DateHelper.format(v);
    }
}
