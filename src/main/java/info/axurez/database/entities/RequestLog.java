package info.axurez.database.entities;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by axurez on 2016/2/25.
 */
@Entity
@Table(name = "request_logs")
public class RequestLog {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private long id;

    @Column
    private String url;

    @Column
    private Date time;

    public RequestLog(String url, Date time) {
        this.url = url;
        this.time = time;
    }
}
