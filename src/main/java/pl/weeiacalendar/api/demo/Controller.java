package pl.weeiacalendar.api.demo;

import org.springframework.web.bind.annotation.*;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.util.RandomUidGenerator;
import net.fortuna.ical4j.util.UidGenerator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/downloadICS")
public class Controller {
    @GetMapping
    String downloadICS()
    {
        String output = new String();
        try
        {
            Map<String, Integer> months = new HashMap<String, Integer>();
            months.put("styczeń", java.util.Calendar.JANUARY);
            months.put("luty", java.util.Calendar.FEBRUARY);
            months.put("marzec", java.util.Calendar.MARCH);
            months.put("kwiecień", java.util.Calendar.APRIL);
            months.put("maj", java.util.Calendar.MAY);
            months.put("czerwiec", java.util.Calendar.JUNE);
            months.put("lipiec", java.util.Calendar.JULY);
            months.put("sierpień", java.util.Calendar.AUGUST);
            months.put("wrzesień", java.util.Calendar.SEPTEMBER);
            months.put("październik", java.util.Calendar.OCTOBER);
            months.put("listopad", java.util.Calendar.NOVEMBER);
            months.put("grudzień", java.util.Calendar.DECEMBER);

            Calendar calendar = new Calendar();
            calendar.getProperties().add(new ProdId("-//Igor Jarek//iCal4j 1.0//PL"));
            calendar.getProperties().add(Version.VERSION_2_0);
            calendar.getProperties().add(CalScale.GREGORIAN);

            Document document = Jsoup.connect("http://www.weeia.p.lodz.pl").get();
            Elements monthAndYearElements = document.getElementsByClass("nawigacja");

            Element monthAndYearElement = monthAndYearElements.get(0).children().get(0);
            String monthAndYear = monthAndYearElement.html();
            String[] monthAndYearSplitted = monthAndYear.split(" ");
            String month = monthAndYearSplitted[0].toLowerCase();
            int year = Integer.parseInt(monthAndYearSplitted[1]);

            Elements days = document.getElementsByClass("active");
            for(Element td : days)
            {
                if(td.tagName().compareTo("td") == 0)
                {
                    Elements descriptions = td.getElementsByTag("p");

                    int day = Integer.parseInt(td.child(0).html());
                    String description = descriptions.get(0).html();

                    java.util.Calendar cal = java.util.Calendar.getInstance();
                    cal.set(java.util.Calendar.YEAR, year);
                    cal.set(java.util.Calendar.MONTH, months.get(month));
                    cal.set(java.util.Calendar.DAY_OF_MONTH, day);

                    VEvent event = new VEvent(new Date(cal.getTime()), description);
                    UidGenerator ug = new RandomUidGenerator();
                    event.getProperties().add(ug.generateUid());
                    calendar.getComponents().add(event);
                }
            }

            output = calendar.toString();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return output;
    }
}
