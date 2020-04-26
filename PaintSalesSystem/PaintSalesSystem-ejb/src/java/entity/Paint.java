/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

/**
 *
 * @author Elgin Patt
 */
@Entity
public class Paint implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paintId;
    
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false, unique = true)
    private String colourCode;
    @Column(nullable = false, precision = 11, scale = 2)
    @DecimalMin("0.00")
    @Digits(integer = 9, fraction = 2)     
    private BigDecimal price;
    @Column(nullable = false)
    @Min(0)
    private Integer quantityOnHand;
    @Column(nullable = false)
    @Min(0)
    private Integer reorderQuantity;
    @Column(nullable = false)
    @Positive
    @Min(1)
    @Max(5)
    private Integer paintRating;


    @ManyToMany(mappedBy = "paints")
    private List<PaintCategory> paintCategories;
    @ManyToMany(mappedBy = "paints")
    private List<PaintTag> tags;
    
    public Paint() {
        paintCategories = new ArrayList<>();
        tags = new ArrayList<>();
    }

    public Paint(String name, String colourCode, BigDecimal price, Integer quantityOnHand, Integer reorderQuantity, Integer paintRating) {
        this();
        this.name = name;
        this.colourCode = colourCode;
        this.price = price;
        this.quantityOnHand = quantityOnHand;
        this.reorderQuantity = reorderQuantity;
        this.paintRating = paintRating;
    }

    public Long getPaintId() {
        return paintId;
    }

    public void setPaintId(Long paintId) {
        this.paintId = paintId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (paintId != null ? paintId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the paintId fields are not set
        if (!(object instanceof Paint)) {
            return false;
        }
        Paint other = (Paint) object;
        if ((this.paintId == null && other.paintId != null) || (this.paintId != null && !this.paintId.equals(other.paintId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Paint[ id=" + paintId + " ]";
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the colourCode
     */
    public String getColourCode() {
        return colourCode;
    }

    /**
     * @param colourCode the colourCode to set
     */
    public void setColourCode(String colourCode) {
        this.colourCode = colourCode;
    }

    /**
     * @return the price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * @return the paintCategories
     */
    public List<PaintCategory> getPaintCategories() {
        return paintCategories;
    }

    /**
     * @param paintCategories the paintCategories to set
     */
    public void setPaintCategories(List<PaintCategory> paintCategories) {
        this.paintCategories = paintCategories;
    }

    /**
     * @return the tags
     */
    public List<PaintTag> getTags() {
        return tags;
    }

    /**
     * @param tags the tags to set
     */
    public void setTags(List<PaintTag> tags) {
        this.tags = tags;
    }

    /**
     * @return the quantityOnHand
     */
    public Integer getQuantityOnHand() {
        return quantityOnHand;
    }

    /**
     * @param quantityOnHand the quantityOnHand to set
     */
    public void setQuantityOnHand(Integer quantityOnHand) {
        this.quantityOnHand = quantityOnHand;
    }

    /**
     * @return the reorderQuantity
     */
    public Integer getReorderQuantity() {
        return reorderQuantity;
    }

    /**
     * @param reorderQuantity the reorderQuantity to set
     */
    public void setReorderQuantity(Integer reorderQuantity) {
        this.reorderQuantity = reorderQuantity;
    }

    /**
     * @return the paintRating
     */
    public Integer getPaintRating() {
        return paintRating;
    }

    /**
     * @param paintRating the paintRating to set
     */
    public void setPaintRating(Integer paintRating) {
        this.paintRating = paintRating;
    }
    
}
