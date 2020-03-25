/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

/**
 *
 * @author matto
 */
@Entity
public class PaintCategory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paintCategoryId;
    
    @Column(nullable = false, unique = true)
    private String categoryName;
    
    @ManyToMany
    private List<Paint> paints;

    public PaintCategory() {
        paints = new ArrayList<>();
    }

    public PaintCategory(String categoryName) {
        this();
        this.categoryName = categoryName;
    }
    
    public Long getPaintCategoryId() {
        return paintCategoryId;
    }

    public void setPaintCategoryId(Long paintCategoryId) {
        this.paintCategoryId = paintCategoryId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (paintCategoryId != null ? paintCategoryId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the paintCategoryId fields are not set
        if (!(object instanceof PaintCategory)) {
            return false;
        }
        PaintCategory other = (PaintCategory) object;
        if ((this.paintCategoryId == null && other.paintCategoryId != null) || (this.paintCategoryId != null && !this.paintCategoryId.equals(other.paintCategoryId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.PaintCategory[ id=" + paintCategoryId + " ]";
    }

    /**
     * @return the categoryName
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * @param categoryName the categoryName to set
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * @return the paints
     */
    public List<Paint> getPaints() {
        return paints;
    }

    /**
     * @param paints the paints to set
     */
    public void setPaints(List<Paint> paints) {
        this.paints = paints;
    }
    
}
