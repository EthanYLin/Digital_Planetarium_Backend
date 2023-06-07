package com.sp04.digital_planetarium.entity;

import com.sp04.digital_planetarium.utils.Utils;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Embeddable
public class Figure {

    //TODO: Future work: get min and max value from database
    @Min(0)
    @Max(1)
    @Column(nullable = false)
    private Integer headGNo;

    @Min(0)
    @Max(11)
    @Column(nullable = false)
    private Integer headCNo;

    @Min(0)
    @Max(3)
    @Column(nullable = false)
    private Integer bodyGNo;

    @Min(0)
    @Max(11)
    @Column(nullable = false)
    private Integer bodyCNo;

    public Integer getHeadGNo() {
        return headGNo;
    }

    public void setHeadGNo(Integer headG) {
        this.headGNo = headG;
    }

    public Integer getHeadCNo() {
        return headCNo;
    }

    public void setHeadCNo(Integer headC) {
        this.headCNo = headC;
    }

    public Integer getBodyGNo() {
        return bodyGNo;
    }

    public void setBodyGNo(Integer bodyG) {
        this.bodyGNo = bodyG;
    }

    public Integer getBodyCNo() {
        return bodyCNo;
    }

    public void setBodyCNo(Integer bodyC) {
        this.bodyCNo = bodyC;
    }

    public Figure() {
        //TODO: get default value from database
        this.headCNo = this.headGNo = this.bodyCNo = this.bodyGNo = 0;
    }

    public Figure(boolean useRandom) {
        if (useRandom) {
            //TODO: get from database
            this.headGNo = Utils.randomInt(0, 1);
            this.bodyGNo = Utils.randomInt(0, 3);
            this.headCNo = Utils.randomInt(0, 11);
            this.bodyCNo = Utils.randomInt(0, 11);
        } else {
            //TODO: get default value from database
            this.headCNo = this.headGNo = this.bodyCNo = this.bodyGNo = 0;
        }
    }

}
