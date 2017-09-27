package eu.canpack.fip.config;

/**
 * CP S.A.
 * Created by lukasz.mochel on 22.05.2017.
 */
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import org.hibernate.engine.jdbc.internal.BasicFormatterImpl;
import org.hibernate.engine.jdbc.internal.Formatter;

/**
 * Created by Igor Dmitriev on 1/3/16
 */
public class Test6SpySqlFormatter implements MessageFormattingStrategy {

    private final Formatter formatter = new BasicFormatterImpl();

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql) {
        if (sql.isEmpty()) {
            return "";
        }
        String template = "Hibernate: %s %s \n{elapsed: %sms}";
        String batch = "batch".equals(category) ? ((elapsed == 0) ? "add batch" : "execute batch") : "";
        return String.format(template, batch, formatter.format(sql), elapsed);
//        String message=now+" | connection id: "+connectionId+" | category:"+category+
//            " | prepared:"+prepared+""+formatter.format(sql)+"\nelapsed: "+elapsed +"[ms]";
//        return message;
    }
}
