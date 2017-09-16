package eu.canpack.fip.bo.attachment;

import eu.canpack.fip.bo.drawing.Drawing;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.time.ZonedDateTime;

/**
 * CP S.A.
 * Created by lukasz.mochel on 28.07.2017.
 */
@Entity
@Table(name = "attachment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "attachment")
public class Attachment {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "path",nullable = false)
    private String path;

    @Column(name="name",nullable = false)
    private String name;

    @Column(name = "data_content_type", nullable = false)
    private String dataContentType;

    @Column(name = "upload_date",nullable = false)
    private ZonedDateTime uploadDate;

    @ManyToOne
    private Drawing drawing;


    public Attachment id(Long id) {
        this.id = id;
        return this;
    }

    public Attachment path(String path) {
        this.path = path;
        return this;
    }

    public Attachment name(String name) {
        this.name = name;
        return this;
    }

    public Attachment dataContentType(String dataContentType) {
        this.dataContentType = dataContentType;
        return this;
    }



    public Attachment drawing(Drawing drawing) {
        this.drawing = drawing;
        return this;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataContentType() {
        return dataContentType;
    }

    public void setDataContentType(String dataContentType) {
        this.dataContentType = dataContentType;
    }


    public Drawing getDrawing() {
        return drawing;
    }

    public void setDrawing(Drawing drawing) {
        this.drawing = drawing;
    }

    public Attachment uploadDate(ZonedDateTime uploadDate) {
        this.uploadDate = uploadDate;
        return this;
    }

    public ZonedDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(ZonedDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Attachment that = (Attachment) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Attachment{" +
            "id=" + id +
            ", path='" + path + '\'' +
            ", name='" + name + '\'' +
            ", dataContentType='" + dataContentType + '\'' +
            ", uploadData=" + uploadDate +
            '}';
    }
}
