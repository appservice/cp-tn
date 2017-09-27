package eu.canpack.fip.bo.remark;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

/**
 * CP S.A.
 * Created by lukasz.mochel on 18.09.2017.
 */
public class EstimationRemarkDTO {
   private Long id;

   @NotNull
   private String remark;
   private ZonedDateTime createdAt;
   private String createdByName;
   private Long createdById;
   private Long estimationId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long createdById) {
        this.createdById = createdById;
    }

    public Long getEstimationId() {
        return estimationId;
    }

    public void setEstimationId(Long estimationId) {
        this.estimationId = estimationId;
    }

    @Override
    public String toString() {
        return "EstimationRemarkDTO{" +
            "id=" + id +
            ", remark='" + remark + '\'' +
            ", createdAt=" + createdAt +
            ", createdByName='" + createdByName + '\'' +
            ", createdById=" + createdById +
            ", estimationId=" + estimationId +
            '}';
    }
}
