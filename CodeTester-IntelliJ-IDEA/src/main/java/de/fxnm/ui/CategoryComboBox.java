package de.fxnm.ui;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.ui.ComboBox;

import java.awt.Dimension;
import java.util.Arrays;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import de.fxnm.util.CodeTesterBundle;
import de.fxnm.web.components.category.Category;

public class CategoryComboBox {

    public static final Logger LOG = Logger.getInstance(CategoryComboBox.class);
    public static final Category reloadFailedInfo = new Category(-1,
            CodeTesterBundle.message("plugin.ui.categoryBox.reloadFailedCategory"));
    private static final int MAXIMUM_SIZE = 350;
    private static final int PREFERRED_SIZE = 250;

    private final ComboBox<Category> comboBox = new ComboBox<>();
    private final DefaultComboBoxModel<Category> comboBoxModel = new DefaultComboBoxModel<>();


    public CategoryComboBox() {
        this.comboBox.setModel(this.comboBoxModel);

        final int preferredHeight = this.comboBox.getPreferredSize().height;
        this.comboBox.setPreferredSize(new Dimension(PREFERRED_SIZE, preferredHeight));
        this.comboBox.setMaximumSize(new Dimension(MAXIMUM_SIZE, preferredHeight));
    }

    public void setReloadFailed() {
        this.comboBoxModel.removeAllElements();

        this.comboBoxModel.addElement(reloadFailedInfo);
        this.comboBoxModel.setSelectedItem(reloadFailedInfo);

        LOG.info(CodeTesterBundle.message("plugin.ui.categoryBox.reloadFailed.loggerMessage"));
    }

    public void removeCategories() {
        this.comboBoxModel.removeAllElements();

        LOG.info(CodeTesterBundle.message("plugin.ui.categoryBox.removeCategories.loggerMessage"));
    }

    public void setNewCategories(final Category[] categories) {
        if (categories.length == 0) {
            this.setReloadFailed();
            return;
        }

        Arrays.sort(categories);

        this.comboBoxModel.removeAllElements();

        for (final Category c : categories) {
            this.comboBoxModel.addElement(c);
        }
        this.comboBoxModel.setSelectedItem(categories[0]);

        LOG.info(String.format(CodeTesterBundle.message("plugin.ui.categoryBox.addCategories.loggerMessage"),
                categories.length));
    }

    public JComboBox<Category> getComboBox() {
        return this.comboBox;
    }

    public Category getSelectedCategory() {
        return (Category) this.comboBox.getSelectedItem();
    }
}
