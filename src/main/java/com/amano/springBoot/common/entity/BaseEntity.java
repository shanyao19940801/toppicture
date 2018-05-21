package com.amano.springBoot.common.entity;

//import lombok.Getter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
//@Getter
public class BaseEntity  implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(insertable = false, updatable = false, columnDefinition = "DATETIME(3) DEFAULT CURRENT_TIMESTAMP(3)")
    protected LocalDateTime createDt;

    @Column(insertable = false, updatable = false, columnDefinition = "DATETIME(3) ON UPDATE CURRENT_TIMESTAMP(3)")
    protected LocalDateTime modifyDt;

    public boolean equals(Object other) {
        if (other == null || other.getClass() != this.getClass())
            return false;
        EqualsBuilder eb = new EqualsBuilder();
        eb.append(this.getId(), ((BaseEntity) other).getId());
        return eb.isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreateDt() {
        return createDt;
    }

    public void setCreateDt(LocalDateTime createDt) {
        this.createDt = createDt;
    }

    public LocalDateTime getModifyDt() {
        return modifyDt;
    }

    public void setModifyDt(LocalDateTime modifyDt) {
        this.modifyDt = modifyDt;
    }
}
