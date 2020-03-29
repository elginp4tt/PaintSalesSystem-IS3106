/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.PaintCategory;
import java.util.List;
import javax.ejb.Local;
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewCategoryException;
import util.exception.DeleteCategoryException;
import util.exception.InputDataValidationException;
import util.exception.UpdateCategoryException;

/**
 *
 * @author matto
 */
@Local
public interface PaintCategorySessionBeanLocal {

    public PaintCategory createNewPaintCategory(PaintCategory newPaintCategory, Long parentCategoryId) throws InputDataValidationException, CreateNewCategoryException;

    public List<PaintCategory> retrieveAllCategories();

    public List<PaintCategory> retrieveAllLeafCategories();

    public List<PaintCategory> retrieveAllCategoriesWithoutPaint();

    public PaintCategory retrieveCategoryByCategoryId(Long categoryId) throws CategoryNotFoundException;

    public void updateCategory(PaintCategory paintCategory, Long parentCategoryId) throws InputDataValidationException, CategoryNotFoundException, UpdateCategoryException;

    public void deleteCategory(Long categoryId) throws CategoryNotFoundException, DeleteCategoryException;

    public List<PaintCategory> retrieveAllRootCategories();
    
}
