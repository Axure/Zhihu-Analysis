package info.axurez.database.entities;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by zhenghu on 2016-01-23.
 */
@Entity
@Table(name = "zhihu_pages")
public class ZhihuPage implements java.io.Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private long id;

    private String title;
    private String content;
    private String author_id;
    private Date created_at;

    ZhihuPage() {

    }

    ZhihuPage(String url) {

    }

    ZhihuPage(String title_, String content_, String author_id_, Date created_at_) {
        this.title = title_;
        this.content = content_;
        this.author_id = author_id_;
        this.created_at = created_at_;
    }
}
