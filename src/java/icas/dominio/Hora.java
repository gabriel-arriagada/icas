/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icas.dominio;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Gabriel
 */
public class Hora {

    private final SimpleDateFormat simpleDateFormat;

    public Hora() {
        this.simpleDateFormat = new SimpleDateFormat("HH:mm");
    }

    public String getHora() {
        Date date = new Date();
        return this.simpleDateFormat.format(date);
    }

    public String getHoraMasX(int minutos) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, minutos);
        return this.simpleDateFormat.format(calendar.getTime());
    }

    public Time getHoraSql(String hora) throws ParseException {        
        Date date = new SimpleDateFormat("HH:mm").parse(hora);
        return new Time(date.getTime());
    }
}
