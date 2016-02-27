package info.axurez.database.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

/**
 * Created by axurez on 2016/2/2.
 */
@Entity
@Table(name = "questions")
public class Question implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "zhihu_id", unique = true)
    private long zhihuId;
    @Column(name = "title")
    private String title;
    @Column(name = "author_zhihu_id")
    private String authorZhihuId;
    @Column(name = "author_current_name")
    private String authorCurrentName;
    @Column(name = "author_current_signature")
    private String authorCurrentSignature;
    @Column(name = "content")
    private String content;
    @Column(name = "time_updated")
    private Date timeUpdated;
    @Column(name = "time_crawled")
    private Date timeCrawled;
    @Column(name = "upvote_count")
    private long upvoteCount;
    @Column(name = "comment_count")
    private long commentCount;
    @ManyToMany(
        targetEntity = Question.class,
        mappedBy = "questionsRelatedBy"
    )
    private Set<Question> relatedQuestions = new HashSet<>();
    @ManyToMany(
        targetEntity = Question.class
    )
    private Set<Question> questionsRelatedBy = new HashSet<>();
    @ManyToMany(
        targetEntity = Topic.class,
        mappedBy = "questions"
    )
    private Set<Topic> topics = new HashSet<>();

    public long getId() {
        return id;
    }

    public long getZhihuId() {
        return zhihuId;
    }

    public void setZhihuId(long zhihuId) {
        this.zhihuId = zhihuId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorZhihuId() {
        return authorZhihuId;
    }

    public void setAuthorZhihuId(String authorZhihuId) {
        this.authorZhihuId = authorZhihuId;
    }

    public String getAuthorCurrentName() {
        return authorCurrentName;
    }

    public void setAuthorCurrentName(String authorCurrentName) {
        this.authorCurrentName = authorCurrentName;
    }

    public String getAuthorCurrentSignature() {
        return authorCurrentSignature;
    }

    public void setAuthorCurrentSignature(String authorCurrentSignature) {
        this.authorCurrentSignature = authorCurrentSignature;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTimeUpdated() {
        return timeUpdated;
    }

    public void setTimeUpdated(Date timeUpdated) {
        this.timeUpdated = timeUpdated;
    }

    public Date getTimeCrawled() {
        return timeCrawled;
    }

    public void setTimeCrawled(Date timeCrawled) {
        this.timeCrawled = timeCrawled;
    }

    public long getUpvoteCount() {
        return upvoteCount;
    }

    public void setUpvoteCount(long upvoteCount) {
        this.upvoteCount = upvoteCount;
    }

    public long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(long commentCount) {
        this.commentCount = commentCount;
    }

    public Set<Question> getRelatedQuestions() {
        return relatedQuestions;
    }

    public void setRelatedQuestions(Set<Question> relatedQuestions) {
        this.relatedQuestions = relatedQuestions;
    }

    public Set<Topic> getTopics() {
        return topics;
    }

    public void setTopics(Set<Topic> topics) {
        this.topics = topics;
    }

    public Set<Question> getQuestionsRelatedBy() {
        return questionsRelatedBy;
    }

    public void setQuestionsRelatedBy(Set<Question> questionsRelatedBy) {
        this.questionsRelatedBy = questionsRelatedBy;
    }

    public Question() {
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{id: ");
        stringBuilder.append(this.id);
        stringBuilder.append(", zid: ");
        stringBuilder.append(this.zhihuId);
        stringBuilder.append(", title: ");
        stringBuilder.append(this.title);
        stringBuilder.append(", content: ");
        stringBuilder.append(this.content);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

}
