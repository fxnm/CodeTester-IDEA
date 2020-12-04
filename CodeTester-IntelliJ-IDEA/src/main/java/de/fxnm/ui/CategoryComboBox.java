package de.fxnm.ui;

import com.intellij.openapi.ui.ComboBox;

import java.awt.Dimension;
import java.util.Arrays;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import de.fxnm.util.CodeTesterBundle;
import de.fxnm.web.components.category.Category;

public class CategoryComboBox {
    private static final int MAXIMUM_SIZE = 350;
    private static final int PREFERRED_SIZE = 250;

    private final ComboBox<Category> comboBox = new ComboBox<>();
    private final DefaultComboBoxModel<Category> comboBoxModel = new DefaultComboBoxModel<>();
    private final Category[] defaultCategories = new Category[]{new Category(-1, CodeTesterBundle.message("plugin.check.noCategorySelected"))};


    public CategoryComboBox() {
        this.comboBox.setModel(this.comboBoxModel);
        this.setCategories(this.defaultCategories);

        final int preferredHeight = this.comboBox.getPreferredSize().height;
        this.comboBox.setPreferredSize(new Dimension(PREFERRED_SIZE, preferredHeight));
        this.comboBox.setMaximumSize(new Dimension(MAXIMUM_SIZE, preferredHeight));
    }

    public void setCategories(final Category[] categories) {
        if (categories.length == 0) {
            this.comboBoxModel.removeAllElements();
            this.setCategories(this.defaultCategories);
            return;
        }

        Arrays.sort(categories);

        this.comboBoxModel.removeAllElements();

        for (final Category c : categories) {
            this.comboBoxModel.addElement(c);
        }
        this.comboBoxModel.setSelectedItem(categories[0]);
    }

    public JComboBox<Category> getComboBox() {
        return this.comboBox;
    }

    public Category getSelectedCategory() {
        return (Category) this.comboBox.getSelectedItem();
    }
}
