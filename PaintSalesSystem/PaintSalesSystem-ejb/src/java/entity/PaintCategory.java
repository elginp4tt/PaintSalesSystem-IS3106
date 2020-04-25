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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;

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
    
    @Column(length = 128)
    @Size(max = 128)
    private String description;
    
    @OneToMany(mappedBy = "parentCategoryEntity")
    private List<PaintCategory> subCategoryEntities;
    
    @ManyToOne
    private PaintCategory parentCategoryEntity;
    
    @ManyToMany
    private List<Paint> paints;

    public PaintCategory() {
        paints = new ArrayList<>();
        subCategoryEntities = new ArrayList<>();
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

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the subCategoryEntities
     */
    public List<PaintCategory> getSubCategoryEntities() {
        return subCategoryEntities;
    }

    /**
     * @param subCategoryEntities the subCategoryEntities to set
     */
    public void setSubCategoryEntities(List<PaintCategory> subCategoryEntities) {
        this.subCategoryEntities = subCategoryEntities;
    }

    /**
     * @return the parentCategoryEntity
     */
    public PaintCategory getParentCategoryEntity() {
        return parentCategoryEntity;
    }

    /**
     * @param parentCategoryEntity the parentCategoryEntity to set
     */
    public void setParentCategoryEntity(PaintCategory parentCategoryEntity) {
        this.parentCategoryEntity = parentCategoryEntity;
    }
    
}
